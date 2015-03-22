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

package kieker.monitoring.probe.aspectj.flow.construction;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.ConstructionEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	/**
	 * The pointcut for the monitored constructors. Inheriting classes should extend the pointcut in order to find the correct constructors (e.g. all constructors or
	 * only constructors with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredConstructor();

	/**
	 * This is an advice which will be used after the construction of an object.
	 *
	 * @param thisObject
	 * @param jp
	 *            The static information about this joint point.
	 */
	// HINT: This may be logged multiple times due to super constructor calls...
	@AfterReturning("monitoredConstructor() && this(thisObject) && notWithinKieker()")
	public void afterConstruction(final Object thisObject, final JoinPoint.StaticPart jp) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final Signature signature = jp.getSignature();
		if (!CTRLINST.isProbeActivated(this.signatureToLongString(signature))) {
			return;
		}
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		final ConstructionEvent crecord = new ConstructionEvent(TIME.getTime(), trace.getTraceId(), trace.getNextOrderId(), signature.getDeclaringTypeName(),
				System.identityHashCode(thisObject));
		CTRLINST.newMonitoringRecord(crecord);
	}
}
