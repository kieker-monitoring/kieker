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

	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.getInstance();
	protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.getInstance();

	public OperationExecutionWebRequestRegistrationInterceptor() {
		// nothing to do
	}

	@Override
	public void preHandle(final WebRequest request) throws Exception {
		OperationExecutionWebRequestRegistrationInterceptor.CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
		OperationExecutionWebRequestRegistrationInterceptor.SESSION_REGISTRY.storeThreadLocalSessionId(request.getSessionId());
		OperationExecutionWebRequestRegistrationInterceptor.CF_REGISTRY.storeThreadLocalEOI(0);
		OperationExecutionWebRequestRegistrationInterceptor.CF_REGISTRY.storeThreadLocalESS(1);
	}

	@Override
	public void postHandle(final WebRequest request, final ModelMap map) throws Exception {
		OperationExecutionWebRequestRegistrationInterceptor.CF_REGISTRY.unsetThreadLocalTraceId();
		OperationExecutionWebRequestRegistrationInterceptor.SESSION_REGISTRY.unsetThreadLocalSessionId();
		OperationExecutionWebRequestRegistrationInterceptor.CF_REGISTRY.unsetThreadLocalEOI();
		OperationExecutionWebRequestRegistrationInterceptor.CF_REGISTRY.unsetThreadLocalESS();
	}

	@Override
	public void afterCompletion(final WebRequest request, final Exception map) throws Exception {
		// nothing to do
	}
}
