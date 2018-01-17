package kieker.analysisteetime.model;

import java.util.function.Function;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.util.ComposedKey;

public final class ModelObjectFromOperationCallAccessors {

	private ModelObjectFromOperationCallAccessors() {}

	public static final Function<OperationCall, Object> DEPLOYED_OPERATION = c -> c.getOperation();

	public static final Function<OperationCall, Object> DEPLOYED_COMPONENT = c -> c.getOperation().getComponent();

	public static final Function<OperationCall, Object> DEPLOYMENT_CONTEXT = c -> c.getOperation().getComponent().getDeploymentContext();

	public static final Function<OperationCall, Object> ASSEMBLY_OPERATION = c -> c.getOperation().getAssemblyOperation();

	public static final Function<OperationCall, Object> ASSEMBLY_COMPONENT = c -> c.getOperation().getAssemblyOperation().getAssemblyComponent();

	public static final Function<OperationCall, Object> OPERATION_TYPE = c -> c.getOperation().getAssemblyOperation().getOperationType();

	public static final Function<OperationCall, Object> COMPONENT_TYPE = c -> c.getOperation().getAssemblyOperation().getOperationType().getComponentType();

	public static final Function<OperationCall, Object> createForAggregatedInvocation(final ExecutionModel executionModel) {
		return operationCall -> {
			// Check if operationCall is an entry operation call. If so than source is null
			final DeployedOperation source = operationCall.getParent() != null ? operationCall.getParent().getOperation() : null;
			final DeployedOperation target = operationCall.getOperation();
			final ComposedKey<DeployedOperation, DeployedOperation> key = ComposedKey.of(source, target);
			return executionModel.getAggregatedInvocations().get(key);
		};
	}

}
