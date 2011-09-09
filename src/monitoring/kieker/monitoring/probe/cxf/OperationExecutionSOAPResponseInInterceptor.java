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

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
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
 * @author Andre van Hoorn, Dennis Kieselhorst
 */
public class OperationExecutionSOAPResponseInInterceptor extends SoapHeaderInterceptor implements IMonitoringProbe {
	// the CXF logger uses java.util.logging by default, look here how to change it to log4j: http://cwiki.apache.org/CXF20DOC/debugging.html

	private static final Logger LOG = LogUtils.getL7dLogger(OperationExecutionSOAPResponseInInterceptor.class);
	private static final IMonitoringController ctrlInst = MonitoringController.getInstance();
	protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
	protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
	protected static final SOAPTraceRegistry soapRegistry = SOAPTraceRegistry.getInstance();
	protected static final ITimeSource timesource = OperationExecutionSOAPResponseInInterceptor.ctrlInst.getTimeSource();

	private static final String componentName = OperationExecutionSOAPResponseInInterceptor.class.getName();
	private static final String opName = "handleMessage(SoapMessage msg)";
	protected static final String vmName = OperationExecutionSOAPResponseInInterceptor.ctrlInst.getHostName();

	@Override
	public void handleMessage(final Message msg) throws Fault {
		if (msg instanceof SoapMessage) {
			final boolean isEntryCall = OperationExecutionSOAPResponseInInterceptor.soapRegistry.recallThreadLocalOutRequestIsEntryCall();
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
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.FINE, "Found no Kieker eoi in response header. "
						+ "Will unset all threadLocal variables");
				this.unsetKiekerThreadLocalData();
				return;
			}
			int eoi = 0;
			try {
				eoi = Integer.parseInt(eoiStr);
			} catch (final Exception exc) {
				/* invalid eoi! */
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.WARNING, exc.getMessage(), exc);
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
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.FINE, "Found no Kieker traceId in response header. "
						+ "Will unset all threadLocal variables");
				this.unsetKiekerThreadLocalData();
				return;
			}
			long traceId = -1;
			try {
				traceId = Long.parseLong(traceIdStr);
			} catch (final Exception exc) {
				/* Invalid trace id! */
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.WARNING, exc.getMessage(), exc);
				this.unsetKiekerThreadLocalData();
				return;
			}

			/* Recall my thread-local data stored before the SOAP call */
			final long myTraceId = OperationExecutionSOAPResponseInInterceptor.cfRegistry.recallThreadLocalTraceId();
			final String mySessionId = OperationExecutionSOAPResponseInInterceptor.sessionRegistry.recallThreadLocalSessionId();
			final int myEoi = OperationExecutionSOAPResponseInInterceptor.cfRegistry.recallThreadLocalEOI();
			final int myEss = OperationExecutionSOAPResponseInInterceptor.cfRegistry.recallThreadLocalESS();
			final long myTin = OperationExecutionSOAPResponseInInterceptor.soapRegistry.recallThreadLocalOutRequestTin();
			final long myTout = OperationExecutionSOAPResponseInInterceptor.timesource.getTime();
			// TODO: Remove following plausibility checks if implementation stable
			// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/162
			if (myTraceId != traceId) {
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.WARNING, "Inconsistency between traceId before and after SOAP request:\n" + ""
						+ myTraceId + "(before) != " + traceId + "(after)");
			}

			// Log this execution
			final OperationExecutionRecord rec = new OperationExecutionRecord(OperationExecutionSOAPResponseInInterceptor.componentName,
					OperationExecutionSOAPResponseInInterceptor.opName, mySessionId, myTraceId, myTin, myTout,
					OperationExecutionSOAPResponseInInterceptor.vmName, myEoi, myEss);
			rec.experimentId = OperationExecutionSOAPResponseInInterceptor.ctrlInst.getExperimentId();
			OperationExecutionSOAPResponseInInterceptor.ctrlInst.newMonitoringRecord(rec);

			/*
			 * Store received Kieker EOI
			 * ESS remains the same as before the call since we didn't increment the variable!
			 */
			OperationExecutionSOAPResponseInInterceptor.cfRegistry.storeThreadLocalEOI(eoi);

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
		OperationExecutionSOAPResponseInInterceptor.cfRegistry.unsetThreadLocalTraceId();
		OperationExecutionSOAPResponseInInterceptor.sessionRegistry.unsetThreadLocalSessionId();
		OperationExecutionSOAPResponseInInterceptor.cfRegistry.unsetThreadLocalEOI();
		OperationExecutionSOAPResponseInInterceptor.cfRegistry.unsetThreadLocalESS();
		OperationExecutionSOAPResponseInInterceptor.soapRegistry.unsetThreadLocalOutRequestIsEntryCall();
		OperationExecutionSOAPResponseInInterceptor.soapRegistry.unsetThreadLocalOutRequestTin();
	}
}
