/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.aspectj.beforeafter.onlycallee;

import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.thread.AfterFailedThreadBasedEvent;
import kieker.common.record.flow.thread.AfterThreadBasedEvent;
import kieker.common.record.flow.thread.BeforeThreadBasedEvent;
import kieker.common.record.misc.ThreadMetaData;
import kieker.monitoring.IdGenerator;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * This aspect spawns before and after events by foregoing an around advice. Instead, it uses before and after advices
 * only so that "cflow" can be used when specifying its pointcut.
 *
 * This implementation uses <code>JoinPoint.StaticPart</code> instead of <code>JoinPoint</code> in the advices for
 * performance reasons: <blockquote>If you only need the static information about the join point, you may access the
 * static part of the join point directly with the special variable thisJoinPointStaticPart. Using
 * thisJoinPointStaticPart will avoid the run-time creation of the join point object that may be necessary when using
 * thisJoinPoint directly. </blockquote>
 *
 * This implementation avoids the usage of a trace id. Instead, it passes the thread id to the event records. The
 * combination of the before/after events, the thread id, and the order index is sufficient to reconstruct different
 * traces of the same thread.
 *
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 *
 */
@Aspect
public abstract class AbstractAspectWithoutTraceRegistry extends AbstractAspectJProbe { // NOPMD

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();

	private final IdGenerator idGenerator = new IdGenerator();
	private final ThreadLocal<Long> threadLocalId = new ThreadLocal<>();
	private final ThreadLocal<Counter> currentOrderIndex = new ThreadLocal<Counter>() {
		@Override
		protected Counter initialValue() {
			return new Counter();
		}
	};

	/**
	 * The pointcut for the monitored operations. Inheriting classes should extend the pointcut in order to find the
	 * correct executions of the methods (e.g. all methods or only methods with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Before("monitoredOperation() && notWithinKieker()")
	public void beforeOperation(final StaticPart jpStaticPart) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		Long threadId = this.threadLocalId.get();
		if (null == threadId) {
			threadId = this.idGenerator.getNewId();
			this.threadLocalId.set(threadId);

			// CTRLINST.newMonitoringRecord(new TraceMetadata(-1, threadId, "<NO SESSION ID>", CTRLINST.getHostname(), -1, -1));
			CTRLINST.newMonitoringRecord(new ThreadMetaData(CTRLINST.getHostname(), threadId));
		}

		final int orderIndex = this.currentOrderIndex.get().incrementValue();
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();

		CTRLINST.newMonitoringRecord(
				new BeforeThreadBasedEvent(TIME.getTime(), threadId, orderIndex, operationSignature, typeName));
	}

	@AfterReturning("monitoredOperation() && notWithinKieker()")
	public void afterReturningOperation(final StaticPart jpStaticPart) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final long threadId = this.threadLocalId.get();
		final int orderIndex = this.currentOrderIndex.get().incrementValue();
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();

		CTRLINST.newMonitoringRecord(
				new AfterThreadBasedEvent(TIME.getTime(), threadId, orderIndex, operationSignature, typeName));
	}

	@AfterThrowing(pointcut = "monitoredOperation() && notWithinKieker()", throwing = "th")
	public void afterThrowing(final StaticPart jpStaticPart, final Throwable th) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final long threadId = this.threadLocalId.get();
		final int orderIndex = this.currentOrderIndex.get().incrementValue();
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();

		CTRLINST.newMonitoringRecord(new AfterFailedThreadBasedEvent(TIME.getTime(), threadId, orderIndex,
				operationSignature, typeName, th.toString()));
	}

}
