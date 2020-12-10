package mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * MongoDB连接数据库
 * 
 * @author li.tian
 *
 */
public class MongoDBJDBC2 {
	
	private static String PASSWORD = "Wsf!2020";
	
	public static void main(String[] args) {
		
		try{
		//连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址  
        //ServerAddress()两个参数分别为 服务器地址 和 端口  
        ServerAddress serverAddress = new ServerAddress("dds-uf65ddb19a1906441.mongodb.rds.aliyuncs.com",3717);  
        List<ServerAddress> addrs = new ArrayList<ServerAddress>();  
        addrs.add(serverAddress);  
          
        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码  
        MongoCredential credential = MongoCredential.createScramSha1Credential("root", "admin", PASSWORD.toCharArray());  
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
        credentials.add(credential);  
          
        //通过连接认证获取MongoDB连接  
        MongoClient mongoClient = new MongoClient(addrs,credentials);  
          
        //连接到数据库  
        MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");  
        System.out.println("成功连接到MongoDB数据库...");  
		
		MongoCollection<Document> collection = mongoDatabase.getCollection("ltcol");
		System.out.println("集合 ltcol 选择成功");
		
		MongoDBJDBC2 mongojdbc = new MongoDBJDBC2();
          
        //删除符合条件的第一个文档
//        collection.deleteOne(Filters.eq("by", "Fly"));
		
		//删除所有符合条件的文档
//		collection.deleteMany(Filters.eq("description", "database"));
		
		mongojdbc.add(collection);
		System.out.println("添加数据成功");
		
		List<String> list = null;
		list = mongojdbc.query(collection);
		System.out.println(list.toString());
		
		}catch(Exception e){
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	/**
	 * 检索所有文档
	 * @param collection
	 */
	public List<String> query(MongoCollection<Document> collection){
		
		List<String> list = new ArrayList<String>();
		
		/**
		 * 1.获取迭代器FindIterable<Document>
		 * 2.获取游标MongoCursor<Document>
		 * 3.通过游标遍历检索出的文档集合
		 */
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while(mongoCursor.hasNext()){
			list.add(mongoCursor.next().toJson());
//			System.out.println(mongoCursor.next());
		}
		return list;
	}
	
	/**
	 * 插入文档  
	 * @param collection
	 */
	public void add(MongoCollection<Document> collection){
		/**
		 * 1. 创建文档 org.bson.Document 参数为key-value的格式 
		 * 2. 创建文档集合List<Document> 
		 * 3. 将文档集合插入数据库集合中 mongoCollection.insertMany(List<Document>) 插入单个文档可以用 mongoCollection.insertOne(Document)
		 */
        Document document = new Document("title", "MongoDB").  
        append("description", "database").  
        append("likes", 100).  
        append("by", "Fly").
        append("url", "http://www.baidu.com");  
        List<Document> documents = new ArrayList<Document>();  
        documents.add(document);  
        collection.insertMany(documents);  
        System.out.println("文档插入成功");
	}
	
	
}
