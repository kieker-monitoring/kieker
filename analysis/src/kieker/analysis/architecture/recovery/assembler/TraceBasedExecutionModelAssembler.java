/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.architecture.recovery.assembler;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.trace.OperationCall;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TraceBasedExecutionModelAssembler extends AbstractModelAssembler<OperationCall> {

	private final ExecutionFactory factory = ExecutionFactory.eINSTANCE;

	private final ExecutionModel executionModel;

	public TraceBasedExecutionModelAssembler(final ExecutionModel executionModel, final SourceModel sourceModel, final String sourceLabel) {
		super(sourceModel, sourceLabel);
		this.executionModel = executionModel;
	}

	@Override
	public void assemble(final OperationCall operationCall) {
		// Check if operationCall is an entry operation call. If so than source is null
		final DeployedOperation caller = operationCall.getParent() != null ? operationCall.getParent().getOperation() : null; // NOCS (declarative)
		final DeployedOperation callee = operationCall.getOperation();

		this.addExecution(caller, callee);
	}

	protected void addExecution(final DeployedOperation caller, final DeployedOperation callee) {
		final Tuple<DeployedOperation, DeployedOperation> key = this.factory.createTuple();
		key.setFirst(caller);
		key.setSecond(callee);
		if (!this.executionModel.getInvocations().containsKey(key)) {
			final Invocation invocation = this.factory.createInvocation();
			invocation.setCaller(caller);
			invocation.setCallee(callee);

			this.updateSourceModel(invocation);

			this.executionModel.getInvocations().put(key, invocation);
		}
	}

}
