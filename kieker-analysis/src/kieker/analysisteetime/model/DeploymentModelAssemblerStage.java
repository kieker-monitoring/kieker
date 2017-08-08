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

package kieker.analysisteetime.model;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DeploymentModelAssemblerStage extends AbstractFilter<IFlowRecord> {

	private final DeploymentModelAssembler assembler;

	public DeploymentModelAssemblerStage(final AssemblyModel assemblyModel, final DeploymentModel deploymentModel) {
		this.assembler = new DeploymentModelAssembler(assemblyModel, deploymentModel);
	}

	@Override
	protected void execute(final IFlowRecord record) {
		if (record instanceof TraceMetadata) {
			this.assembler.handleMetadataRecord((TraceMetadata) record);
		} else if (record instanceof BeforeOperationEvent) {
			this.assembler.handleBeforeOperationEvent((BeforeOperationEvent) record);
		} else if (record instanceof AfterOperationEvent) {
			this.assembler.handleAfterOperationEvent((AfterOperationEvent) record);
		}
		this.outputPort.send(record);
	}

}
