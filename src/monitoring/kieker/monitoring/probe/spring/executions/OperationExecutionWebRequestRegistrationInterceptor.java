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

package kieker.monitoring.probe.spring.executions;

import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @author Andre van Hoorn
 */
public class OperationExecutionWebRequestRegistrationInterceptor implements WebRequestInterceptor {

	protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
	protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();

	@Override
	public void preHandle(final WebRequest request) throws Exception {
		OperationExecutionWebRequestRegistrationInterceptor.cfRegistry.getAndStoreUniqueThreadLocalTraceId();
		OperationExecutionWebRequestRegistrationInterceptor.sessionRegistry.storeThreadLocalSessionId(request.getSessionId());
		OperationExecutionWebRequestRegistrationInterceptor.cfRegistry.storeThreadLocalEOI(0);
		OperationExecutionWebRequestRegistrationInterceptor.cfRegistry.storeThreadLocalESS(1);
	}

	@Override
	public void postHandle(final WebRequest request, final ModelMap map) throws Exception {
		OperationExecutionWebRequestRegistrationInterceptor.cfRegistry.unsetThreadLocalTraceId();
		OperationExecutionWebRequestRegistrationInterceptor.sessionRegistry.unsetThreadLocalSessionId();
		OperationExecutionWebRequestRegistrationInterceptor.cfRegistry.unsetThreadLocalEOI();
		OperationExecutionWebRequestRegistrationInterceptor.cfRegistry.unsetThreadLocalESS();
	}

	@Override
	public void afterCompletion(final WebRequest request, final Exception map) throws Exception {}
}
