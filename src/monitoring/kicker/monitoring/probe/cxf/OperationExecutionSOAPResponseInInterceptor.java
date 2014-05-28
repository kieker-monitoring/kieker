/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.monitoring.probe.cxf;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.w3c.dom.Element;

import kicker.common.logging.Log;
import kicker.common.logging.LogFactory;
import kicker.common.record.controlflow.OperationExecutionRecord;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.core.controller.MonitoringController;
import kicker.monitoring.core.registry.ControlFlowRegistry;
import kicker.monitoring.core.registry.SessionRegistry;
import kicker.monitoring.probe.IMonitoringProbe;
import kicker.monitoring.timer.ITimeSource;

/**
 * CXF InInterceptor to get the sessionIdentifier header from an incoming soap message
 * and associate it with the current thread id.
 * 
 * Look here how to add it to your server config: http://cwiki.apache.org/CXF20DOC/interceptors.html
 */
/**
 * @author Andre van Hoorn, Dennis Kieselhorst
 * 
 * @since 1.0
 */
public class OperationExecutionSOAPResponseInInterceptor extends SoapHeaderInterceptor implements IMonitoringProbe {
	// the CXF logger uses java.util.logging by default, look here how to change it to log4j: http://cwiki.apache.org/CXF20DOC/debugging.html

	/** This constant contains the signature, which will be used in the monitoring logs. */
	public static final String SIGNATURE = "public void " + OperationExecutionSOAPResponseInInterceptor.class.getName()
			+ ".handleMessage(org.apache.cxf.message.Message)";

	/** Stores the singleton instance of the session registry. */
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	/** Stores the singleton instance of the control flow registry. */
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	/** Stores the singleton instance of the SOAP trace registry. */
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();

	private static final Log LOG = LogFactory.getLog(OperationExecutionSOAPResponseInInterceptor.class);

	/** The monitoring controller of this interceptor. */
	protected final IMonitoringController monitoringController;
	/** The used time source. */
	protected final ITimeSource timeSource;
	/** The name of the VM. */
	protected final String vmName;

	/**
	 * Creates a new instance of this class, using the singleton instance of the {@link MonitoringController} as controller.
	 */
	public OperationExecutionSOAPResponseInInterceptor() {
		this(MonitoringController.getInstance());
	}

	/**
	 * Creates a new instance of this class, using the given instance of a {@link MonitoringController} as controller.
	 * 
	 * @param monitoringCtrl
	 *            The controller of this interceptor.
	 */
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
		if (!this.monitoringController.isProbeActivated(SIGNATURE)) {
			return;
		}
		if (msg instanceof SoapMessage) {
			final boolean isEntryCall = SOAP_REGISTRY.recallThreadLocalOutRequestIsEntryCall(); // NOPMD (must be requerst here!)
			final SoapMessage soapMsg = (SoapMessage) msg;

			// 1.) Extract sessionId from SOAP header
			// No need to fetch sessionId from reponse header since it must be
			// the same as before the request.

			// 2.) Extract eoi from SOAP header
			Header hdr = soapMsg.getHeader(SOAPHeaderConstants.EOI_IDENTIFIER_QNAME);
			final String eoiStr = this.getStringContentFromHeader(hdr); // null if hdr==null
			if (eoiStr == null) {
				// No Kicker eoi in header. This may happen for responses from callees w/o Kicker instrumentation.

				LOG.info("Found no Kicker eoi in response header. Will unset all threadLocal variables");
				this.unsetKickerThreadLocalData();
				return;
			}
			int eoi = 0;
			try {
				eoi = Integer.parseInt(eoiStr);
			} catch (final NumberFormatException exc) {
				LOG.warn("Invalid EOI", exc);
				this.unsetKickerThreadLocalData();
				return;
			}

			// 3.) Extract ess from SOAP header
			// No need to fetch ess from reponse header since the stack size
			// is the same as before the request.

			// 4. Extract traceId from SOAP header
			hdr = soapMsg.getHeader(SOAPHeaderConstants.TRACE_IDENTIFIER_QNAME);
			final String traceIdStr = this.getStringContentFromHeader(hdr); // null if hdr==null
			if (traceIdStr == null) {
				// No Kicker trace Id in header. This may happen for responses from callees w/o Kicker instrumentation.

				LOG.info("Found no Kicker traceId in response header. Will unset all threadLocal variables");
				this.unsetKickerThreadLocalData();
				return;
			}
			long traceId;
			try {
				traceId = Long.parseLong(traceIdStr);
			} catch (final NumberFormatException exc) {
				LOG.warn("Invalid trace id", exc);
				this.unsetKickerThreadLocalData();
				return;
			}

			// Recall my thread-local data stored before the SOAP call
			final long myTraceId = CF_REGISTRY.recallThreadLocalTraceId();
			final String mySessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
			final int myEoi = CF_REGISTRY.recallThreadLocalEOI();
			final int myEss = CF_REGISTRY.recallThreadLocalESS();
			final long myTin = SOAP_REGISTRY.recallThreadLocalOutRequestTin();
			final long myTout = this.timeSource.getTime();

			if (myTraceId != traceId) {
				LOG.warn("Inconsistency between traceId before and after SOAP request:\n" + myTraceId + "(before) != " + traceId + "(after)");
			}

			// Log this execution
			final OperationExecutionRecord rec = new OperationExecutionRecord(SIGNATURE, mySessionId, myTraceId, myTin,
					myTout, this.vmName, myEoi, myEss);
			this.monitoringController.newMonitoringRecord(rec);

			// Store received Kicker EOI. ESS remains the same as before the call since we didn't increment the variable!

			CF_REGISTRY.storeThreadLocalEOI(eoi);

			if (isEntryCall) { // clean up iff trace's origin was right before the call!
				this.unsetKickerThreadLocalData();
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

	private final void unsetKickerThreadLocalData() {
		CF_REGISTRY.unsetThreadLocalTraceId();
		SESSION_REGISTRY.unsetThreadLocalSessionId();
		CF_REGISTRY.unsetThreadLocalEOI();
		CF_REGISTRY.unsetThreadLocalESS();
		SOAP_REGISTRY.unsetThreadLocalOutRequestIsEntryCall();
		SOAP_REGISTRY.unsetThreadLocalOutRequestTin();
	}
}
