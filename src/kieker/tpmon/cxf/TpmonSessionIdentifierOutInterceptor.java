package kieker.tpmon.cxf;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.annotations.TpmonInternal;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderOutFilterInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * kieker.tpmon.cxf.TpmonSessionIdentifierOutInterceptor
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 * 
 * CXF OutInterceptor to set the sessionIdentifier header for an outgoing soap message.
 *   
 * Look here how to add it to your client config: http://cwiki.apache.org/CXF20DOC/interceptors.html
 * 
 * Setting the soap header with jaxb or aegis databinding didn't work yet:
 * http://www.nabble.com/Add-%22out-of-band%22-soap-header-using-simple-frontend-td19380093.html
 * 
 * @author Dennis Kieselhorst
 */
public class TpmonSessionIdentifierOutInterceptor extends SoapHeaderOutFilterInterceptor {

	@TpmonInternal()
	public void handleMessage(SoapMessage msg) throws Fault {
		Document d = DOMUtils.createDocument();
                Element e;
                Header hdr;
                /* Add sessionId to header */
		e = d.createElementNS(TpmonSOAPHeaderConstants.NAMESPACE_URI, TpmonSOAPHeaderConstants.SESSION_QUALIFIED_NAME);
		e.setTextContent(TpmonController.getInstance().recallThreadLocalSessionId()); 
		hdr = new Header(TpmonSOAPHeaderConstants.SESSION_IDENTIFIER_QNAME, e);
		msg.getHeaders().add(hdr);
                /* Add traceId to header */
		e = d.createElementNS(TpmonSOAPHeaderConstants.NAMESPACE_URI, TpmonSOAPHeaderConstants.TRACE_QUALIFIED_NAME);
		e.setTextContent(Long.toString(TpmonController.getInstance().recallThreadLocalTraceId())); 
		hdr = new Header(TpmonSOAPHeaderConstants.TRACE_IDENTIFIER_QNAME, e);
		msg.getHeaders().add(hdr);
                /* Add eoi to header */
		e = d.createElementNS(TpmonSOAPHeaderConstants.NAMESPACE_URI, TpmonSOAPHeaderConstants.EOI_QUALIFIED_NAME);
		e.setTextContent(Integer.toString(TpmonController.getInstance().recallThreadLocalEOI()));
		hdr = new Header(TpmonSOAPHeaderConstants.EOI_IDENTIFIER_QNAME, e);
		msg.getHeaders().add(hdr);
                /* Add ess to header */
		e = d.createElementNS(TpmonSOAPHeaderConstants.NAMESPACE_URI, TpmonSOAPHeaderConstants.ESS_QUALIFIED_NAME);
		e.setTextContent(Integer.toString(TpmonController.getInstance().recallThreadLocalESS()));
		hdr = new Header(TpmonSOAPHeaderConstants.ESS_IDENTIFIER_QNAME, e);
		msg.getHeaders().add(hdr);
	}
}
