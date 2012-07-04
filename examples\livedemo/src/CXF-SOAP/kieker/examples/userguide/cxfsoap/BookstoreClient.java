package kieker.examples.userguide.cxfsoap;

import java.io.IOException;

import kieker.monitoring.probe.cxf.OperationExecutionSOAPRequestInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPRequestOutInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseInInterceptor;
import kieker.monitoring.probe.cxf.OperationExecutionSOAPResponseOutInterceptor;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class BookstoreClient {

	public static void main(String[] args) throws IOException {
		for (int i = 0; i < 5; i++) {
			System.out.println("BookstoreClient: Starting SOAP request: " + i);
			JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
			factory.getInInterceptors().add(new OperationExecutionSOAPRequestInInterceptor());
	    	factory.getInInterceptors().add(new OperationExecutionSOAPResponseInInterceptor());
	    	factory.getOutInterceptors().add(new OperationExecutionSOAPRequestOutInterceptor());
	    	factory.getOutInterceptors().add(new OperationExecutionSOAPResponseOutInterceptor());
			factory.setServiceClass(Bookstore.class);
			factory.setAddress("http://localhost:9091/bookstore");
			Bookstore client = (Bookstore) factory.create();

			Book reply = client.searchBook("any");
			System.out.println("Server found: " + reply);
		}
	}
}
