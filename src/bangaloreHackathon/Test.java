package bangaloreHackathon;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class Test {

	public static void main(String[] args) throws UnknownHostException {
		MongoClient mongo = new MongoClient("localhost", 27017);
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("database name");
		List<String> dbs = mongo.getDatabaseNames();
		for (String db1 : dbs) {
			System.out.println(db1);
		}

	}
}
