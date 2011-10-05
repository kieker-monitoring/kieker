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

package kieker.monitoring.probe.aspectJ.operationExecution;

import javax.servlet.http.HttpServletRequest;

import kieker.monitoring.core.registry.SessionRegistry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Andre van Hoorn
 */
@Aspect
public abstract class AbstractOperationExecutionAspectServlet extends AbstractOperationExecutionAspect {

	protected static final SessionRegistry SESSIONREGISTRY = SessionRegistry.getInstance();

	public Object doServletEntryProfiling(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
		final HttpServletRequest req = (HttpServletRequest) thisJoinPoint.getArgs()[0];
		final String sessionId = (req != null) ? req.getSession(true).getId() : null; // NOPMD
		Object retVal = null; // NOPMD
		AbstractOperationExecutionAspectServlet.SESSIONREGISTRY.storeThreadLocalSessionId(sessionId);
		try {
			// does pass the args!
			retVal = thisJoinPoint.proceed(); // NOPMD
		} catch (final Exception exc) { // NOPMD
			throw exc;
		} finally {
			AbstractOperationExecutionAspectServlet.SESSIONREGISTRY.unsetThreadLocalSessionId();
		}
		return retVal;
	}
}
