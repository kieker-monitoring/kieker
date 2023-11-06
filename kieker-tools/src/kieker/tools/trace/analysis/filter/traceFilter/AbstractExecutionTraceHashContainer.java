/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.filter.traceFilter;

import kieker.tools.trace.analysis.systemModel.ExecutionTrace;

/**
 * @author Andre van Hoorn
 *
 * @since 1.2
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
abstract class AbstractExecutionTraceHashContainer { // NOPMD (abstract class without abstract methods)

	private final ExecutionTrace executionTrace;

	/**
	 * Abstract constructor to initialize the container.
	 *
	 * @param t
	 *            The execution trace to be stored in this container.
	 */
	public AbstractExecutionTraceHashContainer(final ExecutionTrace t) {
		this.executionTrace = t;
	}

	/**
	 * Delivers the stored execution trace.
	 *
	 * @return The content of this container.
	 */
	public ExecutionTrace getExecutionTrace() {
		return this.executionTrace;
	}
}
