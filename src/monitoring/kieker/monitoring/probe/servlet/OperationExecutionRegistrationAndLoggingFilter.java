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

import kieker.common.record.controlflow.OperationExecutionRecord;
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
 * @author Marco Luebcke, Jan Waller
 */
public class OperationExecutionRegistrationAndLoggingFilter implements Filter, IMonitoringProbe {
	private static final String SIGNATURE = "public void " + OperationExecutionRegistrationAndLoggingFilter.class.getName()
			+ ".doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)";
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final IMonitoringController CTRL_INST = MonitoringController.getInstance();
	private static final ITimeSource TIMESOURCE = OperationExecutionRegistrationAndLoggingFilter.CTRL_INST.getTimeSource();
	private static final String VM_NAME = OperationExecutionRegistrationAndLoggingFilter.CTRL_INST.getHostname();

	/**
	 * Constructs an {@link OperationExecutionRegistrationAndLoggingFilter}.
	 */
	public OperationExecutionRegistrationAndLoggingFilter() {
		// nothing to do
	}

	public void init(final FilterConfig config) throws ServletException {
		// nothing to do
	}

	private final String getSessionId(final ServletRequest request) {
		if (request instanceof HttpServletRequest) {
			final HttpSession session = ((HttpServletRequest) request).getSession(false);
			if (session != null) {
				return session.getId();
			}
		}
		return null;
	}

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		if (!OperationExecutionRegistrationAndLoggingFilter.CTRL_INST.isMonitoringEnabled()) {
			chain.doFilter(request, response);
		}
		final long traceId = OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId(); // traceId, -1 if entry point
		String sessionId = this.getSessionId(request);
		if (sessionId != null) {
			OperationExecutionRegistrationAndLoggingFilter.SESSION_REGISTRY.storeThreadLocalSessionId(sessionId);
		}
		OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.storeThreadLocalEOI(0); // current execution's eoi is 0
		OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.storeThreadLocalESS(1); // *current* execution's ess is 0
		final String hostname = OperationExecutionRegistrationAndLoggingFilter.VM_NAME;
		final long tin = OperationExecutionRegistrationAndLoggingFilter.TIMESOURCE.getTime();
		try {
			chain.doFilter(request, response);
		} finally {
			final long tout = OperationExecutionRegistrationAndLoggingFilter.TIMESOURCE.getTime();
			// if sessionId == null, try again to fetch it (should exist after being within the application logic)
			if (sessionId == null) {
				sessionId = this.getSessionId(request);
			}
			// TOOD: ?only log record if cfRegistry.recallThreadLocalEOI > 0?
			OperationExecutionRegistrationAndLoggingFilter.CTRL_INST.newMonitoringRecord(
					new OperationExecutionRecord(OperationExecutionRegistrationAndLoggingFilter.SIGNATURE, sessionId, traceId, tin, tout, hostname, 0, 0));
			// since we are in an entry point:
			OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.unsetThreadLocalTraceId();
			OperationExecutionRegistrationAndLoggingFilter.SESSION_REGISTRY.unsetThreadLocalSessionId();
			OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.unsetThreadLocalEOI();
			OperationExecutionRegistrationAndLoggingFilter.CF_REGISTRY.unsetThreadLocalESS();
		}
	}

	public void destroy() {
		// nothing to do
	}
}
