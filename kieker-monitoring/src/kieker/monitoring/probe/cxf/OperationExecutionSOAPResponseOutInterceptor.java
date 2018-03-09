/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderOutFilterInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * CXF OutInterceptor to set the sessionIdentifier header for an outgoing soap
 * message.
 *
 * Look here how to add it to your client config:
 * http://cwiki.apache.org/CXF20DOC/interceptors.html
 *
 * Setting the soap header with jaxb or aegis databinding didn't work yet:
 * http://www.nabble.com/Add-%22out-of-band%22-soap-header-using-simple-frontend-td19380093.html
 *
 * @author Dennis Kieselhorst, Andre van Hoorn
 *
 * @since 1.0
 */
public class OperationExecutionSOAPResponseOutInterceptor extends SoapHeaderOutFilterInterceptor
		implements IMonitoringProbe {

	/**
	 * This constant contains the signature, which will be used in the monitoring
	 * logs.
	 */
	public static final String SIGNATURE = "public void " + OperationExecutionSOAPResponseOutInterceptor.class.getName()
			+ ".handleMessage(org.apache.cxf.binding.soap.SoapMessage)";

	/** Stores the singleton instance of the control flow registry. */
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	/** Stores the singleton instance of the session registry. */
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	/** Stores the singleton instance of the SOAP trace registry. */
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();

	private static final Log LOG = LogFactory.getLog(OperationExecutionSOAPResponseOutInterceptor.class);

	/** The monitoring controller of this interceptor. */
	protected final IMonitoringController monitoringController;
	/** The used time source. */
	protected final ITimeSource timeSource;
	/** The name of the VM. */
	protected final String vmName;

	/**
	 * Creates a new instance of this class, using the singleton instance of the
	 * {@link MonitoringController} as controller.
	 */
	public OperationExecutionSOAPResponseOutInterceptor() {
		this(MonitoringController.getInstance());
	}

	/**
	 * Creates a new instance of this class, using the given instance of a
	 * {@link MonitoringController} as controller.
	 *
	 * @param monitoringCtrl
	 *            The controller of this interceptor.
	 */
	public OperationExecutionSOAPResponseOutInterceptor(final IMonitoringController monitoringCtrl) {
		this.monitoringController = monitoringCtrl;
		this.timeSource = this.monitoringController.getTimeSource();
		this.vmName = this.monitoringController.getHostname();
	}

	@Override
	public void handleMessage(final SoapMessage msg) throws Fault {
		if (!this.monitoringController.isMonitoringEnabled()) {
			return;
		}
		if (!this.monitoringController.isProbeActivated(OperationExecutionSOAPResponseOutInterceptor.SIGNATURE)) {
			return;
		}
		final String sessionID;
		final long traceId = OperationExecutionSOAPResponseOutInterceptor.CF_REGISTRY.recallThreadLocalTraceId();
		final long tin;
		final long tout;
		boolean isEntryCall = true;
		int eoi = -1;
		// final int ess = -1;
		int myEoi = -1;
		int myEss = -1;

		if (traceId == -1) {
			// Kieker trace Id not registered. Should not happen, since this is a response
			// message!
			OperationExecutionSOAPResponseOutInterceptor.LOG
					.warn("Kieker traceId not registered. Will unset all threadLocal variables and return.");
			this.unsetKiekerThreadLocalData(); // unset all variables
			return;
		} else {
			// thread-local traceId exists: eoi, ess, and sessionID should have been
			// registered before
			eoi = OperationExecutionSOAPResponseOutInterceptor.CF_REGISTRY.recallThreadLocalEOI();
			sessionID = OperationExecutionSOAPResponseOutInterceptor.SESSION_REGISTRY.recallThreadLocalSessionId();
			myEoi = OperationExecutionSOAPResponseOutInterceptor.SOAP_REGISTRY.recallThreadLocalInRequestEOI();
			myEss = OperationExecutionSOAPResponseOutInterceptor.SOAP_REGISTRY.recallThreadLocalInRequestESS();
			tin = OperationExecutionSOAPResponseOutInterceptor.SOAP_REGISTRY.recallThreadLocalInRequestTin();
			tout = this.timeSource.getTime();
			isEntryCall = OperationExecutionSOAPResponseOutInterceptor.SOAP_REGISTRY
					.recallThreadLocalInRequestIsEntryCall();
		}

		// The trace is leaving this node, thus we need to clean up the thread-local
		// variables.
		this.unsetKiekerThreadLocalData();

		// Log this execution
		final OperationExecutionRecord rec = new OperationExecutionRecord(
				OperationExecutionSOAPResponseOutInterceptor.SIGNATURE, sessionID, traceId, tin, tout, this.vmName,
				myEoi, myEss);
		this.monitoringController.newMonitoringRecord(rec);

		// We don't put Kieker data into response header if request didn't contain
		// Kieker information
		if (isEntryCall) {
			return;
		}

		final Document d = DOMUtils.createDocument();
		Element e;
		Header hdr;
		// .) Add sessionId to response header
		// There's no need to pass the session ID back.

		// 2.) Add traceId to response header
		// Actually, there's no need to pass the trace ID back but
		// we do this for consistency checks on the caller side.
		e = d.createElementNS(SOAPHeaderConstants.NAMESPACE_URI, SOAPHeaderConstants.TRACE_QUALIFIED_NAME);
		e.setTextContent(Long.toString(traceId));
		hdr = new Header(SOAPHeaderConstants.TRACE_IDENTIFIER_QNAME, e);
		msg.getHeaders().add(hdr);

		// 3.) Add eoi to response header
		e = d.createElementNS(SOAPHeaderConstants.NAMESPACE_URI, SOAPHeaderConstants.EOI_QUALIFIED_NAME);
		e.setTextContent(Integer.toString(eoi));
		hdr = new Header(SOAPHeaderConstants.EOI_IDENTIFIER_QNAME, e);
		msg.getHeaders().add(hdr);

		// 4.) Add ess to response header
		// There's no need to pass the ESS back.
	}

	private final void unsetKiekerThreadLocalData() {
		OperationExecutionSOAPResponseOutInterceptor.CF_REGISTRY.unsetThreadLocalTraceId();
		OperationExecutionSOAPResponseOutInterceptor.CF_REGISTRY.unsetThreadLocalEOI();
		OperationExecutionSOAPResponseOutInterceptor.CF_REGISTRY.unsetThreadLocalESS();
		OperationExecutionSOAPResponseOutInterceptor.SESSION_REGISTRY.unsetThreadLocalSessionId();
		OperationExecutionSOAPResponseOutInterceptor.SOAP_REGISTRY.unsetThreadLocalInRequestIsEntryCall();
		OperationExecutionSOAPResponseOutInterceptor.SOAP_REGISTRY.unsetThreadLocalInRequestTin();
		OperationExecutionSOAPResponseOutInterceptor.SOAP_REGISTRY.unsetThreadLocalInRequestEOI();
		OperationExecutionSOAPResponseOutInterceptor.SOAP_REGISTRY.unsetThreadLocalInRequestESS();
	}
}
