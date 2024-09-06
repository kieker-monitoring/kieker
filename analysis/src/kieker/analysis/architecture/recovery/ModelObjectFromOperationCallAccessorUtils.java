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

package kieker.analysis.architecture.recovery;

import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.recovery.events.DeployedOperationCallEvent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.trace.OperationCall;

/**
 * Utility class for functions ({@link Function}) to access the model objects from operation calls.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public final class ModelObjectFromOperationCallAccessorUtils {

	public static final Function<DeployedOperationCallEvent, EObject> DEPLOYED_OPERATION = c -> c.getOperationCall().getSecond();

	public static final Function<OperationCall, EObject> DEPLOYED_COMPONENT = c -> c.getOperation().getComponent();

	public static final Function<OperationCall, EObject> DEPLOYMENT_CONTEXT = c -> c.getOperation().getComponent().getContext();

	public static final Function<OperationCall, EObject> ASSEMBLY_OPERATION = c -> c.getOperation().getAssemblyOperation();

	public static final Function<OperationCall, EObject> ASSEMBLY_COMPONENT = c -> c.getOperation().getAssemblyOperation().getComponent();

	public static final Function<OperationCall, EObject> OPERATION_TYPE = c -> c.getOperation().getAssemblyOperation().getOperationType();

	public static final Function<OperationCall, EObject> COMPONENT_TYPE = c -> c.getOperation().getAssemblyOperation().getOperationType().getComponentType();

	private ModelObjectFromOperationCallAccessorUtils() {}

	/**
	 * Get corresponding aggregated invocation from the execution model for a given OperationCall.
	 *
	 * @param executionModel
	 *            the execution model to be used.
	 * @return an aggregated invocation
	 */
	public static final Function<OperationCall, EObject> findAggregatedInvocation4OperationCall(final ExecutionModel executionModel) {
		return operationCall -> {
			// Check if operationCall is an entry operation call. If so than source is null
			final DeployedOperation source = operationCall.getParent() != null ? operationCall.getParent().getOperation() : null; // NOCS (declarative)
			final DeployedOperation target = operationCall.getOperation();
			final Tuple<DeployedOperation, DeployedOperation> key = ExecutionFactory.eINSTANCE.createTuple();
			key.setFirst(source);
			key.setSecond(target);
			return executionModel.getInvocations().get(key);
		};
	}

	/**
	 * Get corresponding aggregated invocation from the execution model for a given OperationCall.
	 *
	 * @param executionModel
	 *            the execution model to be used.
	 * @return an aggregated invocation
	 */
	public static final Function<DeployedOperationCallEvent, EObject> findAggregatedInvocation4OperationTuple(final ExecutionModel executionModel) {
		return operationCall -> {
			return executionModel.getInvocations().get(operationCall.getOperationCall());
		};
	}
}
