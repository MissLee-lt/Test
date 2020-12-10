package example1wsf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.StreamRequestHandler;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;

/**
 * Hello world!
 *
 */
public class PushTest implements StreamRequestHandler, FunctionInitializer {

//	private static Logger logger = LogManager.getLogger(PushTest.class);

	protected static final String REGISTRATION_ID = "1104a8979210aa00819";
	protected static final String APP_KEY = "ca703164e78e260c9ffd6ad4";
    protected static final String MASTER_SECRET = "7cdc08e5a669a608a70b3b44";

	@Override
    public void initialize(Context context) throws IOException {
    }

	@Override
    public void handleRequest(
            InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

		System.out.println("PushTest start!");
		
        JPushClient client = new JPushClient(MASTER_SECRET, APP_KEY);
        PushResult response = null;
        try {

			response = client.sendPush("{platform:\"android\",audience:{registration_id:[\"1104a8979210aa00819\"]},notification:{alert : \"Hello, JPush!\"}}");

            outputStream.write(response.toString().getBytes());

        } catch (APIConnectionException e) {

        	System.out.println("PushTest APIConnectionException!");
			e.printStackTrace();
		} catch (APIRequestException e) {
			System.out.println("PushTest APIRequestException!");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("PushTest Exception!" + e.getClass());
			e.printStackTrace();
		} finally {

            if (client != null) {
                client.close();
            }
        }
        System.out.println("PushTest end!");

    }

}
