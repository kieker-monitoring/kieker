/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import kieker.monitoring.core.registry.SessionRegistry;

/**
 * @author Andre van Hoorn
 * 
 * @since 0.9
 */
public class OperationExecutionWebRequestRegistrationInterceptor implements WebRequestInterceptor {

	/** Stores the singleton instance of the session registry. */
	protected static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

	// protected static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;

	/**
	 * Creates a new instance of this class.
	 */
	public OperationExecutionWebRequestRegistrationInterceptor() {
		// nothing to do
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preHandle(final WebRequest request) throws Exception {
		SESSION_REGISTRY.storeThreadLocalSessionId(request.getSessionId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void postHandle(final WebRequest request, final ModelMap map) throws Exception {
		SESSION_REGISTRY.unsetThreadLocalSessionId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterCompletion(final WebRequest request, final Exception map) throws Exception {
		// nothing to do
	}
}
