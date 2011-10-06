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

package kieker.tools.traceAnalysis.systemModel;

import kieker.analysis.plugin.IAnalysisEvent;

/**
 * @author Andre van Hoorn
 */
public class InvalidExecutionTrace implements IAnalysisEvent {
	private final ExecutionTrace invalidExecutionTraceArtifacts;

	public ExecutionTrace getInvalidExecutionTraceArtifacts() {
		return this.invalidExecutionTraceArtifacts;
	}

	public InvalidExecutionTrace(final ExecutionTrace invalidExecutionTrace) {
		this.invalidExecutionTraceArtifacts = invalidExecutionTrace;
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
