/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.trace.traversal;

import java.util.Collection;
import java.util.Collections;

import kieker.model.analysismodel.trace.OperationCall;
import kieker.model.analysismodel.trace.Trace;

/**
 * A class that traverses {@link Trace}s along with one or multiple
 * {@link IOperationCallVisitor}s. It calls the visitor(s) for every
 * {@link OperationCall} within the trace.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TraceTraverser {

	// BETTER Make this generic for aggregated traces

	public TraceTraverser() {
		// Create an empty traverser
	}

	public void traverse(final Trace trace, final IOperationCallVisitor visitor) {
		final Collection<IOperationCallVisitor> visitors = Collections.singleton(visitor);
		this.handleOperationCallsRecursively(trace.getRootOperationCall(), visitors);
	}

	public void traverse(final Trace trace, final Collection<IOperationCallVisitor> visitors) {
		this.handleOperationCallsRecursively(trace.getRootOperationCall(), visitors);
	}

	private void handleOperationCallsRecursively(final OperationCall operationCall,
			final Collection<IOperationCallVisitor> visitors) {
		for (final IOperationCallVisitor visitor : visitors) {
			visitor.visit(operationCall);
		}

		for (final OperationCall childOperationCall : operationCall.getChildren()) {
			this.handleOperationCallsRecursively(childOperationCall, visitors);
		}
	}

}
