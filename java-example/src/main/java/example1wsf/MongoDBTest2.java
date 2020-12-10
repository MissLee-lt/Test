package example1wsf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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

/**
 * Hello world!
 *
 */
public class MongoDBTest2 implements StreamRequestHandler, FunctionInitializer {

	@Override
    public void initialize(Context context) throws IOException {
    }

	@Override
    public void handleRequest(
            InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        outputStream.write(new String("MongoDBTest Start!\n").getBytes());


        List<ServerAddress> addr = new ArrayList<>();
		//ServerAddress()两个参数分别为 服务器地址 和 端口
		ServerAddress serverAddress = new ServerAddress("172.16.1.115", 27017);
		addr.add(serverAddress);

		List<MongoCredential> credentials = new ArrayList<>();
		//MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
		MongoCredential mongoCredential = MongoCredential.createScramSha1Credential("mongodbtest_wsf", "test", "mongodbtest_wsf".toCharArray());
		credentials.add(mongoCredential);


		//通过连接认证获取MongoDB连接
		MongoClient mongoClient = new MongoClient(addr, credentials);

		//连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

		//获取集合
		MongoCollection<Document> collection = mongoDatabase.getCollection("user");


		//创建文档
		Document document = new Document("name","zhangsan")
		.append("sex", "man")
		.append("age", 18);

	    //插入一个文档
	    collection.insertOne(document);


	    //要插入的数据
	    List<Document> list = new ArrayList<>();
	    for(int i = 1; i <= 3; i++) {
	        Document documentItem = new Document("name", "zhangsan" + i)
	                .append("sex", "man" + i)
	                .append("age", 18 + i);
	        list.add(documentItem);
	    }

	    //插入多个文档
	    collection.insertMany(list);

	  //查找集合中的所有文档
	    FindIterable findIterable = collection.find();
	    MongoCursor cursor = findIterable.iterator();
	    while (cursor.hasNext()) {
	    	outputStream.write(cursor.next().toString().getBytes());
//	        System.out.println(cursor.next());
	    }
	    outputStream.write(new String("MongoDBTest End!\n").getBytes());

    }



}
