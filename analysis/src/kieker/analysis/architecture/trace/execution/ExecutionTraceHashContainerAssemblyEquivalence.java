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

package kieker.analysis.architecture.trace.execution;

import java.util.Iterator;

import kieker.model.system.model.Execution;
import kieker.model.system.model.ExecutionTrace;

/**
 *
 * @author Andre van Hoorn
 *
 * @since 1.2
 */
class ExecutionTraceHashContainerAssemblyEquivalence extends AbstractExecutionTraceHashContainer {

	private final int hashCodeBuffer;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param t
	 *            The execution trace to be stored in this container.
	 */
	public ExecutionTraceHashContainerAssemblyEquivalence(final ExecutionTrace t) {
		super(t);
		final int prime = 31;
		int result = 1;
		for (final Execution r : t.getTraceAsSortedExecutionSet()) {
			result = (prime * result) + r.getOperation().getId();
			result = (prime * result) + r.getAllocationComponent().getAssemblyComponent().getId();
			result = (prime * result) + r.getEoi();
			result = (prime * result) + r.getEss();
		}
		//
		this.hashCodeBuffer = result;
	}

	/**
	 * Checks whether the given executions are equal or not.
	 *
	 * @param r1
	 *            The first execution object.
	 * @param r2
	 *            The second execution object.
	 *
	 * @return true if and only if the executions have the same values.
	 */
	private boolean executionsEqual(final Execution r1, final Execution r2) {
		if (r1 == r2) { // NOPMD (no equals)
			return true;
		}
		if ((r1 == null) || (r2 == null)) {
			return false;
		}
		return (r1.getAllocationComponent().getAssemblyComponent().getId() == r2.getAllocationComponent().getAssemblyComponent().getId())
				&& (r1.getOperation().getId() == r2.getOperation().getId()) && (r1.getEoi() == r2.getEoi()) && (r1.getEss() == r2.getEss());
	}

	@Override
	public int hashCode() {
		return this.hashCodeBuffer;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || !(obj instanceof AbstractExecutionTraceHashContainer)) {
			return false;
		}
		final ExecutionTrace otherTrace = ((AbstractExecutionTraceHashContainer) obj).getExecutionTrace();
		if (super.getExecutionTrace().getLength() != otherTrace.getLength()) {
			return false;
		}
		final Iterator<Execution> otherIterator = otherTrace.getTraceAsSortedExecutionSet().iterator();
		for (final Execution r1 : super.getExecutionTrace().getTraceAsSortedExecutionSet()) {
			final Execution r2 = otherIterator.next();
			if (!this.executionsEqual(r1, r2)) {
				return false;
			}
		}
		return true;
	}
}
