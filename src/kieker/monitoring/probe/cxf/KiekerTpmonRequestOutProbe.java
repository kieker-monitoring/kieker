package kieker.monitoring.probe.cxf;


import kieker.monitoring.core.ControlFlowRegistry;
import kieker.monitoring.core.SessionRegistry;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.probe.IMonitoringProbe;
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
public class KiekerTpmonRequestOutProbe extends SoapHeaderOutFilterInterceptor implements IMonitoringProbe {

    private static final MonitoringController ctrlInst = MonitoringController.getInstance();
    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    protected static final SOAPTraceRegistry soapRegistry = SOAPTraceRegistry.getInstance();
    private static final String NULL_SESSION_STR = "NULL";
    private static final String NULL_SESSIONASYNCTRACE_STR = "NULL-ASYNCOUT";

    
    public void handleMessage(SoapMessage msg) throws Fault {
        String sessionID = null;
        long traceId = cfRegistry.recallThreadLocalTraceId();
        int eoi, ess;

        /* Store entry time tin for this trace.
        This value will be used by the corresponding invocation of the
        KiekerTpmonResponseOutProbe. */
        long tin = ctrlInst.getTime();
        boolean isEntryCall = false; // set true below if is entry call

        if (traceId == -1) {
            /* traceId has not been registered before.
             * This might be caused by a thread which has been spawned
             * asynchronously. We will now acquire a thread id and store it
             * in the thread local variable. */
            traceId = cfRegistry.getAndStoreUniqueThreadLocalTraceId();
            eoi = 0; // eoi of this execution
            cfRegistry.storeThreadLocalEOI(eoi);
            ess = 0; // ess of this execution
            cfRegistry.storeThreadLocalESS(ess);
            isEntryCall = true;
            sessionID = NULL_SESSIONASYNCTRACE_STR;
            sessionRegistry.storeThreadLocalSessionId(sessionID);
        } else {
            /* thread-local traceId exists: eoi and ess should have been registered before */
            eoi = cfRegistry.incrementAndRecallThreadLocalEOI();
            ess = cfRegistry.recallThreadLocalESS(); // do not increment in this case!
            sessionID = sessionRegistry.recallThreadLocalSessionId();
            if (sessionID == null) {
                sessionID = NULL_SESSION_STR;
            }
        }

        soapRegistry.storeThreadLocalOutRequestIsEntryCall(isEntryCall);
        soapRegistry.storeThreadLocalOutRequestTin(tin);

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
        e.setTextContent(Integer.toString(ess + 1));
        hdr = new Header(KiekerTpmonSOAPHeaderConstants.ESS_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
    }
}
