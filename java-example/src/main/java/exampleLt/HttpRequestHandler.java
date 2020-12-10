package exampleLt;

import java.io.IOException;
import java.rmi.ServerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aliyun.fc.runtime.Context;

public interface HttpRequestHandler {

	public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
			throws IOException, ServerException;

}
