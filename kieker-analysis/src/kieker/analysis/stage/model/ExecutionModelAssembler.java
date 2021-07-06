/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.AggregatedInvocation;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.sources.SourceModel;

/**
 * Assemble execution model based on operation call tuples.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class ExecutionModelAssembler extends AbstractSourceModelAssembler implements IExecutionModelAssembler {

	private static final Logger LOGGER = LoggerFactory.getLogger("ExecutionModelAssembler");

	private final ExecutionFactory factory = ExecutionFactory.eINSTANCE;

	private final ExecutionModel executionModel;

	public ExecutionModelAssembler(final ExecutionModel executionModel, final SourceModel sourceModel, final String sourceLabel) {
		super(sourceModel, sourceLabel);
		this.executionModel = executionModel;
	}

	@Override
	public void addOperationCall(final Tuple<DeployedOperation, DeployedOperation> operationCall) {
		LOGGER.info("{}:{} => {}:{}",
				operationCall.getFirst().getComponent().getAssemblyComponent().getComponentType().getSignature(),
				operationCall.getFirst().getAssemblyOperation().getOperationType().getSignature(),
				operationCall.getSecond().getComponent().getAssemblyComponent().getComponentType().getSignature(),
				operationCall.getSecond().getAssemblyOperation().getOperationType().getSignature());
		if (!this.executionModel.getAggregatedInvocations().containsKey(operationCall)) {
			LOGGER.info("NEW ENTRY");
			final AggregatedInvocation invocation = this.factory.createAggregatedInvocation();
			invocation.setSource(operationCall.getFirst());
			invocation.setTarget(operationCall.getSecond());

			this.updateSourceModel(invocation);

			this.executionModel.getAggregatedInvocations().put(operationCall, invocation);
		} else {
			LOGGER.info("EXISTING ENTRY");
		}
	}

}
