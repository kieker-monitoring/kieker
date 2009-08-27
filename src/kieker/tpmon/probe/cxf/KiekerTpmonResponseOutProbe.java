package kieker.tpmon.probe.cxf;

import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.core.ControlFlowRegistry;
import kieker.tpmon.core.SessionRegistry;
import kieker.tpmon.probe.IKiekerMonitoringProbe;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderOutFilterInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 */

/**
 * 
 * @author Dennis Kieselhorst, Andre van Hoorn
 */
public class KiekerTpmonResponseOutProbe extends SoapHeaderOutFilterInterceptor implements IKiekerMonitoringProbe {

    private static final Logger LOG = LogUtils.getL7dLogger(KiekerTpmonResponseOutProbe.class);

    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    protected static final SOAPTraceRegistry soapRegistry = SOAPTraceRegistry.getInstance();

    @TpmonInternal()
    public void handleMessage(SoapMessage msg) throws Fault {
        String sessionID = sessionRegistry.recallThreadLocalSessionId();
        long traceId = cfRegistry.recallThreadLocalTraceId();
        long tin = soapRegistry.recallThreadLocalTin();
        int eoi = -1, ess = -1;

        if (traceId == -1) {
            /* Kieker trace Id not registered.
             * Should not happen, since this is a response message! */
            LOG.log(Level.WARNING,
                        "Kieker traceId not registered. " +
                        "Will unset all threadLocal variables");
        } else { 
            /* thread-local traceId exists: eoi, ess, and sessionID should have
             * been registered before */
            eoi = cfRegistry.recallThreadLocalEOI();
            ess = cfRegistry.recallThreadLocalESS();
            sessionID = sessionRegistry.recallThreadLocalSessionId();
        }

        /* The trace is leaving this node, thus we need to clean up. */
        unsetKiekerThreadLocalData();

        /* We don't put Kieker data into response header if request didn't
         * contain Kieker information*/
        if(soapRegistry.recallThreadLocalInRequestIsEntryCall()){
            return;
        }

        Document d = DOMUtils.createDocument();
        Element e;
        Header hdr;
        /* Add sessionId to header */
        e = d.createElementNS(KiekerTpmonSOAPHeaderConstants.NAMESPACE_URI, KiekerTpmonSOAPHeaderConstants.SESSION_QUALIFIED_NAME);
        e.setTextContent(sessionID);
        hdr = new Header(KiekerTpmonSOAPHeaderConstants.SESSION_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
        /* Add traceId to header */
        e = d.createElementNS(KiekerTpmonSOAPHeaderConstants.NAMESPACE_URI, KiekerTpmonSOAPHeaderConstants.TRACE_QUALIFIED_NAME);
        e.setTextContent(Long.toString(traceId));
        hdr = new Header(KiekerTpmonSOAPHeaderConstants.TRACE_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
        /* Add eoi to header */
        e = d.createElementNS(KiekerTpmonSOAPHeaderConstants.NAMESPACE_URI, KiekerTpmonSOAPHeaderConstants.EOI_QUALIFIED_NAME);
        e.setTextContent(Integer.toString(eoi));
        hdr = new Header(KiekerTpmonSOAPHeaderConstants.EOI_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
        /* Add ess to header */
        e = d.createElementNS(KiekerTpmonSOAPHeaderConstants.NAMESPACE_URI, KiekerTpmonSOAPHeaderConstants.ESS_QUALIFIED_NAME);
        e.setTextContent(Integer.toString(ess));
        hdr = new Header(KiekerTpmonSOAPHeaderConstants.ESS_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
    }

    private final void unsetKiekerThreadLocalData() {
        cfRegistry.unsetThreadLocalTraceId();
        cfRegistry.unsetThreadLocalEOI();
        cfRegistry.unsetThreadLocalESS();
        sessionRegistry.unsetThreadLocalSessionId();
        soapRegistry.unsetThreadLocalInRequestIsEntryCall();
        soapRegistry.unsetThreadLocalTin();
    }
}
