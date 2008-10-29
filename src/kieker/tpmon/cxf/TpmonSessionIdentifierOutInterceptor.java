package kieker.tpmon.cxf;


import kieker.tpmon.TpmonController;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderOutFilterInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * CXF OutInterceptor to set the sessionIdentifier header for an outgoing soap message.
 *   
 * Look here how to add it to your client config: http://cwiki.apache.org/CXF20DOC/interceptors.html 
 * 
 * Setting the soap header with jaxb or aegis databinding didn't work yet:
 * http://www.nabble.com/Add-%22out-of-band%22-soap-header-using-simple-frontend-td19380093.html
 * 
 * @author Dennis Kieselhorst
 *
 */
public class TpmonSessionIdentifierOutInterceptor extends
		SoapHeaderOutFilterInterceptor {

	@Override
	public void handleMessage(SoapMessage msg) throws Fault {
		Document d = DOMUtils.createDocument();
		Element e = d.createElementNS(SessionIdentifierConstants.NAMESPACE_URI, SessionIdentifierConstants.QUALIFIED_NAME);
		e.setTextContent(TpmonController.getInstance().recallThreadLocalSessionId()); 
		Header hdr = new Header(SessionIdentifierConstants.SESSION_IDENTIFIER_QNAME, e);
		msg.getHeaders().add(hdr);
	}

}
