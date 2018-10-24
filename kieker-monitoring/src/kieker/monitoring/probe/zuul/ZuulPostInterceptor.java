package kieker.monitoring.probe.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.spring.flow.ThreadSpecificInterceptedData;
import kieker.monitoring.timer.ITimeSource;

public class ZuulPostInterceptor extends ZuulFilter {

	public static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

	private static final Log LOG = LogFactory.getLog(ZuulPostInterceptor.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final String VMNAME = CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;	
	
	@Override
	public boolean shouldFilter() {
		// TODO restrict this
		return true;
	}

	@Override
	public Object run() {
		// measure after
		final long tout = TIME.getTime();
		RequestContext ctx = RequestContext.getCurrentContext();		
		ThreadSpecificInterceptedData data = (ThreadSpecificInterceptedData) ctx.get("KiekerInfo");
		String sessionId = data.getSessionId();
		long traceId = data.getTraceId();
		long tin = data.getTin();
		String hostname = data.getHostname();
		int eoi = data.getEoi();
		int ess = data.getEss();
		CTRLINST.newMonitoringRecord(new OperationExecutionRecord(data.getSignature(), sessionId, traceId, tin, tout, hostname, eoi, ess));
		CF_REGISTRY.unsetThreadLocalTraceId();
		SESSION_REGISTRY.unsetThreadLocalSessionId();
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return Integer.MAX_VALUE;
	}

}
