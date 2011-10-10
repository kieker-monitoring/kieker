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

import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.DefaultSystemTimer;
import kieker.monitoring.timer.ITimeSource;

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
 * and associate it with the current thread id.
 *   
 * Look here how to add it to your server config: http://cwiki.apache.org/CXF20DOC/interceptors.html
 */

/**
 * @author Dennis Kieselhorst, Andre van Hoorn
 */
public class OperationExecutionSOAPRequestInInterceptor extends SoapHeaderInterceptor implements IMonitoringProbe {

	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.getInstance();
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.getInstance();
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();
	protected static final ITimeSource TIMESOURCE = DefaultSystemTimer.getInstance();

	// the CXF logger uses java.util.logging by default, look here how to change it to log4j: http://cwiki.apache.org/CXF20DOC/debugging.html
	private static final Logger LOG = LogUtils.getL7dLogger(OperationExecutionSOAPRequestInInterceptor.class);

	private static final String NULL_SESSION_STR = "NULL";
	private static final String NULL_SESSIONASYNCTRACE_STR = "NULL-ASYNCIN";

	public OperationExecutionSOAPRequestInInterceptor() {
		// nothing to do
	}

	@Override
	public void handleMessage(final Message msg) throws Fault {
		if (msg instanceof SoapMessage) {
			final SoapMessage soapMsg = (SoapMessage) msg;

			/*
			 * Store entry time tin for this trace.
			 * This value will be used by the corresponding invocation of the
			 * ResponseOutProbe.
			 */
			final long tin = OperationExecutionSOAPRequestInInterceptor.TIMESOURCE.getTime();
			boolean isEntryCall = false; // set true below if is entry call

			/* 1.) Extract sessionId from SOAP header */
			Header hdr = soapMsg.getHeader(SOAPHeaderConstants.SESSION_IDENTIFIER_QNAME);
			String sessionId = this.getStringContentFromHeader(hdr); // null if hdr==null
			if (sessionId == null) {
				/* no Kieker session id in header */
				sessionId = OperationExecutionSOAPRequestInInterceptor.NULL_SESSION_STR;
			}

			/* 2.) Extract eoi from SOAP header */
			hdr = soapMsg.getHeader(SOAPHeaderConstants.EOI_IDENTIFIER_QNAME);
			final String eoiStr = this.getStringContentFromHeader(hdr); // null if hdr==null
			int eoi = -1;
			if (eoiStr != null) {
				try {
					eoi = 1 + Integer.parseInt(eoiStr);
				} catch (final NumberFormatException exc) {
					/* invalid eoi! */
					OperationExecutionSOAPRequestInInterceptor.LOG.log(Level.WARNING, exc.getMessage(), exc);
				}
			}

			/* 3.) Extract ess from SOAP header */
			hdr = soapMsg.getHeader(SOAPHeaderConstants.ESS_IDENTIFIER_QNAME);
			final String essStr = this.getStringContentFromHeader(hdr); // null if hdr==null
			int ess = -1;
			if (essStr != null) {
				try {
					ess = Integer.parseInt(essStr);
				} catch (final NumberFormatException exc) {
					/* invalid ess! */
					OperationExecutionSOAPRequestInInterceptor.LOG.log(Level.WARNING, exc.getMessage(), exc);
				}
			}

			/* 4. Extract traceId from SOAP header */
			hdr = soapMsg.getHeader(SOAPHeaderConstants.TRACE_IDENTIFIER_QNAME);
			final String traceIdStr = this.getStringContentFromHeader(hdr); // null if hdr==null
			long traceId = -1;
			if (traceIdStr != null) {
				try {
					traceId = Long.parseLong(traceIdStr);
				} catch (final NumberFormatException exc) {
					/* Invalid trace id! */
					OperationExecutionSOAPRequestInInterceptor.LOG.log(Level.WARNING, exc.getMessage(), exc);
				}
			} else {
				/*
				 * SOAP Header doesn't contain a trace id.
				 * This might be caused by a request which has been sent by
				 * a host not equipped with the RequestOutProbe.
				 * We will now acquire a thread id which is stored (below!!)
				 * in the thread local variable!
				 */
				traceId = OperationExecutionSOAPRequestInInterceptor.CF_REGISTRY.getUniqueTraceId();
				sessionId = OperationExecutionSOAPRequestInInterceptor.NULL_SESSIONASYNCTRACE_STR;
				isEntryCall = true;
				eoi = 0; // EOI of this execution
				ess = 0; // ESS of this execution
			}

			/* Store thread-local values */
			OperationExecutionSOAPRequestInInterceptor.CF_REGISTRY.storeThreadLocalTraceId(traceId);
			OperationExecutionSOAPRequestInInterceptor.CF_REGISTRY.storeThreadLocalEOI(eoi); // this execution has EOI=eoi; next execution will get eoi with
																								// incrementAndRecall
			OperationExecutionSOAPRequestInInterceptor.CF_REGISTRY.storeThreadLocalESS(ess + 1); // this execution has ESS=ess
			OperationExecutionSOAPRequestInInterceptor.SESSION_REGISTRY.storeThreadLocalSessionId(sessionId);
			OperationExecutionSOAPRequestInInterceptor.SOAP_REGISTRY.storeThreadLocalInRequestIsEntryCall(isEntryCall);
			OperationExecutionSOAPRequestInInterceptor.SOAP_REGISTRY.storeThreadLocalInRequestTin(tin);
			OperationExecutionSOAPRequestInInterceptor.SOAP_REGISTRY.storeThreadLocalInRequestEOI(eoi); // eoi for this execution
			OperationExecutionSOAPRequestInInterceptor.SOAP_REGISTRY.storeThreadLocalInRequestESS(ess); // ess for this execution
		}
	}

	private final String getStringContentFromHeader(final Header hdr) {
		if (hdr == null) {
			return null;
		}
		if (hdr.getObject() instanceof Element) {
			final Element e = (Element) hdr.getObject();
			return DOMUtils.getContent(e);
		}
		return null;
	}
}
