package bangaloreHackathon;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class Test {

	public static void main(String[] args) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB("dealsbytheway");
		System.out.println(db.collectionExists("sellerInfo"));

	}
}
