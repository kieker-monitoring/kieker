package kieker.examples.userguide.spring;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

public class TestProbe implements WebRequestInterceptor {
	
	@Override
	public void afterCompletion(WebRequest arg0, Exception arg1)
			throws Exception {
		System.out.println("test probe: after");
		
	}

	@Override
	public void postHandle(WebRequest arg0, ModelMap arg1) throws Exception {
		System.out.println("test probe: post");
		
	}

	@Override
	public void preHandle(WebRequest arg0) throws Exception {
		System.out.println("test probe: pre");
		
	}

}
