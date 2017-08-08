package kieker.analysisteetime.model;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.analysisteetime.signature.SignatureExtractor;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

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
