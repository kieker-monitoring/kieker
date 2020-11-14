/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.trace.analysis.tt;

import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.MessageTrace;

/**
 * This is an abstract base for components which process message traces.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- teetime port
 *
 * @since 1.1
 */
public abstract class AbstractMessageTraceProcessingFilter extends AbstractTraceProcessingFilter<MessageTrace> {

	/** The name of the input port receiving the message traces. */
	public static final String INPUT_PORT_NAME_MESSAGE_TRACES = "messageTraces";

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public AbstractMessageTraceProcessingFilter(final SystemModelRepository repository) {
		super(repository);
	}

	@Override
	protected abstract void execute(MessageTrace element) throws Exception;
}
