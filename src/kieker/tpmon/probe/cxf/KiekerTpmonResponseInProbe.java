package kieker.tpmon.probe.cxf;

import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.tpmon.core.ControlFlowRegistry;
import kieker.tpmon.core.SessionRegistry;
import kieker.tpmon.core.TpmonController;
import kieker.common.record.OperationExecutionRecord;
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
    private static final TpmonController ctrlInst = TpmonController.getInstance();
    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    protected static final SOAPTraceRegistry soapRegistry = SOAPTraceRegistry.getInstance();
    private static final String componentName = KiekerTpmonResponseInProbe.class.getName();
    private static final String opName = "handleMessage(SoapMessage msg)";
    protected static final String vmName = ctrlInst.getVmName();
    
    
    public void handleMessage(Message msg) throws Fault {
        if (msg instanceof SoapMessage) {
            boolean isEntryCall = soapRegistry.recallThreadLocalOutRequestIsEntryCall();
            SoapMessage soapMsg = (SoapMessage) msg;

            /* 1.) Extract sessionId from SOAP header */
            // No need to fetch sessionId from reponse header since it must be
            // the same as before the request.

            /* 2.) Extract eoi from SOAP header */
            Header hdr = soapMsg.getHeader(KiekerTpmonSOAPHeaderConstants.EOI_IDENTIFIER_QNAME);
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
            // No need to fetch ess from reponse header since the stack size
            // is the same as before the request.

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

            /* Recall my thread-local data stored before the SOAP call */
            long myTraceId = cfRegistry.recallThreadLocalTraceId();
            String mySessionId = sessionRegistry.recallThreadLocalSessionId();
            int myEoi = cfRegistry.recallThreadLocalEOI();
            int myEss = cfRegistry.recallThreadLocalESS();
            long myTin = soapRegistry.recallThreadLocalOutRequestTin();
            long myTout = ctrlInst.getTime();
            // TODO:  Remove following plausibility checks if implementation stable
            if (myTraceId != traceId) {
                LOG.log(Level.WARNING, "Inconsistency between traceId before and after SOAP request:\n" +
                        "" + myTraceId + "(before) != " + traceId + "(after)");
            }

            // Log this execution
            OperationExecutionRecord rec = new OperationExecutionRecord(componentName, opName, mySessionId, myTraceId, myTin, myTout, vmName, myEoi, myEss);
            rec.experimentId = ctrlInst.getExperimentId();
            ctrlInst.newMonitoringRecord(rec);

            /* Store received Kieker EOI
             * ESS remains the same as before the call since we didn't increment the variable! */
            cfRegistry.storeThreadLocalEOI(eoi);

            if (isEntryCall){ // clean up iff trace's origin was right before the call!
                unsetKiekerThreadLocalData();
            }
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

    private final void unsetKiekerThreadLocalData() {
        cfRegistry.unsetThreadLocalTraceId();
        sessionRegistry.unsetThreadLocalSessionId();
        cfRegistry.unsetThreadLocalEOI();
        cfRegistry.unsetThreadLocalESS();
        soapRegistry.unsetThreadLocalOutRequestIsEntryCall();
        soapRegistry.unsetThreadLocalOutRequestTin();
    }
}
