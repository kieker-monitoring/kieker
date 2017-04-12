package kieker.analysisteetime;

import java.util.function.Function;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.type.ComponentType;
import kieker.analysisteetime.model.analysismodel.type.OperationType;
import kieker.analysisteetime.util.ComposedKey;

public final class ModelObjectFromOperationCallAccesors {

	private ModelObjectFromOperationCallAccesors() {}
	
	public static final Function<OperationCall, DeployedOperation> DEPLOYED_OPERATION = c -> c.getOperation();
	
	public static final Function<OperationCall, DeployedComponent> DEPLOYED_COMPONENT = c -> c.getOperation().getComponent();
	
	public static final Function<OperationCall, DeploymentContext> DEPLOYMENT_CONTEXT = c -> c.getOperation().getComponent().getDeploymentContext();
	
	public static final Function<OperationCall, AssemblyOperation> ASSEMBLY_OPERATION = c -> c.getOperation().getAssemblyOperation();
	
	public static final Function<OperationCall, AssemblyComponent> ASSEMBLY_COMPONENT = c -> c.getOperation().getAssemblyOperation().getAssemblyComponent();
	
	public static final Function<OperationCall, OperationType> OPERATION_TYPE = c -> c.getOperation().getAssemblyOperation().getOperationType();
	
	public static final Function<OperationCall, ComponentType> COMPONENT_TYPE = c -> c.getOperation().getAssemblyOperation().getOperationType().getComponentType();
	
	public static final Function<OperationCall, AggregatedInvocation> createForAggregatedInvocation(final ExecutionModel executionModel) {
		return operationCall -> {
			// Check if operationCall is an entry operation call. If so than source is null
			final DeployedOperation source = operationCall.getParent() != null ? operationCall.getParent().getOperation() : null;
			final DeployedOperation target = operationCall.getOperation();
			final ComposedKey<DeployedOperation, DeployedOperation> key = ComposedKey.of(source, target);
			return executionModel.getAggregatedInvocations().get(key);
		};
	}
	
}
