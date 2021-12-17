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

package kieker.analysis.stage.model;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.AggregatedInvocation;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.trace.OperationCall;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TraceBasedExecutionModelAssembler extends AbstractSourceModelAssembler implements ITraceBasedExecutionModelAssembler {

	private final ExecutionFactory factory = ExecutionFactory.eINSTANCE;

	private final ExecutionModel executionModel;

	public TraceBasedExecutionModelAssembler(final ExecutionModel executionModel, final SourceModel sourceModel, final String sourceLabel) {
		super(sourceModel, sourceLabel);
		this.executionModel = executionModel;
	}

	@Override
	public void addOperationCall(final OperationCall operationCall) {
		// Check if operationCall is an entry operation call. If so than source is null
		final DeployedOperation source = operationCall.getParent() != null ? operationCall.getParent().getOperation() : null; // NOCS (declarative)
		final DeployedOperation target = operationCall.getOperation();

		this.addExecution(source, target);
	}

	protected void addExecution(final DeployedOperation source, final DeployedOperation target) {
		final Tuple<DeployedOperation, DeployedOperation> key = this.factory.createTuple();
		key.setFirst(source);
		key.setSecond(target);
		if (!this.executionModel.getAggregatedInvocations().containsKey(key)) {
			final AggregatedInvocation invocation = this.factory.createAggregatedInvocation();
			invocation.setSource(source);
			invocation.setTarget(target);

			this.updateSourceModel(invocation);

			this.executionModel.getAggregatedInvocations().put(key, invocation);
		}
	}

}
