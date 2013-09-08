package DataProvider;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Deal {

	private String dealId;
	private String sellerId;
	private float discount;
	private ArrayList<String> tag;
	private Timestamp startDate;
	private Timestamp endDate;
	private float latitude;
	private float longitude;

	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	Deal(DBObject obj) {
		this.sellerId = obj.get("SellerID").toString();
		this.startDate = (Timestamp) obj.get("StartDate");
		this.endDate = (Timestamp) obj.get("EndDate");
		this.discount = Float.parseFloat(obj.get("Discount").toString());
		this.dealId = obj.get("_id").toString();
		this.latitude = Float.parseFloat(obj.get("Latitude").toString());
		this.longitude = Float.parseFloat(obj.get("Longitude").toString());
		this.tag = new ArrayList<String>();
		tag.add(obj.get("Tag").toString());
	}

	public Deal(String sellerId, float discount, ArrayList<String> tag,
			Timestamp startDate, Timestamp endDate) {
		super();
		this.sellerId = sellerId;
		this.discount = discount;
		this.tag = tag;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public float getDiscountPercent() {
		return discount;
	}

	public ArrayList<String> getTag() {
		return tag;
	}

	public void setTag(ArrayList<String> tag) {
		this.tag = tag;
	}

	public String getDealId() {
		return dealId;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discount = discountPercent;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public void update() throws UnknownHostException {
		DealDataConnection.reinsertDeal(this);
	}

	/**
	 * removes the current deal from the database
	 * 
	 * @throws UnknownHostException
	 */
	public void removeDeal() throws UnknownHostException {
		DealDataConnection.deleteDeal(dealId);
	}

	/**
	 * removes the deal corresponding to specified deal id from the database
	 * 
	 * @param dealId
	 * @throws UnknownHostException
	 */
	static void removeDeal(String dealId) throws UnknownHostException {
		DealDataConnection.deleteDeal(dealId);
	}

	/**
	 * adds multiple deals corresponding to the tags. For every tag a new deal
	 * is inserted into the database
	 * 
	 * @param sellerId
	 * @param tags
	 * @param startDate
	 * @param endDate
	 * @param discount
	 * @throws UnknownHostException
	 */
	public void insert() throws UnknownHostException {
		DealDataConnection.addDeals(this);
	}

	public static ArrayList<Deal> searchDeals(String sellerId)
			throws UnknownHostException {
		return DealDataConnection.searchDeals(sellerId);
	}
}

class DealDataConnection {

	private static MongoClient mongoClient;
	private static DB db;
	private static DBCollection dbcDeal;
	private static DBCollection dbCounters;
	private static BasicDBObject query = new BasicDBObject();
	private static DBCursor cursor;

	private static void load() throws UnknownHostException {
		if (mongoClient == null) {
			mongoClient = new MongoClient();
			db = mongoClient.getDB("dealsbytheway");
			dbcDeal = db.getCollection("DealseInfo");
			dbCounters = db.getCollection("counters");
		}
	}

	static ArrayList<Deal> searchDeals(String sellerId)
			throws UnknownHostException {
		load();

		query.clear();
		query.append("SellerID", sellerId);

		cursor = dbcDeal.find(query);
		HashMap<String, Deal> map = new HashMap<String, Deal>();
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			String key = obj.get("_id").toString();
			if (!map.containsKey(key)) {
				Deal temp = new Deal(obj);
				map.put(key, temp);
			} else
				map.get(key).getTag().add(obj.get("Tag").toString());
		}
		ArrayList<Deal> list = new ArrayList<>();
		for (String key : map.keySet()) {
			list.add(map.get(key));
		}
		cursor.close();
		return list;
	}

	static void deleteDeal(String dealId) throws UnknownHostException {
		load();
		query.clear();
		query.append("did", dealId);
		dbcDeal.remove(query);
	}

	static void addDeals(Deal d) throws UnknownHostException {
		load();

		String sellerId = d.getSellerId();
		SellerInfoFetcher obj = new SellerInfoFetcher(sellerId);
		float lat = obj.getLatitude();
		float longit = obj.getLongitude();

		Timestamp startDate = d.getStartDate();
		Timestamp endDate = d.getEndDate();
		float discount = d.getDiscountPercent();
		String dealId = nextIncrement();
		for (String tag : d.getTag()) {
			query.clear();
			query.append("StartDate", startDate);
			query.append("EndDate", endDate);
			query.append("TAG", tag);
			query.append("Latitude", lat);
			query.append("Longitude", longit);
			query.append("Discount", discount);
			query.append("_id", dealId);
			query.append("SellerID", sellerId);
			dbcDeal.insert(query);
		}
	}

	static void reinsertDeal(Deal d) throws UnknownHostException {
		load();
		deleteDeal(d.getDealId());
		String val = nextIncrement();
		for (String tag : d.getTag()) {
			query.clear();
			query.append("StartDate", d.getStartDate());
			query.append("EndDate", d.getEndDate());
			query.append("Latitude", d.getLatitude());
			query.append("Longitude", d.getLongitude());
			query.append("Discount", d.getDiscountPercent());
			query.append("TAG", tag);
			query.append("SellerId", d.getSellerId());
			query.append("_id", val);
			dbcDeal.insert(query);
		}
	}

	static String nextIncrement() {
		BasicDBObject update = new BasicDBObject();
		update.append("$inc", new BasicDBObject().append("seq", 1));
		query.clear();
		query.append("_id", "dealId");
		DBObject obj1 = dbCounters.findAndModify(query, null, update);
		String dealId = obj1.get("seq").toString();
		return dealId;
	}
}
