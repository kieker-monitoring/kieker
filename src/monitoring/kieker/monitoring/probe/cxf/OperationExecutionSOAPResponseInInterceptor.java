/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.w3c.dom.Element;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * CXF InInterceptor to get the sessionIdentifier header from an incoming soap message
 * and associate it with the current thread id.
 * 
 * Look here how to add it to your server config: http://cwiki.apache.org/CXF20DOC/interceptors.html
 */
/**
 * @author Andre van Hoorn, Dennis Kieselhorst
 */
public class OperationExecutionSOAPResponseInInterceptor extends SoapHeaderInterceptor implements IMonitoringProbe {
	// the CXF logger uses java.util.logging by default, look here how to change it to log4j: http://cwiki.apache.org/CXF20DOC/debugging.html

	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();

	protected final IMonitoringController monitoringController;
	protected final ITimeSource timeSource;
	protected final String vmName;

	private static final Logger LOG = LogUtils.getL7dLogger(OperationExecutionSOAPResponseInInterceptor.class);

	public static final String SIGNATURE = "public void " + OperationExecutionSOAPResponseInInterceptor.class.getName()
			+ ".handleMessage(org.apache.cxf.message.Message)";

	public OperationExecutionSOAPResponseInInterceptor() {
		this(MonitoringController.getInstance());
	}

	public OperationExecutionSOAPResponseInInterceptor(final IMonitoringController monitoringCtrl) {
		this.monitoringController = monitoringCtrl;
		this.timeSource = this.monitoringController.getTimeSource();
		this.vmName = this.monitoringController.getHostname();
	}

	@Override
	public void handleMessage(final Message msg) throws Fault {
		if (!this.monitoringController.isMonitoringEnabled()) {
			return;
		}
		if (msg instanceof SoapMessage) {
			final boolean isEntryCall = SOAP_REGISTRY.recallThreadLocalOutRequestIsEntryCall(); // NOPMD (must be requerst here!)
			final SoapMessage soapMsg = (SoapMessage) msg;

			/* 1.) Extract sessionId from SOAP header */
			// No need to fetch sessionId from reponse header since it must be
			// the same as before the request.

			/* 2.) Extract eoi from SOAP header */
			Header hdr = soapMsg.getHeader(SOAPHeaderConstants.EOI_IDENTIFIER_QNAME);
			final String eoiStr = this.getStringContentFromHeader(hdr); // null if hdr==null
			if (eoiStr == null) {
				/*
				 * No Kieker eoi in header.
				 * This may happen for responses from callees w/o Kieker instrumentation.
				 */
				LOG.log(Level.FINE, "Found no Kieker eoi in response header. "
						+ "Will unset all threadLocal variables");
				this.unsetKiekerThreadLocalData();
				return;
			}
			int eoi = 0;
			try {
				eoi = Integer.parseInt(eoiStr);
			} catch (final NumberFormatException exc) {
				/* invalid eoi! */
				LOG.log(Level.WARNING, exc.getMessage(), exc);
				this.unsetKiekerThreadLocalData();
				return;
			}

			/* 3.) Extract ess from SOAP header */
			// No need to fetch ess from reponse header since the stack size
			// is the same as before the request.

			/* 4. Extract traceId from SOAP header */
			hdr = soapMsg.getHeader(SOAPHeaderConstants.TRACE_IDENTIFIER_QNAME);
			final String traceIdStr = this.getStringContentFromHeader(hdr); // null if hdr==null
			if (traceIdStr == null) {
				/*
				 * No Kieker trace Id in header.
				 * This may happen for responses from callees w/o Kieker instrumentation.
				 */
				LOG.log(Level.FINE, "Found no Kieker traceId in response header. "
						+ "Will unset all threadLocal variables");
				this.unsetKiekerThreadLocalData();
				return;
			}
			long traceId;
			try {
				traceId = Long.parseLong(traceIdStr);
			} catch (final NumberFormatException exc) {
				/* Invalid trace id! */
				LOG.log(Level.WARNING, exc.getMessage(), exc);
				this.unsetKiekerThreadLocalData();
				return;
			}

			/* Recall my thread-local data stored before the SOAP call */
			final long myTraceId = CF_REGISTRY.recallThreadLocalTraceId();
			final String mySessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
			final int myEoi = CF_REGISTRY.recallThreadLocalEOI();
			final int myEss = CF_REGISTRY.recallThreadLocalESS();
			final long myTin = SOAP_REGISTRY.recallThreadLocalOutRequestTin();
			final long myTout = this.timeSource.getTime();

			if (myTraceId != traceId) {
				LOG.log(Level.WARNING, "Inconsistency between traceId before and after SOAP request:\n" + myTraceId
						+ "(before) != " + traceId + "(after)");
			}

			// Log this execution
			final OperationExecutionRecord rec = new OperationExecutionRecord(SIGNATURE, mySessionId, myTraceId, myTin,
					myTout, this.vmName, myEoi, myEss);
			this.monitoringController.newMonitoringRecord(rec);

			/*
			 * Store received Kieker EOI
			 * ESS remains the same as before the call since we didn't increment the variable!
			 */
			CF_REGISTRY.storeThreadLocalEOI(eoi);

			if (isEntryCall) { // clean up iff trace's origin was right before the call!
				this.unsetKiekerThreadLocalData();
			}
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

	private final void unsetKiekerThreadLocalData() {
		CF_REGISTRY.unsetThreadLocalTraceId();
		SESSION_REGISTRY.unsetThreadLocalSessionId();
		CF_REGISTRY.unsetThreadLocalEOI();
		CF_REGISTRY.unsetThreadLocalESS();
		SOAP_REGISTRY.unsetThreadLocalOutRequestIsEntryCall();
		SOAP_REGISTRY.unsetThreadLocalOutRequestTin();
	}
}
