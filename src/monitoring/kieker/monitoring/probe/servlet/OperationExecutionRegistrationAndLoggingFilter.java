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

package kieker.monitoring.probe.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * Register session and trace id for incoming request.
 * This probe also logs activations of this probe as execution records.
 * 
 * It can be integrated into the web.xml as follows:
 * 
 * <filter>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <filter-class>kieker.monitoring.probe.servlet.OperationExecutionRegistrationAndLoggingFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * 
 * @author Marco Luebcke
 */
public class OperationExecutionRegistrationAndLoggingFilter implements Filter, IMonitoringProbe {

	private static final String COMPONENT_NAME = OperationExecutionRegistrationAndLoggingFilter.class.getName();
	private static final String OP_NAME = "doFilter(ServletRequest request, ServletResponse response, FilterChain chain)";
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final IMonitoringController CTRL_INST = MonitoringController.getInstance();
	private static final ITimeSource TIMESOURCE = OperationExecutionRegistrationAndLoggingFilter.CTRL_INST.getTimeSource();
	private static final String VM_NAME = OperationExecutionRegistrationAndLoggingFilter.CTRL_INST.getHostName();

	private static final String NULL_SESSION_STR = "NULL-SERVLETFILTER";

	/**
	 * Constructs an {@link OperationExecutionRegistrationAndLoggingFilter}.
	 */
	public OperationExecutionRegistrationAndLoggingFilter() {
		// nothing to do
	}

	@Override
	public void init(final FilterConfig config) throws ServletException {
		// nothing to do
	}

	/**
	 * Returns the session ID from request @r or null if no session in @r.
	 * 
	 */
	public final String getSessionId(final HttpServletRequest httpReq) {
		final HttpSession session = httpReq.getSession(false);
		if (session != null) {
			return session.getId();
		} else {
			return null;
		}
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		OperationExecutionRecord execData = null;
		final int eoi = 0; /* this is executionOrderIndex-th execution in this trace */
		final int ess = 0; /* this is the height in the dynamic call tree of this execution */
		if (request instanceof HttpServletRequest) {
			execData = new OperationExecutionRecord(OperationExecutionRegistrationAndLoggingFilter.COMPONENT_NAME,
					OperationExecutionRegistrationAndLoggingFilter.OP_NAME,
					OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId() /* traceId, -1 if entry point */);
			execData.setSessionId(this.getSessionId((HttpServletRequest) request));
			if (execData.getSessionId() == null) {
				execData.setSessionId(OperationExecutionRegistrationAndLoggingFilter.NULL_SESSION_STR);
			}
			OperationExecutionRegistrationAndLoggingFilter.SESSION_REGISTRY.storeThreadLocalSessionId(execData.getSessionId());
			execData.setEntryPoint(true); // of course (however, we never evaluate it here)!
			OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.storeThreadLocalEOI(0); // current execution's eoi is 0
			OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.storeThreadLocalESS(1); // *current* execution's ess is 0
			execData.setHostName(OperationExecutionRegistrationAndLoggingFilter.VM_NAME);
			execData.setExperimentId(OperationExecutionRegistrationAndLoggingFilter.CTRL_INST.getExperimentId());
			execData.setTin(OperationExecutionRegistrationAndLoggingFilter.TIMESOURCE.getTime());
		}
		try {
			chain.doFilter(request, response);
		} finally {
			if (execData != null) {
				execData.setTout(OperationExecutionRegistrationAndLoggingFilter.TIMESOURCE.getTime());
				execData.setEoi(eoi);
				execData.setEss(ess);
				// if execData.sessionId == null, try again to fetch it (should exist after being within the application logic)
				if (execData.getSessionId() == null) {
					// log.info("TraceID" + execData.traceId + "had no sessionId so far. Now?");
					execData.setSessionId(this.getSessionId((HttpServletRequest) request));
					// log.info("New sessionId? " + execData.sessionId);
				}
				// TOOD: ?only log record if cfRegistry.recallThreadLocalEOI > 0?
				OperationExecutionRegistrationAndLoggingFilter.CTRL_INST.newMonitoringRecord(execData);
			}
			// since we are in an entry point:
			OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.unsetThreadLocalTraceId();
			OperationExecutionRegistrationAndLoggingFilter.SESSION_REGISTRY.unsetThreadLocalSessionId();
			OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.unsetThreadLocalEOI();
			OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.unsetThreadLocalESS();
		}
	}

	@Override
	public void destroy() {
		// nothing to do
	}
}
