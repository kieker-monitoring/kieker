/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.systemModel;

/**
 * This object represents an somehow invalid trace of executions.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.2
 */
public class InvalidExecutionTrace {

	private final ExecutionTrace invalidExecutionTraceArtifacts;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param invalidExecutionTrace
	 *            The execution trace which will be wrapped as invalid.
	 */
	public InvalidExecutionTrace(final ExecutionTrace invalidExecutionTrace) {
		this.invalidExecutionTraceArtifacts = invalidExecutionTrace;
	}

	/**
	 * Delivers the wrapped execution trace.
	 * 
	 * @return The execution trace artifacts.
	 */
	public ExecutionTrace getInvalidExecutionTraceArtifacts() {
		return this.invalidExecutionTraceArtifacts;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder("Invalid Trace: ");
		strBuild.append(this.invalidExecutionTraceArtifacts.toString());
		return strBuild.toString();
	}

	@Override
	public int hashCode() {
		return ((this.invalidExecutionTraceArtifacts == null) ? 0 : this.invalidExecutionTraceArtifacts.hashCode()); // NOCS
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof InvalidExecutionTrace)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		return ((InvalidExecutionTrace) obj).getInvalidExecutionTraceArtifacts().equals(this.invalidExecutionTraceArtifacts);
	}
}
