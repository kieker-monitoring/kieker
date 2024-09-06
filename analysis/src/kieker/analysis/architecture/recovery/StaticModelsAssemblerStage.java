/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.recovery;

import kieker.analysis.architecture.recovery.assembler.OperationAssemblyModelAssembler;
import kieker.analysis.architecture.recovery.assembler.OperationDeploymentModelAssembler;
import kieker.analysis.architecture.recovery.assembler.OperationTypeModelAssembler;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.signature.SignatureExtractor;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.type.TypeModel;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Composite stage covering @{link TypeModel}, @{link AssemblyModel}, @{link DeploymentModel}
 * and @{link SourceModel} in one stage. Receives @{link OperationEvent}s and sends them out
 * unmodified. State changes happen in the used models.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class StaticModelsAssemblerStage extends CompositeStage { // NOPMD not a data class

	private final TypeModel typeModel;
	private final AssemblyModel assemblyModel;
	private final DeploymentModel deploymentModel;
	private final SourceModel sourceModel;

	private final InputPort<OperationEvent> inputPort;
	private final OutputPort<OperationEvent> outputPort;

	/**
	 * Create a static model assembler stage.
	 *
	 * @param typeModel
	 *            type model
	 * @param assemblyModel
	 *            assembly model
	 * @param deploymentModel
	 *            deployment model
	 * @param sourceModel
	 *            source model
	 * @param sourceLabel
	 *            label to be used for all added model elements
	 * @param signatureExtractor
	 *            signature extractor for @{link OperationEvent}s to determine package, component and operation names
	 */
	public StaticModelsAssemblerStage(final TypeModel typeModel, final AssemblyModel assemblyModel,
			final DeploymentModel deploymentModel, final SourceModel sourceModel, final String sourceLabel,
			final SignatureExtractor signatureExtractor) {
		this.typeModel = typeModel;
		this.assemblyModel = assemblyModel;
		this.deploymentModel = deploymentModel;
		this.sourceModel = sourceModel;

		final ModelAssemblerStage<OperationEvent> typeModelAssembler = new ModelAssemblerStage<>(
				new OperationTypeModelAssembler(this.typeModel, this.sourceModel, sourceLabel,
						signatureExtractor.getComponentSignatureExtractor(),
						signatureExtractor.getOperationSignatureExtractor()));
		final ModelAssemblerStage<OperationEvent> assemblyModelAssembler = new ModelAssemblerStage<>(
				new OperationAssemblyModelAssembler(this.typeModel, this.assemblyModel, this.sourceModel,
						sourceLabel));
		final ModelAssemblerStage<OperationEvent> deploymentModelAssembler = new ModelAssemblerStage<>(
				new OperationDeploymentModelAssembler(this.assemblyModel, this.deploymentModel, this.sourceModel,
						sourceLabel));

		this.inputPort = typeModelAssembler.getInputPort();
		this.outputPort = deploymentModelAssembler.getOutputPort();

		super.connectPorts(typeModelAssembler.getOutputPort(), assemblyModelAssembler.getInputPort());
		super.connectPorts(assemblyModelAssembler.getOutputPort(), deploymentModelAssembler.getInputPort());
	}

	public TypeModel getTypeModel() {
		return this.typeModel;
	}

	public AssemblyModel getAssemblyModel() {
		return this.assemblyModel;
	}

	public DeploymentModel getDeploymentModel() {
		return this.deploymentModel;
	}

	public SourceModel getSourceModel() {
		return this.sourceModel;
	}

	public InputPort<OperationEvent> getInputPort() {
		return this.inputPort;
	}

	public OutputPort<OperationEvent> getOutputPort() {
		return this.outputPort;
	}

}
