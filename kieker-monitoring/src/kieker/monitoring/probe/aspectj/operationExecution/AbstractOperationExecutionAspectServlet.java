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

package kieker.monitoring.probe.aspectj.operationExecution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.SessionRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
@Aspect
public abstract class AbstractOperationExecutionAspectServlet extends AbstractOperationExecutionAspect {

	private static final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();

	@Pointcut
	public abstract void monitoredServlet(final HttpServletRequest request, final HttpServletResponse response);

	@Around("monitoredServlet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) && notWithinKieker()")
	public Object servlet(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!AbstractOperationExecutionAspectServlet.CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		if (!AbstractOperationExecutionAspectServlet.CTRLINST
				.isProbeActivated(this.signatureToLongString(thisJoinPoint.getSignature()))) {
			return thisJoinPoint.proceed();
		}
		final HttpServletRequest req = (HttpServletRequest) thisJoinPoint.getArgs()[0];
		final String sessionId = (req != null) ? req.getSession(true).getId() : null; // NOPMD,NOCS (assign null)
		AbstractOperationExecutionAspectServlet.SESSIONREGISTRY.storeThreadLocalSessionId(sessionId);
		final Object retVal;
		try {
			retVal = thisJoinPoint.proceed();
		} finally {
			AbstractOperationExecutionAspectServlet.SESSIONREGISTRY.unsetThreadLocalSessionId();
		}
		return retVal;
	}
}
