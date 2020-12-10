package exampleLt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.StreamRequestHandler;

import net.sf.json.JSONObject;


public class App implements StreamRequestHandler, FunctionInitializer {

	@Override
	public void initialize(Context context) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		// TODO Auto-generated method stub
		outputStream.write(new String("start...打印输入的值...\n").getBytes());//输出
		context.getLogger().info("start...");//打印日志
		context.getLogger().warn("测试打印warn日志");
		context.getLogger().error("测试打印error日志");
		
		/**
		 * 如下代码为把inputStream转换为StringBuilder类型
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));   
        StringBuilder sb = new StringBuilder();   
        String line = null;   
        try {   
            while ((line = reader.readLine()) != null) {   
                sb.append(line + "\n");   
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            try {   
            	inputStream.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }  
        }
        
        outputStream.write(new String(sb).getBytes());
		
        /**
         * String转换为json，必须先把原来的StringBuilder先toString了
         */
        JSONObject json = JSONObject.fromObject(sb.toString());

        outputStream.write(new String(json.getString("key") + "\n").getBytes());
		
        context.getLogger().info("end...");
        outputStream.write(new String("\nend...打印输入的值...\n").getBytes());
	}
}
