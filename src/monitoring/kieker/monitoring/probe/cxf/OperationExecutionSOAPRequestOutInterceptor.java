/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.monitoring.probe.cxf;

import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.DefaultSystemTimer;
import kieker.monitoring.timer.ITimeSource;

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
 */
/**
 *
 * @author Dennis Kieselhorst, Andre van Hoorn
 */
public class OperationExecutionSOAPRequestOutInterceptor extends SoapHeaderOutFilterInterceptor implements IMonitoringProbe {

    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    protected static final SOAPTraceRegistry soapRegistry = SOAPTraceRegistry.getInstance();
    protected static final ITimeSource timesource = DefaultSystemTimer.getInstance();
    
    private static final String NULL_SESSION_STR = "NULL";
    private static final String NULL_SESSIONASYNCTRACE_STR = "NULL-ASYNCOUT";

    
    @Override
	public void handleMessage(final SoapMessage msg) throws Fault {
        String sessionID = null;
        long traceId = OperationExecutionSOAPRequestOutInterceptor.cfRegistry.recallThreadLocalTraceId();
        int eoi, ess;

        /* Store entry time tin for this trace.
        This value will be used by the corresponding invocation of the
        ResponseOutProbe. */
        final long tin = timesource.getTime();
        boolean isEntryCall = false; // set true below if is entry call

        if (traceId == -1) {
            /* traceId has not been registered before.
             * This might be caused by a thread which has been spawned
             * asynchronously. We will now acquire a thread id and store it
             * in the thread local variable. */
            traceId = OperationExecutionSOAPRequestOutInterceptor.cfRegistry.getAndStoreUniqueThreadLocalTraceId();
            eoi = 0; // eoi of this execution
            OperationExecutionSOAPRequestOutInterceptor.cfRegistry.storeThreadLocalEOI(eoi);
            ess = 0; // ess of this execution
            OperationExecutionSOAPRequestOutInterceptor.cfRegistry.storeThreadLocalESS(ess);
            isEntryCall = true;
            sessionID = OperationExecutionSOAPRequestOutInterceptor.NULL_SESSIONASYNCTRACE_STR;
            OperationExecutionSOAPRequestOutInterceptor.sessionRegistry.storeThreadLocalSessionId(sessionID);
        } else {
            /* thread-local traceId exists: eoi and ess should have been registered before */
            eoi = OperationExecutionSOAPRequestOutInterceptor.cfRegistry.incrementAndRecallThreadLocalEOI();
            ess = OperationExecutionSOAPRequestOutInterceptor.cfRegistry.recallThreadLocalESS(); // do not increment in this case!
            sessionID = OperationExecutionSOAPRequestOutInterceptor.sessionRegistry.recallThreadLocalSessionId();
            if (sessionID == null) {
                sessionID = OperationExecutionSOAPRequestOutInterceptor.NULL_SESSION_STR;
            }
        }

        OperationExecutionSOAPRequestOutInterceptor.soapRegistry.storeThreadLocalOutRequestIsEntryCall(isEntryCall);
        OperationExecutionSOAPRequestOutInterceptor.soapRegistry.storeThreadLocalOutRequestTin(tin);

        final Document d = DOMUtils.createDocument();
        Element e;
        Header hdr;
        /* Add sessionId to header */
        e = d.createElementNS(SOAPHeaderConstants.NAMESPACE_URI, SOAPHeaderConstants.SESSION_QUALIFIED_NAME);
        e.setTextContent(sessionID);
        hdr = new Header(SOAPHeaderConstants.SESSION_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
        /* Add traceId to header */
        e = d.createElementNS(SOAPHeaderConstants.NAMESPACE_URI, SOAPHeaderConstants.TRACE_QUALIFIED_NAME);
        e.setTextContent(Long.toString(traceId));
        hdr = new Header(SOAPHeaderConstants.TRACE_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
        /* Add eoi to header */
        e = d.createElementNS(SOAPHeaderConstants.NAMESPACE_URI, SOAPHeaderConstants.EOI_QUALIFIED_NAME);
        e.setTextContent(Integer.toString(eoi));
        hdr = new Header(SOAPHeaderConstants.EOI_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
        /* Add ess to header */
        e = d.createElementNS(SOAPHeaderConstants.NAMESPACE_URI, SOAPHeaderConstants.ESS_QUALIFIED_NAME);
        e.setTextContent(Integer.toString(ess + 1));
        hdr = new Header(SOAPHeaderConstants.ESS_IDENTIFIER_QNAME, e);
        msg.getHeaders().add(hdr);
    }
}
