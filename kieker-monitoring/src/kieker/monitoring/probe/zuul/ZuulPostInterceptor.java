/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.spring.flow.ThreadSpecificInterceptedData;
import kieker.monitoring.timer.ITimeSource;

/**
 *
 * @author Christian Stier
 *
 * @since 1.15
 */
public class ZuulPostInterceptor extends ZuulFilter {

	public static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();

	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

	/** default constructor. */
	public ZuulPostInterceptor() {
		super();
	}

	@Override
	public boolean shouldFilter() {
		// TODO restrict this
		return true;
	}

	@Override
	public Object run() {
		// measure after
		final long tout = TIME.getTime();
		final RequestContext ctx = RequestContext.getCurrentContext();
		final ThreadSpecificInterceptedData data = (ThreadSpecificInterceptedData) ctx.get("KiekerInfo");
		final String sessionId = data.getSessionId();
		final long traceId = data.getTraceId();
		final long tin = data.getTin();
		final String hostname = data.getHostname();
		final int eoi = data.getEoi();
		final int ess = data.getEss();
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
