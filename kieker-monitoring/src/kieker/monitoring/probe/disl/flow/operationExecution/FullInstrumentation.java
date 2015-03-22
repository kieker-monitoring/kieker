/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.disl.flow.operationExecution;

import ch.usi.dag.disl.annotation.AfterReturning;
import ch.usi.dag.disl.annotation.AfterThrowing;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 * 
 * @since 1.9
 */
public class FullInstrumentation { // NOPMD NOCS (disl class)
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	@SyntheticLocal
	private static boolean newTrace;

	@SyntheticLocal
	private static long traceId;

	@SyntheticLocal
	private static String signature;

	@SyntheticLocal
	private static String clazz;

	@SyntheticLocal
	private static TraceMetadata trace;

	@Before(marker = BodyMarker.class, scope = "")
	public static void onMethodEntry(final MethodStaticContext msc, final DynamicContext dc) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		signature = msc.thisMethodFullName();
		if (!CTRLINST.isProbeActivated(signature)) {
			return;
		}
		// common fields
		trace = TRACEREGISTRY.getTrace();
		newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		traceId = trace.getTraceId();
		clazz = dc.getThis().getClass().toString();
		// measure before execution
		CTRLINST.newMonitoringRecord(new BeforeOperationEvent(TIME.getTime(), traceId, trace.getNextOrderId(), signature, clazz));
	}

	@AfterReturning(marker = BodyMarker.class, scope = "")
	public static void onMethodExit(final MethodStaticContext msc) {
		if (!CTRLINST.isMonitoringEnabled() || !CTRLINST.isProbeActivated(signature)) {
			return;
		}
		if (newTrace) { // close the trace
			TRACEREGISTRY.unregisterTrace();
		}
		// measure after successful execution
		CTRLINST.newMonitoringRecord(new AfterOperationEvent(TIME.getTime(), traceId, trace.getNextOrderId(), signature, clazz));
	}

	@AfterThrowing(marker = BodyMarker.class, scope = "")
	public static void onMethodException(final MethodStaticContext msc, final DynamicContext dc) {
		if (!CTRLINST.isMonitoringEnabled() || !CTRLINST.isProbeActivated(signature)) {
			return;
		}
		if (newTrace) { // close the trace
			TRACEREGISTRY.unregisterTrace();
		}
		// measure after failed execution
		CTRLINST.newMonitoringRecord(new AfterOperationFailedEvent(TIME.getTime(), traceId, trace.getNextOrderId(), signature, clazz, dc.getException().toString()));
	}
}
