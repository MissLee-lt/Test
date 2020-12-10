package JPush;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.StreamRequestHandler;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;
import net.sf.json.JSONObject;

public class JPushSingleScheduleTest implements StreamRequestHandler, FunctionInitializer {

	protected static final Logger LOG = LoggerFactory.getLogger(JPushSingleScheduleTest.class);

	private static final String APP_KEY = "ca703164e78e260c9ffd6ad4";
	private static final String MASTER_SECRET = "7cdc08e5a669a608a70b3b44";

	@Override
	public void initialize(Context context) throws IOException {
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		JPushSingleScheduleTest jpushScheduleTest2 = new JPushSingleScheduleTest();

//		String name = "test_single_schedule_example";
//		String time = "2020-11-24 09:33:00";
		
		Map<String,String> inputMap = jpushScheduleTest2.inputToMap(input);

		jpushScheduleTest2.testCreateSingleSchedule(inputMap);
	}

	public void testCreateSingleSchedule(Map<String, String> map) {
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);

		// PushPayload push = PushPayload.alertAll("test single schedule example.");

		// 推送的关键，构造一个payload（notification是推送有通知的消息）
		PushPayload push = PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.all())
				.setNotification(Notification.android(map.get("msg"), map.get("title"), map))
				.setOptions(Options.newBuilder().setApnsProduction(true).setTimeToLive(7200).build())
				.setMessage(Message.content(map.get("msg"))).build();
		
		String name = map.get("name");
		String time = map.get("time");

		try {
			ScheduleResult result = jpushClient.createSingleSchedule(name, time, push);
			LOG.info("schedule result is " + result);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	/**
	 * 把传入的input转换为map
	 * @param input
	 * @return
	 */
	public Map<String, String> inputToMap(InputStream input) {
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
		/**
		 * String转换为json 必须先把原来的StringBuilder先toString了
		 */
		JSONObject json = JSONObject.fromObject(sb.toString());

		Map<String, String> map = new HashedMap();
		map.put("title", json.getString("title"));
		map.put("msg", json.getString("msg"));
		map.put("name", json.getString("name"));
		map.put("time", json.getString("time"));
		return map;
	}

}
