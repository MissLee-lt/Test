package mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
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
public class MongoDBJDBC {
	
	public static void main(String[] args) {
		
		try{
		//连接诶到MongoDB服务
		MongoClient mongoClient = new MongoClient("localhost",27017);
		
		//连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("litiantest");
		
		System.out.println("成功连接到MongoDB数据库...");
		
		MongoCollection<Document> collection = mongoDatabase.getCollection("ltcol");
		System.out.println("集合 ltcol 选择成功");
		
		MongoDBJDBC mongojdbc = new MongoDBJDBC();
          
        //删除符合条件的第一个文档
//        collection.deleteOne(Filters.eq("by", "cai niao jiao cheng"));
		
		//删除所有符合条件的文档
//		collection.deleteMany(Filters.eq("description", "database"));
		
//		mongojdbc.add(collection);
		
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
