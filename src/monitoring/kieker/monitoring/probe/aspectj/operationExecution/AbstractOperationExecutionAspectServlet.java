/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.monitoring.probe.aspectj.operationExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.SessionRegistry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Andre van Hoorn, Jan Waller
 */
@Aspect
public abstract class AbstractOperationExecutionAspectServlet extends AbstractOperationExecutionAspect {

	private static final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();

	@Pointcut
	public abstract void monitoredServlet(final HttpServletRequest request, final HttpServletResponse response);

	@Around("monitoredServlet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) && notWithinKieker()")
	public Object servlet(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final HttpServletRequest req = (HttpServletRequest) thisJoinPoint.getArgs()[0];
		final String sessionId = (req != null) ? req.getSession(true).getId() : null; // NOPMD (assign null)
		SESSIONREGISTRY.storeThreadLocalSessionId(sessionId);
		Object retVal;
		try {
			retVal = thisJoinPoint.proceed();
		} finally {
			SESSIONREGISTRY.unsetThreadLocalSessionId();
		}
		return retVal;
	}
}
