package kieker.monitoring.probe.aspectJ;

import java.util.concurrent.ConcurrentHashMap;

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
public class DeactivatedProbeAnnotation extends AbstractAspectJProbe {
	private static final Log log = LogFactory.getLog(DeactivatedProbeAnnotation.class);
	protected static final MonitoringController ctrlInst = MonitoringController.getInstance();
	
	private static final Object dummy = new Object();
	private static final ConcurrentHashMap<String,Object> deactivatedProbes = new ConcurrentHashMap<String,Object>();
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
		if ( !ctrlInst.isMonitoringEnabled()
		   || deactivatedProbes.containsKey(thisJoinPoint.getStaticPart().getSignature().toString()) ) {
			return thisJoinPoint.proceed();
		}
		Object retVal = null;
		// Measure
		log.fatal("Probe got accidently activated!!!");
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
