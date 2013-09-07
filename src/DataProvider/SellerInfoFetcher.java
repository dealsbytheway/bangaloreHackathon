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

	private String address;
	private ArrayList<String> products;
	private int contactNumber;
	private ArrayList<String> types;

	private HashMap<String, ArrayList<String>> typeDetails;
	private HashMap<String, Integer> idPerType;
	private String name;
	private int sid;

	public String getAddress() {
		return address;
	}

	public ArrayList<String> getProducts() {
		return products;
	}

	public int getContactNumber() {
		return contactNumber;
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public HashMap<String, ArrayList<String>> getTypeDetails() {
		return typeDetails;
	}

	public HashMap<String, Integer> getIdPerType() {
		return idPerType;
	}

	public String getName() {
		return name;
	}

	public SellerInfoFetcher(int sid) throws UnknownHostException {
		DataConnection.loadSeller(sid, this);
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
		Object list = dbObj.get("Type");
		System.out.println(list);

	}
}
