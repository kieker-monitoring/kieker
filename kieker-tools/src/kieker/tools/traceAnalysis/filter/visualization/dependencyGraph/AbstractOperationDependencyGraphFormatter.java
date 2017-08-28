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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.common.util.signature.Signature;
import kieker.tools.traceAnalysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 * Abstract for formatters for operation-level dependency graph.
 * 
 * @author Holger Knoche
 * 
 * @param <T>
 *            The type of graph this formatter is for
 * 
 * @since 1.6
 */
public abstract class AbstractOperationDependencyGraphFormatter<T extends AbstractDependencyGraph<?>> extends AbstractDependencyGraphFormatter<T> {

	private String createOperationSignature(final Operation operation) {
		final StringBuilder builder = new StringBuilder();
		final Signature signature = operation.getSignature();

		builder.append(signature.getName());
		builder.append('(');

		final String[] parameterTypes = signature.getParamTypeList();
		if (parameterTypes.length > 0) { // // parameterTypes cannot be null (getParamTypeList never returns null)
			builder.append("..");
		}
		builder.append(')');

		return builder.toString();
	}

	protected String createOperationNodeLabel(final Operation operation, final DependencyGraphNode<?> node) {
		final StringBuilder builder = new StringBuilder();

		builder.append(this.createOperationSignature(operation));
		AbstractGraphFormatter.formatDecorations(builder, node);

		return builder.toString();
	}

}
