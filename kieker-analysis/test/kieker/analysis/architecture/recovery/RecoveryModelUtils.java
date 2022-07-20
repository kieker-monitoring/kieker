/**
 *
 */
package kieker.analysis.architecture.recovery;

import org.junit.Assert;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.sources.SourcesFactory;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class RecoveryModelUtils {

	public static final String SOURCE_LABEL = "test-label";

	public static final String HOST_NAME = "test";
	public static final String MISSING_HOST_NAME = "wrong-host";

	public static final String COMPONENT_NAME = "component";
	public static final String MISSING_COMPONENT_NAME = "missing-component";
	public static final String COMPONENT_PACKAGE = "test";
	public static final String COMPONENT_SIGNATURE = COMPONENT_PACKAGE + "." + COMPONENT_NAME;
	public static final String MISSING_COMPONENT_SIGNATURE = COMPONENT_PACKAGE + "." + MISSING_COMPONENT_NAME;

	public static final String OPERATION_NAME = "op";
	public static final String OPERATION_TYPE = "integer";
	public static final String OPERATION_SIGNATURE = OPERATION_TYPE + " " + OPERATION_NAME + "()";

	public static final String MISSING_OPERATION_NAME = "missing";
	public static final String MISSING_OPERATION_TYPE = "integer";
	public static final String MISSING_OPERATION_SIGNATURE = MISSING_OPERATION_TYPE + " " + MISSING_OPERATION_NAME + "()";

	public static TypeModel createTypeModel() {
		final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
		final ComponentType component = TypeFactory.eINSTANCE.createComponentType();
		component.setSignature(COMPONENT_SIGNATURE);
		component.setName(COMPONENT_NAME);
		component.setPackage(COMPONENT_PACKAGE);
		final OperationType operationType = TypeFactory.eINSTANCE.createOperationType();
		operationType.setSignature(OPERATION_SIGNATURE);
		operationType.setName(OPERATION_NAME);
		operationType.setReturnType(OPERATION_TYPE);
		component.getProvidedOperations().put(OPERATION_SIGNATURE, operationType);
		typeModel.getComponentTypes().put(COMPONENT_SIGNATURE, component);

		return typeModel;
	}

	public static AssemblyModel createAssemblyModel(final TypeModel typeModel) {
		final AssemblyModel assemblyModel = RecoveryModelUtils.createEmptyAssemblyModel();

		final AssemblyComponent assemblyComponent = AssemblyFactory.eINSTANCE.createAssemblyComponent();
		final ComponentType componentType = typeModel.getComponentTypes().get(COMPONENT_SIGNATURE);
		assemblyComponent.setComponentType(componentType);
		Assert.assertNotNull("Component missing", assemblyComponent.getComponentType());
		assemblyModel.getComponents().put(COMPONENT_SIGNATURE, assemblyComponent);

		final AssemblyOperation assemblyOperation = AssemblyFactory.eINSTANCE.createAssemblyOperation();
		assemblyOperation.setOperationType(componentType.getProvidedOperations().get(OPERATION_SIGNATURE));
		Assert.assertNotNull("Operation missing", assemblyOperation.getOperationType());

		assemblyComponent.getOperations().put(OPERATION_SIGNATURE, assemblyOperation);

		return assemblyModel;
	}

	public static AssemblyModel createEmptyAssemblyModel() {
		return AssemblyFactory.eINSTANCE.createAssemblyModel();
	}

	public static SourceModel createEmptySourceModel() {
		return SourcesFactory.eINSTANCE.createSourceModel();
	}

	public static DeploymentModel createDeploymentModel(final AssemblyModel assemblyModel) {
		final DeploymentModel deploymentModel = RecoveryModelUtils.createEmptyDeploymentModel();

		final DeploymentContext context = DeploymentFactory.eINSTANCE.createDeploymentContext();
		context.setName(HOST_NAME);
		deploymentModel.getContexts().put(HOST_NAME, context);

		final DeployedComponent deployedComponent = DeploymentFactory.eINSTANCE.createDeployedComponent();

		final AssemblyComponent assemblyComponent = assemblyModel.getComponents().get(COMPONENT_SIGNATURE);
		Assert.assertNotNull("Missing assembly component", assemblyComponent);
		deployedComponent.setAssemblyComponent(assemblyComponent);
		deployedComponent.setSignature(COMPONENT_SIGNATURE);
		context.getComponents().put(COMPONENT_SIGNATURE, deployedComponent);

		final DeployedOperation deployedOperation = DeploymentFactory.eINSTANCE.createDeployedOperation();
		deployedOperation.setAssemblyOperation(assemblyComponent.getOperations().get(OPERATION_SIGNATURE));
		Assert.assertNotNull("Missing assembly operation", deployedOperation.getAssemblyOperation());

		deployedComponent.getOperations().put(OPERATION_SIGNATURE, deployedOperation);

		return deploymentModel;
	}

	private static DeploymentModel createEmptyDeploymentModel() {
		return DeploymentFactory.eINSTANCE.createDeploymentModel();
	}

}
