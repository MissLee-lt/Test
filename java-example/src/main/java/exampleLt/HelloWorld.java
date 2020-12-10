package exampleLt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.StreamRequestHandler;

/**
 * Hello world!
 *
 */
public class HelloWorld implements StreamRequestHandler, FunctionInitializer {

	@Override
    public void initialize(Context context) throws IOException {
    }

	@Override
    public void handleRequest(
            InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
	
        outputStream.write(new String("hello world\n").getBytes());
        
        /**
         * 打印inputStream中的内容
         */
        byte[] input = new byte[1024];
        while(inputStream.read(input) != -1) {
        	outputStream.write(input);
        }
        outputStream.write(new String("\nend\n").getBytes());

    }
}
