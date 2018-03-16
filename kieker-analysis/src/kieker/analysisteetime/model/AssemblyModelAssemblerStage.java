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

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class AssemblyModelAssemblerStage extends AbstractFilter<IFlowRecord> {

	private final AssemblyModelAssembler assembler;

	public AssemblyModelAssemblerStage(final TypeModel typeModel, final AssemblyModel assemblyModel) {
		this.assembler = new AssemblyModelAssembler(typeModel, assemblyModel);
	}

	@Override
	protected void execute(final IFlowRecord record) {
		if (record instanceof BeforeOperationEvent) {
			this.assembler.addRecord((BeforeOperationEvent) record);
		}
		this.outputPort.send(record);
	}

}
