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
	private static final IMonitoringController CTRL_INST = MonitoringController.getInstance();
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.getInstance();
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.getInstance();
	protected static final SOAPTraceRegistry SOAP_REGISTRY = SOAPTraceRegistry.getInstance();
	protected static final ITimeSource TIMESOURCE = OperationExecutionSOAPResponseInInterceptor.CTRL_INST.getTimeSource();

	private static final String COMPONENT_NAME = OperationExecutionSOAPResponseInInterceptor.class.getName();
	private static final String OP_NAME = "handleMessage(SoapMessage msg)";
	protected static final String VM_NAME = OperationExecutionSOAPResponseInInterceptor.CTRL_INST.getHostName();

	@Override
	public void handleMessage(final Message msg) throws Fault {
		if (msg instanceof SoapMessage) {
			final boolean isEntryCall = OperationExecutionSOAPResponseInInterceptor.SOAP_REGISTRY.recallThreadLocalOutRequestIsEntryCall();
			final SoapMessage soapMsg = (SoapMessage) msg;

			/* 1.) Extract sessionId from SOAP header */
			// No need to fetch sessionId from reponse header since it must be
			// the same as before the request.

			/* 2.) Extract eoi from SOAP header */
			Header hdr = soapMsg.getHeader(SOAPHeaderConstants.EOI_IDENTIFIER_QNAME);
			final String eoiStr = getStringContentFromHeader(hdr); // null if hdr==null
			if (eoiStr == null) {
				/*
				 * No Kieker eoi in header.
				 * This may happen for responses from callees w/o Kieker instrumentation.
				 */
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.FINE, "Found no Kieker eoi in response header. " + "Will unset all threadLocal variables");
				unsetKiekerThreadLocalData();
				return;
			}
			int eoi = 0;
			try {
				eoi = Integer.parseInt(eoiStr);
			} catch (final Exception exc) {
				/* invalid eoi! */
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.WARNING, exc.getMessage(), exc);
				unsetKiekerThreadLocalData();
				return;
			}

			/* 3.) Extract ess from SOAP header */
			// No need to fetch ess from reponse header since the stack size
			// is the same as before the request.

			/* 4. Extract traceId from SOAP header */
			hdr = soapMsg.getHeader(SOAPHeaderConstants.TRACE_IDENTIFIER_QNAME);
			final String traceIdStr = getStringContentFromHeader(hdr); // null if hdr==null
			if (traceIdStr == null) {
				/*
				 * No Kieker trace Id in header.
				 * This may happen for responses from callees w/o Kieker instrumentation.
				 */
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.FINE, "Found no Kieker traceId in response header. "
						+ "Will unset all threadLocal variables");
				unsetKiekerThreadLocalData();
				return;
			}
			long traceId;
			try {
				traceId = Long.parseLong(traceIdStr);
			} catch (final Exception exc) {
				/* Invalid trace id! */
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.WARNING, exc.getMessage(), exc);
				unsetKiekerThreadLocalData();
				return;
			}

			/* Recall my thread-local data stored before the SOAP call */
			final long myTraceId = OperationExecutionSOAPResponseInInterceptor.CF_REGISTRY.recallThreadLocalTraceId();
			final String mySessionId = OperationExecutionSOAPResponseInInterceptor.SESSION_REGISTRY.recallThreadLocalSessionId();
			final int myEoi = OperationExecutionSOAPResponseInInterceptor.CF_REGISTRY.recallThreadLocalEOI();
			final int myEss = OperationExecutionSOAPResponseInInterceptor.CF_REGISTRY.recallThreadLocalESS();
			final long myTin = OperationExecutionSOAPResponseInInterceptor.SOAP_REGISTRY.recallThreadLocalOutRequestTin();
			final long myTout = OperationExecutionSOAPResponseInInterceptor.TIMESOURCE.getTime();
			// TODO: Remove following plausibility checks if implementation stable
			// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/162
			if (myTraceId != traceId) {
				OperationExecutionSOAPResponseInInterceptor.LOG.log(Level.WARNING, "Inconsistency between traceId before and after SOAP request:\n" + myTraceId
						+ "(before) != " + traceId + "(after)");
			}

			// Log this execution
			final OperationExecutionRecord rec = new OperationExecutionRecord(OperationExecutionSOAPResponseInInterceptor.COMPONENT_NAME,
					OperationExecutionSOAPResponseInInterceptor.OP_NAME, mySessionId, myTraceId, myTin, myTout, OperationExecutionSOAPResponseInInterceptor.VM_NAME,
					myEoi, myEss);
			rec.experimentId = OperationExecutionSOAPResponseInInterceptor.CTRL_INST.getExperimentId();
			OperationExecutionSOAPResponseInInterceptor.CTRL_INST.newMonitoringRecord(rec);

			/*
			 * Store received Kieker EOI
			 * ESS remains the same as before the call since we didn't increment the variable!
			 */
			OperationExecutionSOAPResponseInInterceptor.CF_REGISTRY.storeThreadLocalEOI(eoi);

			if (isEntryCall) { // clean up iff trace's origin was right before the call!
				unsetKiekerThreadLocalData();
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
		OperationExecutionSOAPResponseInInterceptor.CF_REGISTRY.unsetThreadLocalTraceId();
		OperationExecutionSOAPResponseInInterceptor.SESSION_REGISTRY.unsetThreadLocalSessionId();
		OperationExecutionSOAPResponseInInterceptor.CF_REGISTRY.unsetThreadLocalEOI();
		OperationExecutionSOAPResponseInInterceptor.CF_REGISTRY.unsetThreadLocalESS();
		OperationExecutionSOAPResponseInInterceptor.SOAP_REGISTRY.unsetThreadLocalOutRequestIsEntryCall();
		OperationExecutionSOAPResponseInInterceptor.SOAP_REGISTRY.unsetThreadLocalOutRequestTin();
	}
}
