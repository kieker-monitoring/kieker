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

import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class ExecutionModelAssemblerStage extends AbstractFilter<OperationCall> {

	private final ExecutionModelAssembler assembler;

	public ExecutionModelAssemblerStage(final ExecutionModel executionModel) {
		this.assembler = new ExecutionModelAssembler(executionModel);
	}

	@Override
	protected void execute(final OperationCall operationCall) {
		this.assembler.addOperationCall(operationCall);
		this.outputPort.send(operationCall);
	}

}
