package kieker.tpmon.cxf;

import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.tpmon.TpmonController;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.w3c.dom.Element;

/**
 * CXF InInterceptor to get the sessionIdentifier header from an incoming soap message
 * and associate it with the current thread id in TpmonController.
 *   
 * Look here how to add it to your server config: http://cwiki.apache.org/CXF20DOC/interceptors.html
 * 
 * @author Dennis Kieselhorst
 *
 */
public class TpmonSessionIdentifierInInterceptor extends SoapHeaderInterceptor {
	// the CXF logger uses java.util.logging by default, look here how to change it to log4j: http://cwiki.apache.org/CXF20DOC/debugging.html 
	private static final Logger LOG = LogUtils.getL7dLogger(TpmonSessionIdentifierInInterceptor.class);
			
	@Override
	public void handleMessage(Message msg) throws Fault {
		if (msg instanceof SoapMessage) {
			SoapMessage soapMsg = (SoapMessage) msg;
			if(LOG.isLoggable(Level.FINE)) {
				for(Header hdr : soapMsg.getHeaders()) {
					LOG.fine("found header: "+hdr.getName()+" "+hdr.getObject()+", string content="+getStringContentFromHeader(hdr));
					LOG.finer("type "+hdr.getObject().getClass());
				}
			}
			Header hdr = soapMsg.getHeader(SessionIdentifierConstants.SESSION_IDENTIFIER_QNAME);
			if (hdr!=null) {
				String sessionId = getStringContentFromHeader(hdr);
				if(sessionId!=null) {
					LOG.info("registering session identifier "+sessionId);
					TpmonController.getInstance().storeThreadLocalSessionId(sessionId);
				}
			} else {
				LOG.info("no tpmon session identifier header found!");
			}
		}
	}

	private String getStringContentFromHeader(Header hdr) {
		if (hdr.getObject() instanceof Element) {
			Element e = (Element) hdr.getObject();
			return DOMUtils.getContent(e);	
		}
		return null;
	}

}
