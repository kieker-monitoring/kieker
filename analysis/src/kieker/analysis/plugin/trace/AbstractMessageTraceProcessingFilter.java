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
package kieker.analysis.plugin.trace;

import kieker.analysis.architecture.trace.AbstractTraceProcessingStage;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.MessageTrace;

/**
 * This is an abstract base for components which process message traces.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- teetime port
 *
 * @since 1.1
 * @deprecated since 1.15 the class is obsolete
 */
@Deprecated
public abstract class AbstractMessageTraceProcessingFilter extends AbstractTraceProcessingStage<MessageTrace> {

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param repository
	 *            model repository
	 */
	public AbstractMessageTraceProcessingFilter(final SystemModelRepository repository) {
		super(repository);
	}

	@Override
	protected abstract void execute(MessageTrace element) throws Exception;
}
