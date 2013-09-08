package DataProvider;

import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import DataProviderInterface.SellerDataProvider;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class SellerInfoFetcher implements SellerDataProvider {

	public static void main(String[] args) throws UnknownHostException {
		SellerInfoFetcher obj = new SellerInfoFetcher(1 + "");
		System.out.println(obj);
	}

	private String sid;
	private String name;
	private String address;
	private String contactNumber;

	private ArrayList<String> types;
	private HashMap<String, ArrayList<String>> typeDetails;
	private HashMap<String, ArrayList<String>> serviceDetail;
	private float latitude;
	private float longitude;

	public void setAddress(String address) {
		this.address = address;
	}

	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}

	public void setTypeDetails(HashMap<String, ArrayList<String>> typeDetails) {
		this.typeDetails = typeDetails;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public HashMap<String, ArrayList<String>> getTypeDetails() {
		return typeDetails;
	}

	public String getName() {
		return name;
	}

	public SellerInfoFetcher(String sid) throws UnknownHostException {
		this.sid = sid;
		this.typeDetails = new HashMap<String, ArrayList<String>>();
		this.serviceDetail = new HashMap<String, ArrayList<String>>();
		SellerDataConnection.loadSeller(sid, this);
	}

	@Override
	public String toString() {
		return "SellerInfoFetcher [sid=" + sid + ", name=" + name
				+ ", address=" + address + ", contactNumber=" + contactNumber
				+ ", types=" + types + ", typeDetails=" + typeDetails
				+ ", perEntryDetil=" + serviceDetail + "]";
	}

	public HashMap<String, ArrayList<String>> getServiceDetail() {
		return serviceDetail;
	}

	public void setServiceDetail(
			HashMap<String, ArrayList<String>> serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	@Override
	public void cancelDealFromSeller(String dealId) {
		try {
			Deal.removeDeal(dealId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Deal> getSellerCurrentActiveDeals(String sellerId) {
		try {
			return Deal.searchDeals(sellerId);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<String> getSellerOfferedServices(String sellerId) {
		throw new NotImplementedException();
	}

	@Override
	public ArrayList<Deal> getSellerInactiveDeals(String sellerId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	@Override
	public void addNewDealFromSeller(String sellerId, ArrayList<String> tags,
			Timestamp startDate, Timestamp endDate, String discount) {
		Deal d = new Deal(sellerId, discount, tags, startDate, endDate);
		try {
			d.insert();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDealFromSeller(Deal d) {
		try {
			d.update();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void insert() throws UnknownHostException {
		SellerDataConnection.addSeller(this);
	}

}

class SellerDataConnection {

	private static MongoClient mongoClient;
	private static DB db;
	private static DBCollection dbcSeller;
	private static BasicDBObject query = new BasicDBObject();
	private static DBCursor cursor;
	private static DBCollection dbCounters;

	private static void load() throws UnknownHostException {
		if (mongoClient == null) {
			mongoClient = new MongoClient();
			db = mongoClient.getDB("dealsbytheway");
			dbcSeller = db.getCollection("sellerInfo");
			dbCounters = db.getCollection("counters");
		}
	}

	@SuppressWarnings("unchecked")
	static void loadSeller(String s1, SellerInfoFetcher sif)
			throws UnknownHostException {
		load();
		query.clear();
		query.append("_id", s1);
		cursor = dbcSeller.find(query);

		DBObject dbObj = cursor.next();

		sif.setSid(s1);

		for (String key : dbObj.keySet()) {
			if (key.equals("Type"))
				sif.setTypes((ArrayList<String>) dbObj.get("Type"));
			else if (key.equals("Name"))
				sif.setName(dbObj.get("Name").toString());
			else if (key.equals("Contact"))
				sif.setContactNumber(dbObj.get("Contact").toString());
			else if (key.equals("Address"))
				sif.setName(dbObj.get("Address").toString());
		}

		for (String t : sif.getTypes()) {
			ArrayList<String> list = (ArrayList<String>) dbObj.get(t);
			sif.getTypeDetails().put(t, list);
			for (String entry : list) {
				sif.getServiceDetail().put(entry,
						(ArrayList<String>) dbObj.get(entry));
			}
		}
	}

	static void addSeller(SellerInfoFetcher s) throws UnknownHostException {
		load();
		query.clear();
		String val = nextIncrement();
		query.append("_id", val);
		query.append("Name", s.getName());
		query.append("Address", s.getAddress());
		query.append("Contact", s.getContactNumber());
		query.append("Type", s.getTypes());
		HashMap<String, ArrayList<String>> obj = s.getTypeDetails();
		for (String key : obj.keySet()) {
			query.append(key, obj.get(key));
		}

		throw new NotImplementedException();
	}

	static String nextIncrement() {
		BasicDBObject update = new BasicDBObject();
		update.append("$inc", new BasicDBObject().append("seq", 1));
		query.clear();
		query.append("_id", "sellerId");
		DBObject obj1 = dbCounters.findAndModify(query, null, update);
		String dealId = obj1.get("seq").toString();
		return dealId;
	}
}
