/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionFactory;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.util.ComposedKey;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ExecutionModelAssembler {

	private final ExecutionFactory factory = ExecutionFactory.eINSTANCE;

	private final ExecutionModel executionModel;

	public ExecutionModelAssembler(final ExecutionModel executionModel) {
		this.executionModel = executionModel;
	}

	public void addOperationCall(final OperationCall operationCall) {
		// Check if operationCall is an entry operation call. If so than source is null
		final DeployedOperation source = operationCall.getParent() != null ? operationCall.getParent().getOperation() : null;
		final DeployedOperation target = operationCall.getOperation();

		this.addExecution(source, target);
	}

	protected void addExecution(final DeployedOperation source, final DeployedOperation target) {
		final ComposedKey<DeployedOperation, DeployedOperation> key = ComposedKey.of(source, target);
		if (!this.executionModel.getAggregatedInvocations().contains(key)) {
			final AggregatedInvocation invocation = this.factory.createAggregatedInvocation();
			invocation.setSource(source);
			invocation.setTarget(target);

			this.executionModel.getAggregatedInvocations().put(key, invocation);
		}
	}

}
