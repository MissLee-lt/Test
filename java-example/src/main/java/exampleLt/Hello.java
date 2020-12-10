package exampleLt;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.ServerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aliyun.fc.runtime.Context;

public class Hello implements HttpRequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
			throws IOException, ServerException {
		// TODO Auto-generated method stub
		String requestPath = (String) request.getAttribute("FC_REQUEST_PATH");//获取请求的路径
		String requestURI = (String) request.getAttribute("FC_REQUEST_URI");//获取请求的URI
		String requestClientIP = (String) request.getAttribute("FC_REQUEST_CLIENT_IP");//获取请求的Client IP地址
		
		response.setStatus(200);
		response.setHeader("header1", "value1");
		response.setHeader("header2", "value2");
		
		String body = String.format("Path: %s\n Uri: %s\n IP: %s\n", requestPath, requestURI, requestClientIP);
		OutputStream out = response.getOutputStream();
		out.write((body).getBytes());
		out.flush();
		out.close();
	}

}
