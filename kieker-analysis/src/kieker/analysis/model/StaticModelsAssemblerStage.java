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

package kieker.analysis.model;

import kieker.analysis.signature.SignatureExtractor;
import kieker.common.record.flow.IFlowRecord;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class StaticModelsAssemblerStage extends CompositeStage {

	private final TypeModel typeModel;
	private final AssemblyModel assemblyModel;
	private final DeploymentModel deploymentModel;

	private final InputPort<IFlowRecord> inputPort;
	private final OutputPort<IFlowRecord> outputPort;

	public StaticModelsAssemblerStage(final SignatureExtractor signatureExtractor) {
		this(TypeFactory.eINSTANCE.createTypeModel(), AssemblyFactory.eINSTANCE.createAssemblyModel(), DeploymentFactory.eINSTANCE.createDeploymentModel(),
				signatureExtractor);
	}

	public StaticModelsAssemblerStage(final TypeModel typeModel, final AssemblyModel assemblyModel, final DeploymentModel deploymentModel,
			final SignatureExtractor signatureExtractor) {
		this.typeModel = typeModel;
		this.assemblyModel = assemblyModel;
		this.deploymentModel = deploymentModel;

		final TypeModelAssemblerStage typeModelAssembler = new TypeModelAssemblerStage(this.typeModel, signatureExtractor.getComponentSignatureExtractor(),
				signatureExtractor.getOperationSignatureExtractor());
		final AssemblyModelAssemblerStage assemblyModelAssembler = new AssemblyModelAssemblerStage(this.typeModel, this.assemblyModel);
		final DeploymentModelAssemblerStage deploymentModelAssembler = new DeploymentModelAssemblerStage(this.assemblyModel, this.deploymentModel);

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

	public InputPort<IFlowRecord> getInputPort() {
		return this.inputPort;
	}

	public OutputPort<IFlowRecord> getOutputPort() {
		return this.outputPort;
	}

}
