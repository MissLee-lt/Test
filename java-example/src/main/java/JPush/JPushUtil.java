package JPush;

import java.io.IOException;
import java.io.InputStream;
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
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushUtil implements StreamRequestHandler, FunctionInitializer{

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pu;
	}

	/**
	 * 极光推送>>All所有平台
	 * 
	 * @param param
	 */
	/*public static void jpushAll(Map<String, String> param) {

		// 创建JPushClient
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);

		// 创建option
		PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
				.setAudience(Audience.registrationId(param.get("id")))
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(IosNotification.newBuilder().setAlert(param.get("msg")).setBadge(+1)
								.setSound("happy").addExtras(param).build())
						.build())
				.setOptions(Options.newBuilder().setApnsProduction(true).build())
				.setMessage(Message.newBuilder().setMsgContent(param.get("msg")).addExtras(param).build()).build();
		
		try {
			PushResult pu = jpushClient.sendPush(payload);
			System.out.println(pu.toString());
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/

	@Override
	public void initialize(Context context) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		// TODO Auto-generated method stub
		output.write(new String("...start...\n").getBytes());
		
		Map<String, String> map = new HashedMap();
//		map.put("id", REGISTRATION_ID);
		map.put("title", "TestTitle");
		map.put("msg", "test message...");
//		map.put("alias", "abc");
		PushResult pu = JPushUtil.jpushAndroid(map);
		
		output.write(pu.toString().getBytes());
		
		output.write(new String("\n...end...\n").getBytes());
		
	}

}
