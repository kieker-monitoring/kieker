/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.model;

import java.util.function.Function;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.util.ComposedKey;

/**
 * Utility class for functions ({@link Function}) to access the model objects from operation calls.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public final class ModelObjectFromOperationCallAccessors {

	public static final Function<OperationCall, Object> DEPLOYED_OPERATION = c -> c.getOperation();

	public static final Function<OperationCall, Object> DEPLOYED_COMPONENT = c -> c.getOperation().getComponent();

	public static final Function<OperationCall, Object> DEPLOYMENT_CONTEXT = c -> c.getOperation().getComponent().getDeploymentContext();

	public static final Function<OperationCall, Object> ASSEMBLY_OPERATION = c -> c.getOperation().getAssemblyOperation();

	public static final Function<OperationCall, Object> ASSEMBLY_COMPONENT = c -> c.getOperation().getAssemblyOperation().getAssemblyComponent();

	public static final Function<OperationCall, Object> OPERATION_TYPE = c -> c.getOperation().getAssemblyOperation().getOperationType();

	public static final Function<OperationCall, Object> COMPONENT_TYPE = c -> c.getOperation().getAssemblyOperation().getOperationType().getComponentType();

	private ModelObjectFromOperationCallAccessors() {}

	public static final Function<OperationCall, Object> createForAggregatedInvocation(final ExecutionModel executionModel) {
		return operationCall -> {
			// Check if operationCall is an entry operation call. If so than source is null
			final DeployedOperation source = operationCall.getParent() != null ? operationCall.getParent().getOperation() : null; // NOCS (declarative)
			final DeployedOperation target = operationCall.getOperation();
			final ComposedKey<DeployedOperation, DeployedOperation> key = ComposedKey.of(source, target);
			return executionModel.getAggregatedInvocations().get(key);
		};
	}

}
