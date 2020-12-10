package JPush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.StreamRequestHandler;

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
import net.sf.json.JSONObject;

public class JPushUtil2 implements StreamRequestHandler, FunctionInitializer{

	// 设置好账号的app_key和master_secret是必须的
	private static String APP_KEY = "ca703164e78e260c9ffd6ad4";
	private static String MASTER_SECRET = "7cdc08e5a669a608a70b3b44";
	
	private static String REGISTRATION_ID = "1104a8979210aa00819";

	/**
	 * 极光推送>>Android Map<String, String> parm 是我自己传过来的参数，可以自定义参数
	 * 
	 * @param parm
	 */
	public static PushResult jpushAndroid(Map<String, String> parm) {

		// 创建JPushClient(极光推送的实例)
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
		// 推送的关键，构造一个payload（notification是推送有通知的消息）
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pu;
	}


	@Override
	public void initialize(Context context) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// TODO Auto-generated method stub
		output.write(new String("...start...\n").getBytes());
		
		/**
		 * 如下代码为把inputStream转换为StringBuilder类型
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));   
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
            	input.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }  
        }
        output.write(new String(sb).getBytes());
		
        /**
         * String转换为json
         * 必须先把原来的StringBuilder先toString了
         */
        JSONObject json = JSONObject.fromObject(sb.toString());
		
		Map<String, String> map = new HashedMap();
		map.put("msg", json.getString("msg"));
		map.put("title", json.getString("title"));
		
		PushResult pu = JPushUtil2.jpushAndroid(map);
		
		output.write(pu.toString().getBytes());
		
		output.write(new String("\n...end...\n").getBytes());
		
	}

}
