package DataProvider;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class SellerInfoFetcher {

	public static void main(String[] args) throws UnknownHostException {
		SellerInfoFetcher obj = new SellerInfoFetcher(1);
		System.out.println(obj);
	}

	private int sid;
	private String name;
	private String address;
	private String contactNumber;

	private ArrayList<String> types;
	private HashMap<String, ArrayList<String>> typeDetails;
	private HashMap<String, ArrayList<String>> perEntryDetil;

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

	public SellerInfoFetcher(int sid) throws UnknownHostException {
		this.sid = sid;
		this.typeDetails = new HashMap<String, ArrayList<String>>();
		this.perEntryDetil = new HashMap<String, ArrayList<String>>();
		DataConnection.loadSeller(sid, this);
	}

	public HashMap<String, ArrayList<String>> getPerEntryDetil() {
		return perEntryDetil;
	}

	public void setPerEntryDetil(
			HashMap<String, ArrayList<String>> perEntryDetil) {
		this.perEntryDetil = perEntryDetil;
	}

	@Override
	public String toString() {
		return "SellerInfoFetcher [sid=" + sid + ", name=" + name
				+ ", address=" + address + ", contactNumber=" + contactNumber
				+ ", types=" + types + ", typeDetails=" + typeDetails
				+ ", perEntryDetil=" + perEntryDetil + "]";
	}

}

class DataConnection {

	private static MongoClient mongoClient;
	private static DB db;
	private static DBCollection dbc;
	private static BasicDBObject query = new BasicDBObject();
	private static DBCursor cursor;

	static void loadSeller(int s1, SellerInfoFetcher sif)
			throws UnknownHostException {
		if (mongoClient == null) {
			mongoClient = new MongoClient();
			db = mongoClient.getDB("dealsbytheway");
			dbc = db.getCollection("sellerInfo");
		}

		query.clear();
		query.append("sid", s1);
		cursor = dbc.find(query);

		DBObject dbObj = cursor.next();

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
				sif.getPerEntryDetil().put(entry,
						(ArrayList<String>) dbObj.get(entry));
			}
		}

	}
}
