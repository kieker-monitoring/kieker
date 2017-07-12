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

/**
 * This aspect spawns before and after events by foregoing an around advice.
 * Instead, it uses before and after advices only so that "cflow" can be used when specifying its pointcut.
 * 
 * This implementation uses <code>JoinPoint.StaticPart</code> instead of <code>JoinPoint</code> in the advices for performance reasons:
 * <blockquote>If you only need the static information about the join point, you may access the static part of the join point directly with the special variable
 * thisJoinPointStaticPart.
 * Using thisJoinPointStaticPart will avoid the run-time creation of the join point object that may be necessary when using thisJoinPoint directly.
 * </blockquote>
 * 
 * @author Christian Wulf (chw)
 *
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	private final Map<JoinPoint.StaticPart, TraceMetadata> entryJoinPoints = new ConcurrentHashMap<>();

	/**
	 * The pointcut for the monitored operations. Inheriting classes should
	 * extend the pointcut in order to find the correct executions of the
	 * methods (e.g. all methods or only methods with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Before("monitoredOperation() && notWithinKieker()")
	public void beforeOperation(final JoinPoint.StaticPart jpStaticPart) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace(); // TODO parent trace is never used, so reduce impl. (chw)
			CTRLINST.newMonitoringRecord(trace);
			this.entryJoinPoints.put(jpStaticPart, trace);
		}

		final long traceId = trace.getTraceId();
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();
		// measure before execution
		CTRLINST.newMonitoringRecord(
				new BeforeOperationEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, typeName));
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

	@AfterReturning("monitoredOperation() && notWithinKieker()")
	public void afterReturningOperation(final JoinPoint.StaticPart jpStaticPart) {
		System.out.println("AbstractAspect.afterReturningOperation()");
		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		// this check indirectly includes the checks for isMonitoringEnabled and isProbeActivated
		if (trace == null) {
			return;
		}

		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();

		CTRLINST.newMonitoringRecord(new AfterOperationEvent(TIME.getTime(), trace.getTraceId(), trace.getNextOrderId(), operationSignature, typeName));
	}

	@AfterThrowing(pointcut = "monitoredOperation() && notWithinKieker()", throwing = "th")
	public void afterThrowing(final JoinPoint.StaticPart jpStaticPart, Throwable th) {
		System.out.println("AbstractAspect.afterThrowing()");
		final TraceMetadata trace = TRACEREGISTRY.getTrace();
		// this check indirectly includes the checks for isMonitoringEnabled and isProbeActivated
		if (trace == null) {
			return;
		}

		final String operationSignature = this.signatureToLongString(jpStaticPart.getSignature());
		final String typeName = jpStaticPart.getSignature().getDeclaringTypeName();

		CTRLINST.newMonitoringRecord(
				new AfterOperationFailedEvent(TIME.getTime(), trace.getTraceId(), trace.getNextOrderId(), operationSignature, typeName, th.toString()));
	}

	@After("monitoredOperation() && notWithinKieker()")
	public void afterOperation(final JoinPoint.StaticPart jpStaticPart) {
		System.out.println("AbstractAspect.afterOperation()");
		if (this.entryJoinPoints.remove(jpStaticPart) != null) {
			TRACEREGISTRY.unregisterTrace();
		}
	}
}
