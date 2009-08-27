package kieker.tpmon.probe.cxf;

import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.core.ControlFlowRegistry;
import kieker.tpmon.core.SessionRegistry;
import kieker.tpmon.probe.IKiekerMonitoringProbe;
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
 * @author Andre van Hoorn, Dennis Kieselhorst
 */
public class KiekerTpmonResponseInProbe extends SoapHeaderInterceptor implements IKiekerMonitoringProbe {
    // the CXF logger uses java.util.logging by default, look here how to change it to log4j: http://cwiki.apache.org/CXF20DOC/debugging.html

    private static final Logger LOG = LogUtils.getL7dLogger(KiekerTpmonResponseInProbe.class);
    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();

    @TpmonInternal()
    public void handleMessage(Message msg) throws Fault {
        if (msg instanceof SoapMessage) {
            SoapMessage soapMsg = (SoapMessage) msg;

            /* 1.) Extract sessionId from SOAP header */
            Header hdr = soapMsg.getHeader(KiekerTpmonSOAPHeaderConstants.SESSION_IDENTIFIER_QNAME);
            String sessionId = getStringContentFromHeader(hdr); // null if hdr==null
            if (sessionId == null) {
                /* No Kieker session id in header.
                 * This may happen for responses from callees w/o Kieker instrumentation. */
                LOG.log(Level.FINE,
                        "Found no Kieker sessionID in response header. " +
                        "Will unset all threadLocal variables");
                unsetKiekerThreadLocalData();
                return;
            }

            /* 2.) Extract eoi from SOAP header */
            hdr = soapMsg.getHeader(KiekerTpmonSOAPHeaderConstants.EOI_IDENTIFIER_QNAME);
            String eoiStr = getStringContentFromHeader(hdr); // null if hdr==null
            if (eoiStr == null) {
                /* No Kieker eoi in header.
                 * This may happen for responses from callees w/o Kieker instrumentation. */
                LOG.log(Level.FINE,
                        "Found no Kieker eoi in response header. " +
                        "Will unset all threadLocal variables");
                unsetKiekerThreadLocalData();
                return;
            }
            int eoi = 0;
            try {
                eoi = Integer.parseInt(eoiStr);
            } catch (Exception exc) {
                /* invalid eoi! */
                LOG.log(Level.WARNING, exc.getMessage(), exc);
                unsetKiekerThreadLocalData();
                return;
            }

            /* 3.) Extract ess from SOAP header */
            hdr = soapMsg.getHeader(KiekerTpmonSOAPHeaderConstants.ESS_IDENTIFIER_QNAME);
            String essStr = getStringContentFromHeader(hdr); // null if hdr==null
            if (essStr == null) {
                /* No Kieker ess in header.
                 * This may happen for responses from callees w/o Kieker instrumentation. */
                LOG.log(Level.FINE,
                        "Found no Kieker ess in response header. " +
                        "Will unset all threadLocal variables");
                unsetKiekerThreadLocalData();
                return;
            }
            int ess = 1;
            try {
                ess = Integer.parseInt(essStr);
            } catch (Exception exc) {
                /* invalid ess! */
                LOG.log(Level.WARNING, exc.getMessage(), exc);
                unsetKiekerThreadLocalData();
                return;
            }

            /* 4. Extract traceId from SOAP header */
            hdr = soapMsg.getHeader(KiekerTpmonSOAPHeaderConstants.TRACE_IDENTIFIER_QNAME);
            String traceIdStr = getStringContentFromHeader(hdr); // null if hdr==null
            if (traceIdStr == null) {
                /* No Kieker trace Id in header.
                 * This may happen for responses from callees w/o Kieker instrumentation. */
                LOG.log(Level.FINE,
                        "Found no Kieker traceId in response header. " +
                        "Will unset all threadLocal variables");
                unsetKiekerThreadLocalData();
                return;
            }
            long traceId = -1;
            try {
                traceId = Long.parseLong(traceIdStr);
            } catch (Exception exc) {
                /* Invalid trace id! */
                LOG.log(Level.WARNING, exc.getMessage(), exc);
                unsetKiekerThreadLocalData();
                return;
            }

            /* Store received Kieker trace data */
            cfRegistry.storeThreadLocalTraceId(traceId);
            sessionRegistry.storeThreadLocalSessionId(sessionId);
            cfRegistry.storeThreadLocalEOI(eoi);
            cfRegistry.storeThreadLocalESS(ess);
        }
    }

    @TpmonInternal()
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

    private final void unsetKiekerThreadLocalData() {
        cfRegistry.unsetThreadLocalTraceId();
        sessionRegistry.unsetThreadLocalSessionId();
        cfRegistry.unsetThreadLocalEOI();
        cfRegistry.unsetThreadLocalESS();
    }
}
