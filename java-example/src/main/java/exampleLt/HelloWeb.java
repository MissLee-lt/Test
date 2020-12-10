package exampleLt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.FcAppLoader;
import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aliyun.fc.runtime.FunctionInitializer;
import com.aliyun.fc.runtime.HttpRequestHandler;

public class HelloWeb implements FunctionInitializer, HttpRequestHandler {
	
	private FcAppLoader fcAppLoader = new FcAppLoader();
	
	private String ossEndPoint = "YourOSSEndPoint";
	private String bucket = "YourOSSBucket";
	private String key = "YourWarName";
	private String userContextPath = "/2016-08-15/proxy/{YourServiceName}/{YourFunctionName}";

	@Override
	public void initialize(Context context) throws IOException {
		// TODO Auto-generated method stub
		FunctionComputeLogger fcLogger = context.getLogger();
		fcAppLoader.setFCContext(context);
		
		//Load code from OSS
		fcAppLoader.loadCodeFromOSS(ossEndPoint, bucket, key);
		
		//Init webapp from code
		fcAppLoader.initApp(userContextPath, HelloWeb.class.getClassLoader());

	}
	
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		try {
			fcAppLoader.forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
