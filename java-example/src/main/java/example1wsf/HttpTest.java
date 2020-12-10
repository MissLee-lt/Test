package example1wsf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class HttpTest implements StreamRequestHandler, FunctionInitializer {

	@Override
    public void initialize(Context context) throws IOException {
    }

	@Override
    public void handleRequest(
            InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        outputStream.write(new String("hello world\n").getBytes());

        try {
            CloseableHttpClient client = null;
            CloseableHttpResponse response = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("code", "001");
                data.put("name", "test");

                HttpPost httpPost = new HttpPost("https://1925591790271515.cn-shanghai.fc.aliyuncs.com/2016-08-15/proxy/aliyun-test/helloworld002/");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");
                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(data),
                        ContentType.create("text/json", "UTF-8")));

                client = HttpClients.createDefault();
                response = client.execute(httpPost);
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                outputStream.write(result.getBytes());

            } finally {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
