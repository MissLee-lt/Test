package example1wsf;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;

/**
 * Hello world!
 *
 */
public class PushTest2{


	protected static final String REGISTRATION_ID = "1104a8979210aa00819";
	protected static final String APP_KEY = "ca703164e78e260c9ffd6ad4";
    protected static final String MASTER_SECRET = "7cdc08e5a669a608a70b3b44";

    public static void main(String[] args) {
    	new PushTest2().push();
    }

    public void push(){

		System.out.println("PushTest start!");

//		HttpProxy proxy = new HttpProxy("http://10.167.23.45/beijing.pac", 8080);
//		HttpProxy proxy = new HttpProxy("10.167.23.45", 8080);

//        JPushClient client = new JPushClient(MASTER_SECRET, APP_KEY, proxy, ClientConfig.getInstance());
        JPushClient client = new JPushClient(MASTER_SECRET, APP_KEY);




        PushResult response = null;
        try {

			response = client.sendPush("{platform:\"android\",audience:{registration_id:[\"1104a8979210aa00819\"]},notification:{alert : \"Hello, JPush!\"}}");

			System.out.println(response.toString());

        } catch (APIConnectionException | APIRequestException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} finally {

            if (client != null) {
                client.close();
            }
        }
        System.out.println("PushTest end!");

    }

}
