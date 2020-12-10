package mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.bson.Document;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import JPush.JPushUtil2;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class MongoDBFC2 implements StreamRequestHandler, FunctionInitializer {
	
	// 设置好账号的app_key和master_secret是必须的
	private static String APP_KEY = "ca703164e78e260c9ffd6ad4";
	private static String MASTER_SECRET = "7cdc08e5a669a608a70b3b44";
	
	private static String HOST = "dds-uf65ddb19a1906441.mongodb.rds.aliyuncs.com";
	private static int PORT = 3717;
	private static String USER_NAME = "root";
	private static String SOURCE = "admin";
	private static String PASSWORD = "Wsf!2020";
	

	@Override
	public void initialize(Context context) throws IOException {
		
	}

	/**
	 * 函数计算入口函数
	 */
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		try{
			
			output.write(new String("...start...\n").getBytes());
			
			//连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址  
            //ServerAddress()两个参数分别为 服务器地址 和 端口  
            ServerAddress serverAddress = new ServerAddress(HOST,PORT);  
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();  
            addrs.add(serverAddress);  
              
            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码  
            MongoCredential credential = MongoCredential.createScramSha1Credential(USER_NAME, SOURCE, PASSWORD.toCharArray());  
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
            credentials.add(credential);  
              
            //通过连接认证获取MongoDB连接  
            MongoClient mongoClient = new MongoClient(addrs,credentials);  
              
            //连接到数据库  
            MongoDatabase mongoDatabase = mongoClient.getDatabase(SOURCE);  
            System.out.println("成功连接到MongoDB数据库...");  
            
            MongoCollection<Document> collection = mongoDatabase.getCollection("ltcol");
			System.out.println("集合 ltcol 选择成功");
			
			MongoDBFC2 mongodbfc = new MongoDBFC2();
			
//			mongodbfc.add(collection);
//			System.out.println("给集合添加数据成功。。。");
			
			List<String> list = new ArrayList<String>();
			
			list = mongodbfc.query(collection);
			System.out.println(list.toString());
			output.write(list.toString().getBytes());
			
			Map<String, String> map = new HashedMap();
			map.put("msg",list.toString());
			map.put("title", "MongoDB数据");
			PushResult pu = JPushUtil2.jpushAndroid(map);
			
			output.write(new String("\n").getBytes());
			
			output.write(pu.toString().getBytes());
			
			output.write(new String("\n...end...\n").getBytes());
			
			}catch(Exception e){
				System.err.println(e.getClass().getName() + ": " + e.getMessage());
			}
		
	}
	
	/**
	 * 极光推送>>Android Map<String, String> parm 是我自己传过来的参数，可以自定义参数
	 * 
	 * @param parm
	 */
	public static PushResult jpushAndroid(Map<String, String> parm) {

		// 创建JPushClient(极光推送的实例)
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		// 推送的关键，构造一个payload
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.all())
				.setNotification(Notification.android(parm.get("msg"), parm.get("title"), parm))
				.setOptions(Options.newBuilder().setApnsProduction(true).setTimeToLive(7200).build())
				.setMessage(Message.content(parm.get("msg"))).build();

		PushResult pu = null;
		try {
			pu = jpushClient.sendPush(payload);
			System.out.println(pu.toString());
			return pu;
		} catch (APIConnectionException e) {
			e.printStackTrace();
		} catch (APIRequestException e) {
			e.printStackTrace();
		}
		return pu;
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
			System.out.println(mongoCursor.next());
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
