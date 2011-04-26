package kieker.monitoring.probe.aspectJ;

import java.util.concurrent.ConcurrentHashMap;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Jan Waller
 */
@Aspect
public class DeactivatedProbeAnnotation extends AbstractAspectJProbe {
	private static final Log log = LogFactory.getLog(DeactivatedProbeAnnotation.class);
	protected static final IMonitoringController ctrlInst = MonitoringController.getInstance();

	private static final Object dummy = new Object();
	private static final ConcurrentHashMap<String, Object> deactivatedProbes = new ConcurrentHashMap<String, Object>();
	{
		deactivatedProbes.put("asdfasfg", dummy);
		deactivatedProbes.put("xcasdgsdfahsdfh", dummy);
		deactivatedProbes.put("long kieker.evaluation.monitoredApplication.MonitoredClass.monitoredMethod(long, int)", dummy);
	}

	@Pointcut("execution(@kieker.monitoring.annotation.BenchmarkProbe * *.*(..))")
	public void monitoredMethod() {
	}

	@Around("monitoredMethod() && notWithinKieker()")
	public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
		if (!ctrlInst.isMonitoringEnabled() || deactivatedProbes.containsKey(thisJoinPoint.getStaticPart().getSignature().toString())) {
			return thisJoinPoint.proceed();
		}
		Object retVal = null;
		// Measure
		log.fatal("Probe got accidently activated!!!");
		try {
			retVal = thisJoinPoint.proceed();
		} catch (final Exception e) {
			throw e; // exceptions are forwarded
		} finally {
			// Measure
		}
		return retVal;
	}
}
