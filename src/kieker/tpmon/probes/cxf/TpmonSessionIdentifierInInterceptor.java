package kieker.tpmon.probes.cxf;

import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.annotations.TpmonInternal;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.w3c.dom.Element;

/**
 * kieker.tpmon.cxf.TpmonSessionIdentifierInInterceptor
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
 * CXF InInterceptor to get the sessionIdentifier header from an incoming soap message
 * and associate it with the current thread id in TpmonController.
 *   
 * Look here how to add it to your server config: http://cwiki.apache.org/CXF20DOC/interceptors.html
 * 
 * @author Dennis Kieselhorst
 */
public class TpmonSessionIdentifierInInterceptor extends SoapHeaderInterceptor {
    // the CXF logger uses java.util.logging by default, look here how to change it to log4j: http://cwiki.apache.org/CXF20DOC/debugging.html
    private static final Logger LOG = LogUtils.getL7dLogger(TpmonSessionIdentifierInInterceptor.class);

    @TpmonInternal()
    public void handleMessage(Message msg) throws Fault {
        if (msg instanceof SoapMessage) {
            SoapMessage soapMsg = (SoapMessage) msg;
            if (LOG.isLoggable(Level.FINE)) {
                for (Header hdr : soapMsg.getHeaders()) {
                    LOG.fine("found header: " + hdr.getName() + " " + hdr.getObject() + ", string content=" + getStringContentFromHeader(hdr));
                    LOG.finer("type " + hdr.getObject().getClass());
                }
            }
            /* Extract and register sessionId from SOAP header */
            Header hdr = soapMsg.getHeader(TpmonSOAPHeaderConstants.SESSION_IDENTIFIER_QNAME);
            if (hdr != null) {
                String sessionId = getStringContentFromHeader(hdr);
                if (sessionId != null) {
                    LOG.info("registering session identifier " + sessionId);
                    TpmonController.getInstance().storeThreadLocalSessionId(sessionId);
                }
            } else {
                LOG.info("no tpmon session identifier header found!");
            }
            /* Extract and register traceId from SOAP header */
            hdr = soapMsg.getHeader(TpmonSOAPHeaderConstants.TRACE_IDENTIFIER_QNAME);
            if (hdr != null) {
                String traceIdStr = getStringContentFromHeader(hdr);
                if (traceIdStr != null) {
                    try {
                        long traceId = Long.parseLong(traceIdStr);
                        LOG.info("registering trace identifier " + traceId);
                        TpmonController.getInstance().storeThreadLocalTraceId(traceId);
                    } catch (Exception exc) {
                        LOG.log(Level.WARNING, exc.getMessage(), exc);
                    }
                }
            } else {
                LOG.info("no tpmon trace identifier header found!");
            }
            /* Extract and register eoi from SOAP header */
            hdr = soapMsg.getHeader(TpmonSOAPHeaderConstants.EOI_IDENTIFIER_QNAME);
            if (hdr != null) {
                String eoiStr = getStringContentFromHeader(hdr);
                if (eoiStr != null) {
                    try {
                        int eoi = Integer.parseInt(eoiStr);
                        LOG.info("registering eoi " + eoi);
                        TpmonController.getInstance().storeThreadLocalEOI(eoi);
                    } catch (Exception exc) {
                        LOG.log(Level.WARNING, exc.getMessage(), exc);
                    }
                }
            } else {
                LOG.info("no tpmon eoi header found!");
            }
           /* Extract and register ess from SOAP header */
            hdr = soapMsg.getHeader(TpmonSOAPHeaderConstants.ESS_IDENTIFIER_QNAME);
            if (hdr != null) {
                String essStr = getStringContentFromHeader(hdr);
                if (essStr != null) {
                    try {
                        int ess = Integer.parseInt(essStr);
                        LOG.info("registering ess " + ess);
                        TpmonController.getInstance().storeThreadLocalESS(ess);
                    } catch (Exception exc) {
                        LOG.log(Level.WARNING, exc.getMessage(), exc);
                    }
                }
            } else {
                LOG.info("no tpmon ess header found!");
            }
        }
    }

    @TpmonInternal()
    private String getStringContentFromHeader(Header hdr) {
        if (hdr.getObject() instanceof Element) {
            Element e = (Element) hdr.getObject();
            return DOMUtils.getContent(e);
        }
        return null;
    }
}
