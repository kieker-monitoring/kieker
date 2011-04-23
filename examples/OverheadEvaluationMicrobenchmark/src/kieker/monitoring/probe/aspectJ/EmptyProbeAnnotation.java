package kieker.monitoring.probe.aspectJ;

import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.probe.aspectJ.AbstractAspectJProbe;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
@Aspect
public class EmptyProbeAnnotation extends AbstractAspectJProbe {
	private static final Log log = LogFactory.getLog(EmptyProbeAnnotation.class);
	protected static final MonitoringController ctrlInst = MonitoringController.getInstance();
  
	@Pointcut("execution(@kieker.monitoring.annotation.BenchmarkProbe * *.*(..))")
	public void monitoredMethod() {
	}

	@Around("monitoredMethod() && notWithinKieker()")
	public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
		if (!ctrlInst.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		Object retVal = null;
		// Measure
		try {
      retVal =  thisJoinPoint.proceed();
    } catch (final Exception e) {
      throw e; // exceptions are forwarded
    } finally {
  		// Measure
    }
    return retVal;
	}
}
