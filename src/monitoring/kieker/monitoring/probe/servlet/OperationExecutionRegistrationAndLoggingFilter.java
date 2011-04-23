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

	private static final String componentName = OperationExecutionRegistrationAndLoggingFilter.class.getName();
	private static final String opName = "doFilter(ServletRequest request, ServletResponse response, FilterChain chain)";
	private static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
	private static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
	private static final IMonitoringController ctrlInst = MonitoringController.getInstance();
	private static final ITimeSource timesource = ctrlInst.getTimeSource();
	private static final String vmName = OperationExecutionRegistrationAndLoggingFilter.ctrlInst.getHostName();

	private static final String NULL_SESSION_STR = "NULL-SERVLETFILTER";

	@Override
	public void init(final FilterConfig config) throws ServletException {
	}

	/**
	 * Returns the session ID from request @r or null if no session in @r.
	 * 
	 */
	public final String getSessionId(final HttpServletRequest httpReq) {
		final HttpSession session = (httpReq).getSession(false);
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
			execData = new OperationExecutionRecord(OperationExecutionRegistrationAndLoggingFilter.componentName,
					OperationExecutionRegistrationAndLoggingFilter.opName,
					OperationExecutionRegistrationAndLoggingFilter.cfRegistry.getAndStoreUniqueThreadLocalTraceId() /* traceId, -1 if entry point */);
			execData.sessionId = this.getSessionId((HttpServletRequest) request);
			if (execData.sessionId == null) {
				execData.sessionId = OperationExecutionRegistrationAndLoggingFilter.NULL_SESSION_STR;
			}
			OperationExecutionRegistrationAndLoggingFilter.sessionRegistry.storeThreadLocalSessionId(execData.sessionId);
			execData.isEntryPoint = true; // of course (however, we never evaluate it here)!
			OperationExecutionRegistrationAndLoggingFilter.cfRegistry.storeThreadLocalEOI(0); // current execution's eoi is 0
			OperationExecutionRegistrationAndLoggingFilter.cfRegistry.storeThreadLocalESS(1); // *current* execution's ess is 0
			execData.hostName = OperationExecutionRegistrationAndLoggingFilter.vmName;
			execData.experimentId = OperationExecutionRegistrationAndLoggingFilter.ctrlInst.getExperimentId();
			execData.tin = timesource.getTime();
		}
		try {
			chain.doFilter(request, response);
		} finally {
			if (execData != null) {
				execData.tout = timesource.getTime();
				execData.eoi = eoi;
				execData.ess = ess;
				// if execData.sessionId == null, try again to fetch it (should exist after being within the application logic)
				if (execData.sessionId == null) {
					// log.info("TraceID" + execData.traceId + "had no sessionId so far. Now?");
					execData.sessionId = this.getSessionId((HttpServletRequest) request);
					// log.info("New sessionId? " + execData.sessionId);
				}
				// TOOD: ?only log record if cfRegistry.recallThreadLocalEOI > 0?
				OperationExecutionRegistrationAndLoggingFilter.ctrlInst.newMonitoringRecord(execData);
			}
			// since we are in an entry point:
			OperationExecutionRegistrationAndLoggingFilter.cfRegistry.unsetThreadLocalTraceId();
			OperationExecutionRegistrationAndLoggingFilter.sessionRegistry.unsetThreadLocalSessionId();
			OperationExecutionRegistrationAndLoggingFilter.cfRegistry.unsetThreadLocalEOI();
			OperationExecutionRegistrationAndLoggingFilter.cfRegistry.unsetThreadLocalESS();
		}
	}

	@Override
	public void destroy() {
	}
}
