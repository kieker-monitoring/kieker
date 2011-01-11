package kieker.monitoring.probe.aspectJ;

import kieker.monitoring.core.MonitoringController;
import kieker.monitoring.probe.aspectJ.AbstractAspectJProbe;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
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
 * ==================================================
 */

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
