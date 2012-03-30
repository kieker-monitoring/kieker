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

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.ClassOperationSignaturePair;
import kieker.common.util.Signature;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * For each incoming request via {@link #doFilter(ServletRequest, ServletResponse, FilterChain)}, this class
 * (i) registers session and trace information into the thread-local data structures {@link SessionRegistry} and {@link TraceRegistry} accessible to other probes in
 * the control-flow of this request, (ii) executes the given {@link FilterChain} and subsequently (iii) unregisters the thread-local
 * data. If configured in the {@link FilterConfig} (see below), the execution of the {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} method
 * is also part of the trace and logged to the {@link IMonitoringController} (note that this is the default behavior when no property is found).
 * 
 * The filter can be integrated into the web.xml as follows:
 * 
 * <filter>
 * <filter-name>sessionAndTraceRegistrationFilter</filter-name>
 * <filter-class>SessionAndTraceRegistrationFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>sessionAndTraceRegistrationFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 * 
 * TODO: Properties
 * 
 * @author Andre van Hoorn, Marco Luebcke, Jan Waller
 * 
 */
public class SessionAndTraceRegistrationFilter implements Filter, IMonitoringProbe {
	private static final Log LOG = LogFactory.getLog(SessionAndTraceRegistrationFilter.class);

	public static final String CONFIG_PROPERTY_NAME_LOG_FILTER_EXECUTION = "logFilterExecution";

	protected static final IMonitoringController MONITORING_CTRL = MonitoringController.getInstance();
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;

	protected static final ITimeSource TIMESOURCE = MONITORING_CTRL.getTimeSource();
	protected static final String VM_NAME = MONITORING_CTRL.getHostname();

	/**
	 * Signature for the {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} which will be used when logging
	 * executions of this method.
	 */
	private final String filterOperationSignatureString;

	private volatile boolean logFilterExecution = true; // default

	public SessionAndTraceRegistrationFilter() {
		super();
		final Signature methodSignature =
				new Signature("doFilter", // operation name
						new String[] { "public", "void" }, // modifier list
						"void", // return type
						new String[] { ServletRequest.class.getName(), ServletResponse.class.getName(), FilterChain.class.getName() }); // arg types
		final ClassOperationSignaturePair filterOperationSignaturePair =
				new ClassOperationSignaturePair(SessionAndTraceRegistrationFilter.class.getName(), methodSignature);
		this.filterOperationSignatureString = filterOperationSignaturePair.toString();
	}

	public SessionAndTraceRegistrationFilter(final boolean logFilterExecution) {
		this();
		this.logFilterExecution = logFilterExecution;
	}

	/**
	 * Returns the operation signature of this filter's {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} operation
	 * to be used when logging executions of this operation.
	 * 
	 * Extending classes may override this method in order to provide an alternative signature. However,
	 * note that this method is executed on each filter execution. Hence, you should return a final
	 * value here instead of executing expensive String operations.
	 * 
	 * @return
	 */
	protected String getFilterOperationSignatureString() {
		return this.filterOperationSignatureString;
	}

	public void init(final FilterConfig config) throws ServletException {
		// by default, we do nothing here. Extending classes may override this method
		final String valString = config.getInitParameter(CONFIG_PROPERTY_NAME_LOG_FILTER_EXECUTION);
		if (valString != null) {
			this.logFilterExecution = Boolean.parseBoolean(valString);
		} else {
			LOG.warn("Filter configuration '"
					+ CONFIG_PROPERTY_NAME_LOG_FILTER_EXECUTION
					+ "' not set. Using the value: " + this.logFilterExecution);
		}
	}

	/**
	 * Register thread-local session and trace information, executes the given {@link FilterChain} and unregisters
	 * the session/trace information. If configured, the execution of this filter is also logged to the {@link IMonitoringController}.
	 * This method returns immediately if monitoring is not enabled.
	 */
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		if (!MONITORING_CTRL.isMonitoringEnabled()) {
			chain.doFilter(request, response);
			return;
		}

		/*
		 * Register session information which needs to be reset after the chain has been executed.
		 */
		String sessionId = this.registerSessionInformation(request); // {@link OperationExecutionRecord#NO_SESSION_ID} if no session ID
		long traceId = OperationExecutionRecord.NO_TRACEID; // note that we must NOT register anything to the CF_REGISTRY here!

		/*
		 * If this filter execution shall be part of the traced control flow, we need to register some control flow information.
		 */
		if (this.logFilterExecution) {
			traceId = CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CF_REGISTRY.storeThreadLocalEOI(0); // current execution's eoi is 0
			CF_REGISTRY.storeThreadLocalESS(1); // *current* execution's ess is 0; next execution is at stack depth 1
		}

		final long tin = TIMESOURCE.getTime(); // the entry timestamp
		try {
			chain.doFilter(request, response);
		} finally {
			SESSION_REGISTRY.unsetThreadLocalSessionId();
			if (this.logFilterExecution) {
				final long tout = TIMESOURCE.getTime();
				// if sessionId == null, try again to fetch it (should exist after being within the application logic)
				if (sessionId == OperationExecutionRecord.NO_SESSION_ID) { // yes, == and not equals
					sessionId = this.registerSessionInformation(request);
				}

				/*
				 * Log this execution
				 */
				MONITORING_CTRL.newMonitoringRecord(
						new OperationExecutionRecord(this.getFilterOperationSignatureString(), sessionId, traceId, tin, tout,
								VM_NAME, 0, 0)); // 0,0 state that this method is the application entry point

				/*
				 * Reset the thread-local trace information
				 */
				CF_REGISTRY.unsetThreadLocalTraceId();
				CF_REGISTRY.unsetThreadLocalEOI();
				CF_REGISTRY.unsetThreadLocalESS();
			}
		}
	}

	public void destroy() {
		// by default, we do nothing here. Extending classes may override this method
	}

	/**
	 * If the given {@link ServletRequest} is an instance of {@link HttpServletRequest}, this
	 * methods extracts the session ID and registers it in the {@link #SESSION_REGISTRY} in
	 * order to be accessible for other probes in this thread. In case no session
	 * is associated with this request (or if the request is not an instance of {@link HttpServletRequest}),
	 * this method returns without any further actions and returns {@link OperationExecutionRecord#NO_SESSION_ID}.
	 * 
	 * @param request
	 */
	protected String registerSessionInformation(final ServletRequest request) {
		String sessionId = OperationExecutionRecord.NO_SESSION_ID;

		if ((request == null) || !(request instanceof HttpServletRequest)) {
			return sessionId;
		}

		if (request instanceof HttpServletRequest) {
			final HttpSession session = ((HttpServletRequest) request).getSession(false);
			if (session != null) {
				sessionId = session.getId();
				SESSION_REGISTRY.storeThreadLocalSessionId(sessionId);
			}
		}

		return sessionId;
	}
}
