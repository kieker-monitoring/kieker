/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.flow.operationExecutionFlat;

import java.nio.ByteBuffer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.util.registry.IRegistry;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	private static final int MESSAGE_BUFFER_SIZE = 131072;

	private static final ThreadLocalByteBuffer bufferStore = new ThreadLocalByteBuffer(
			MESSAGE_BUFFER_SIZE);
	private static final IRegistry<String> stringRegistry = CTRLINST.getStringRegistry();

	private static final int traceMetadataStringId = stringRegistry.get(TraceMetadata.class.getName());
	private static final int beforeStringId = stringRegistry.get(BeforeOperationEvent.class.getName());
	private static final int afterStringId = stringRegistry.get(AfterOperationEvent.class.getName());

	/**
	 * The pointcut for the monitored operations. Inheriting classes should extend the pointcut in order to find the correct executions of the methods (e.g. all
	 * methods or only methods with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Around("monitoredOperation() && this(thisObject) && notWithinKieker()")
	public Object operation(final Object thisObject, final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}

		final ByteBuffer buffer = bufferStore.get();

		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			if ((TraceMetadata.SIZE + 12) > buffer.remaining()) {
				buffer.flip();
				final int max = buffer.limit();
				final byte[] messages = new byte[max];
				buffer.get(messages, 0, max);
				CTRLINST.newMonitoringRecord(messages);
				buffer.clear();
			}

			buffer.putInt(AbstractAspect.traceMetadataStringId);
			buffer.putLong(TIME.getTime());

			buffer.putLong(trace.getTraceId());
			buffer.putLong(trace.getThreadId());
			buffer.putInt(AbstractAspect.stringRegistry.get(trace.getSessionId()));
			buffer.putInt(AbstractAspect.stringRegistry.get(trace.getHostname()));
			buffer.putLong(trace.getParentTraceId());
			buffer.putInt(trace.getParentOrderId());
		}
		final long traceId = trace.getTraceId();
		final String clazz = thisObject.getClass().getName();
		// measure before execution

		if ((BeforeOperationEvent.SIZE + 12) > buffer.remaining()) {
			buffer.flip();
			final int max = buffer.limit();
			final byte[] messages = new byte[max];
			buffer.get(messages, 0, max);
			System.out.println("before");
			CTRLINST.newMonitoringRecord(messages);
			System.out.println("after");
			buffer.clear();
		}

		buffer.putInt(AbstractAspect.beforeStringId);
		final long time = TIME.getTime();
		buffer.putLong(time);
		buffer.putLong(time);
		buffer.putLong(traceId);
		buffer.putInt(trace.getNextOrderId());
		buffer.putInt(AbstractAspect.stringRegistry.get(signature));
		buffer.putInt(AbstractAspect.stringRegistry.get(clazz));

		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} catch (final Throwable th) { // NOPMD NOCS (catch throw might ok here)
			// measure after failed execution
			CTRLINST.newMonitoringRecord(new AfterOperationFailedEvent(TIME.getTime(), traceId, trace.getNextOrderId(), signature, clazz,
					th.toString()));
			throw th;
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		// measure after successful execution
		if ((AfterOperationEvent.SIZE + 12) > buffer.remaining()) {
			buffer.flip();
			final int max = buffer.limit();
			final byte[] messages = new byte[max];
			buffer.get(messages, 0, max);
			CTRLINST.newMonitoringRecord(messages);
			buffer.clear();
		}

		buffer.putInt(AbstractAspect.afterStringId);
		final long time2 = TIME.getTime();
		buffer.putLong(time2);
		buffer.putLong(time2);
		buffer.putLong(traceId);
		buffer.putInt(trace.getNextOrderId());
		buffer.putInt(AbstractAspect.stringRegistry.get(signature));
		buffer.putInt(AbstractAspect.stringRegistry.get(clazz));

		return retval;
	}

	@Around("monitoredOperation() && !this(java.lang.Object) && notWithinKieker()")
	public Object staticOperation(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final Signature sig = thisJoinPoint.getSignature();
		final String signature = this.signatureToLongString(sig);
		if (!CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}
		final ByteBuffer buffer = bufferStore.get();
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();

			if ((TraceMetadata.SIZE + 12) > buffer.remaining()) {
				buffer.flip();
				final int max = buffer.limit();
				final byte[] messages = new byte[max];
				buffer.get(messages, 0, max);
				CTRLINST.newMonitoringRecord(messages);
				buffer.clear();
			}

			buffer.putInt(AbstractAspect.traceMetadataStringId);
			buffer.putLong(TIME.getTime());

			buffer.putLong(trace.getTraceId());
			buffer.putLong(trace.getThreadId());
			buffer.putInt(AbstractAspect.stringRegistry.get(trace.getSessionId()));
			buffer.putInt(AbstractAspect.stringRegistry.get(trace.getHostname()));
			buffer.putLong(trace.getParentTraceId());
			buffer.putInt(trace.getParentOrderId());

		}
		final long traceId = trace.getTraceId();
		final String clazz = sig.getDeclaringTypeName();
		// measure before execution
		if ((BeforeOperationEvent.SIZE + 12) > buffer.remaining()) {
			buffer.flip();
			final int max = buffer.limit();
			final byte[] messages = new byte[max];
			buffer.get(messages, 0, max);
			CTRLINST.newMonitoringRecord(messages);
			buffer.clear();
		}
		buffer.putInt(AbstractAspect.beforeStringId);
		final long time = TIME.getTime();
		buffer.putLong(time);
		buffer.putLong(time);
		buffer.putLong(traceId);
		buffer.putInt(trace.getNextOrderId());
		buffer.putInt(AbstractAspect.stringRegistry.get(signature));
		buffer.putInt(AbstractAspect.stringRegistry.get(clazz));

		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} catch (final Throwable th) { // NOPMD NOCS (catch throw might ok here)
			// measure after failed execution
			CTRLINST.newMonitoringRecord(new AfterOperationFailedEvent(TIME.getTime(), traceId, trace.getNextOrderId(), signature, clazz,
					th.toString()));
			throw th;
		} finally {
			if (newTrace) { // close the trace
				TRACEREGISTRY.unregisterTrace();
			}
		}
		// measure after successful execution
		if ((AfterOperationEvent.SIZE + 12) > buffer.remaining()) {
			buffer.flip();
			final int max = buffer.limit();
			final byte[] messages = new byte[max];
			buffer.get(messages, 0, max);
			CTRLINST.newMonitoringRecord(messages);
			buffer.clear();
		}
		buffer.putInt(AbstractAspect.afterStringId);
		final long time2 = TIME.getTime();
		buffer.putLong(time2);
		buffer.putLong(time2);
		buffer.putLong(traceId);
		buffer.putInt(trace.getNextOrderId());
		buffer.putInt(AbstractAspect.stringRegistry.get(signature));
		buffer.putInt(AbstractAspect.stringRegistry.get(clazz));

		return retval;
	}
}
