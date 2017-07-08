package kieker.monitoring.probe.aspectj.beforeafter.onlycallee;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	private final Map<JoinPoint, TraceMetadata> entryJoinPoints = new ConcurrentHashMap<>();

	/**
	 * The pointcut for the monitored operations. Inheriting classes should
	 * extend the pointcut in order to find the correct executions of the
	 * methods (e.g. all methods or only methods with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Before("monitoredOperation() && target(calleeObject) && notWithinKieker()")
	public void beforeOperation(final JoinPoint thisJoinPoint, final Object calleeObject) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace(); // TODO parent trace is never used, so reduce impl. (chw)
			CTRLINST.newMonitoringRecord(trace);
			this.entryJoinPoints.put(thisJoinPoint, trace);
		}

		final long traceId = trace.getTraceId();
		final String clazz = calleeObject.getClass().getName();
		// measure before execution
		CTRLINST.newMonitoringRecord(
				new BeforeOperationEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz));
	}

	// @Around("monitoredOperation() && notWithinKieker()")
	// public Object aroundOperation(ProceedingJoinPoint jp) throws Throwable {
	// TraceMetadata trace = TRACEREGISTRY.getTrace();
	// final boolean newTrace = trace == null;
	// if (newTrace) {
	// trace = TRACEREGISTRY.registerTrace();
	// CTRLINST.newMonitoringRecord(trace);
	// }
	//
	// try {
	// return jp.proceed();
	// } catch (Throwable th) {
	// throw th;
	// } finally {
	// if (newTrace) { // close the trace
	// TRACEREGISTRY.unregisterTrace();
	// }
	// }
	// }

	@AfterReturning("monitoredOperation() && target(calleeObject) && notWithinKieker()")
	public void afterOperation(final JoinPoint thisJoinPoint, Object calleeObject) {
		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		final String clazz = calleeObject.getClass().getName();

		CTRLINST.newMonitoringRecord(new AfterOperationEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz));
	}

	@AfterThrowing(pointcut = "monitoredOperation() && target(calleeObject) && notWithinKieker()", throwing = "th")
	public void afterThrowing(final JoinPoint thisJoinPoint, Object calleeObject, Throwable th) {
		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		final String clazz = calleeObject.getClass().getName();

		CTRLINST.newMonitoringRecord(new AfterOperationFailedEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, clazz, th.toString()));
	}

	@After("monitoredOperation() && notWithinKieker()")
	public void afterOperation(final JoinPoint thisJoinPoint) {
		if (this.entryJoinPoints.remove(thisJoinPoint) != null) {
			TRACEREGISTRY.unregisterTrace();
		}
	}
}
