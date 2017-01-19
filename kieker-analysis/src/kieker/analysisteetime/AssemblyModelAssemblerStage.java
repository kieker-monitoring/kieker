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

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot;
import kieker.analysisteetime.model.analysismodel.type.TypeRoot;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class AssemblyModelAssemblerStage extends AbstractFilter<IFlowRecord> {

	private final AssemblyModelAssembler assembler;

	public AssemblyModelAssemblerStage(final TypeRoot typeRoot, final AssemblyRoot assemblyRoot) {
		this.assembler = new AssemblyModelAssembler(typeRoot, assemblyRoot);
	}

	@Override
	protected void execute(final IFlowRecord record) {
		if (record instanceof BeforeOperationEvent) {
			this.assembler.addRecord((BeforeOperationEvent) record);
		}
		this.outputPort.send(record);
	}

}
