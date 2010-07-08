package kieker.monitoring.probe.cxf;

import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.monitoring.core.ControlFlowRegistry;
import kieker.monitoring.core.SessionRegistry;
import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.probe.IMonitoringProbe;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
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
 * CXF InInterceptor to get the sessionIdentifier header from an incoming soap message
 * and associate it with the current thread id in TpmonController.
 *   
 * Look here how to add it to your server config: http://cwiki.apache.org/CXF20DOC/interceptors.html
 */

/**
 * @author Dennis Kieselhorst, Andre van Hoorn
 */
public class OperationExecutionSOAPRequestInInterceptor extends SoapHeaderInterceptor implements IMonitoringProbe {
    // the CXF logger uses java.util.logging by default, look here how to change it to log4j: http://cwiki.apache.org/CXF20DOC/debugging.html

    private static final Logger LOG = LogUtils.getL7dLogger(OperationExecutionSOAPRequestInInterceptor.class);

    private static final MonitoringController ctrlInst = MonitoringController.getInstance();
    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    protected static final SOAPTraceRegistry soapRegistry = SOAPTraceRegistry.getInstance();

    private static final String NULL_SESSION_STR = "NULL";
    private static final String NULL_SESSIONASYNCTRACE_STR = "NULL-ASYNCIN";

    
    public void handleMessage(Message msg) throws Fault {
        if (msg instanceof SoapMessage) {
            SoapMessage soapMsg = (SoapMessage) msg;

            /* Store entry time tin for this trace.
               This value will be used by the corresponding invocation of the
               KiekerTpmonResponseOutProbe. */
            long tin = ctrlInst.getTime();
            boolean isEntryCall = false; // set true below if is entry call

            /* 1.) Extract sessionId from SOAP header */
            Header hdr = soapMsg.getHeader(SOAPHeaderConstants.SESSION_IDENTIFIER_QNAME);
            String sessionId = getStringContentFromHeader(hdr); // null if hdr==null
            if (sessionId == null) {
                /* no Kieker session id in header  */
                sessionId = NULL_SESSION_STR;
            }

            /* 2.) Extract eoi from SOAP header */
            hdr = soapMsg.getHeader(SOAPHeaderConstants.EOI_IDENTIFIER_QNAME);
            String eoiStr = getStringContentFromHeader(hdr); // null if hdr==null
            int eoi = -1;
            if (eoiStr != null) {
                try {
                    eoi = 1 + Integer.parseInt(eoiStr);
                } catch (Exception exc) {
                    /* invalid eoi! */
                    LOG.log(Level.WARNING, exc.getMessage(), exc);
                }
            }

            /* 3.) Extract ess from SOAP header */
            hdr = soapMsg.getHeader(SOAPHeaderConstants.ESS_IDENTIFIER_QNAME);
            String essStr = getStringContentFromHeader(hdr); // null if hdr==null
            int ess = -1;
            if (essStr != null) {
                try {
                    ess = Integer.parseInt(essStr);
                } catch (Exception exc) {
                    /* invalid ess! */
                    LOG.log(Level.WARNING, exc.getMessage(), exc);
                }
            }

            /* 4. Extract traceId from SOAP header */
            hdr = soapMsg.getHeader(SOAPHeaderConstants.TRACE_IDENTIFIER_QNAME);
            String traceIdStr = getStringContentFromHeader(hdr); // null if hdr==null
            long traceId = -1;
            if (traceIdStr != null) {
                try {
                    traceId = Long.parseLong(traceIdStr);
                } catch (Exception exc) {
                    /* Invalid trace id! */
                    LOG.log(Level.WARNING, exc.getMessage(), exc);
                }
            } else {
                /* SOAP Header doesn't contain a trace id.
                 * This might be caused by a request which has been sent by
                 * a host not equipped with the KiekerTpmonRequestOutProbe.
                 * We will now acquire a thread id which is stored (below)
                 * in the thread local variable! */
                traceId = cfRegistry.getUniqueTraceId();
                sessionId = NULL_SESSIONASYNCTRACE_STR;
                isEntryCall = true;
                eoi = 0; // EOI of this execution
                ess = 0; // ESS of this execution
            }

            /* Store thread-local values */
            cfRegistry.storeThreadLocalTraceId(traceId);
            cfRegistry.storeThreadLocalEOI(eoi); // this execution has EOI=eoi; next execution will get eoi with incrementAndRecall
            cfRegistry.storeThreadLocalESS(ess+1); // this execution has ESS=ess
            sessionRegistry.storeThreadLocalSessionId(sessionId);
            soapRegistry.storeThreadLocalInRequestIsEntryCall(isEntryCall);
            soapRegistry.storeThreadLocalInRequestTin(tin);
            soapRegistry.storeThreadLocalInRequestEOI(eoi); // eoi for this execution
            soapRegistry.storeThreadLocalInRequestESS(ess); // ess for this execution
        }
    }

    
    private final String getStringContentFromHeader(final Header hdr) {
        if (hdr == null) {
            return null;
        }
        if (hdr.getObject() instanceof Element) {
            Element e = (Element) hdr.getObject();
            return DOMUtils.getContent(e);
        }
        return null;
    }
}
