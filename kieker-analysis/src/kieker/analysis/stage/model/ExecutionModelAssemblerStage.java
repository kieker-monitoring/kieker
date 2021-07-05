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

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.Tuple;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class ExecutionModelAssemblerStage extends AbstractFilter<Tuple<DeployedOperation, DeployedOperation>> {

	private final IExecutionModelAssembler assembler;

	public ExecutionModelAssemblerStage(final IExecutionModelAssembler assembler) {
		this.assembler = assembler;
	}

	@Override
	protected void execute(final Tuple<DeployedOperation, DeployedOperation> operationCall) {
		this.assembler.addOperationCall(operationCall);
		this.outputPort.send(operationCall);
	}

}
