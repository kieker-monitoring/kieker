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

import kieker.analysis.stage.model.data.OperationEvent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.sources.SourceModel;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DeploymentModelAssemblerStage extends AbstractFilter<OperationEvent> {

	private final DeploymentModelAssembler assembler;

	public DeploymentModelAssemblerStage(final AssemblyModel assemblyModel, final DeploymentModel deploymentModel, final SourceModel sourceModel,
			final String sourceLabel) {
		this.assembler = new DeploymentModelAssembler(assemblyModel, deploymentModel, sourceModel, sourceLabel);
	}

	@Override
	protected void execute(final OperationEvent event) {
		this.assembler.addOperation(event);
		this.outputPort.send(event);
	}

}
