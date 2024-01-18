/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mop;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Reiner Jung
 *
 */
public final class DarModelFactory extends AbstractModelTestFactory {

	private static final String DAR_COMPONENT = "dar-component";

	public static final String DAR_COMPONENT_SIGNATURE = DarModelFactory.PACKAGE + "." + DarModelFactory.DAR_COMPONENT;

	private static final String DAR_ASSEMBLY_NAME = "dar-assembly";
	public static final String DAR_ASSEMBLY_SIGNATURE = DarModelFactory.PACKAGE + "."
			+ DarModelFactory.DAR_ASSEMBLY_NAME;

	private DarModelFactory() {
		// factory
	}

	public static TypeModel createTypeModel() {
		final TypeFactory factory = TypeFactory.eINSTANCE;

		final TypeModel result = factory.createTypeModel();

		final ComponentType component = factory.createComponentType();
		component.setName(DarModelFactory.DAR_COMPONENT);
		component.setPackage(AbstractModelTestFactory.PACKAGE);
		component.setSignature(DarModelFactory.DAR_COMPONENT_SIGNATURE);
		component.getProvidedOperations().put(AbstractModelTestFactory.OP_SIGNATURE, AbstractModelTestFactory
				.createOperationType("operation", "void", AbstractModelTestFactory.OP_SIGNATURE));
		result.getComponentTypes().put(DarModelFactory.DAR_COMPONENT_SIGNATURE, component);

		final ComponentType component2 = factory.createComponentType();
		component2.setName(AbstractModelTestFactory.JOINT_COMPONENT);
		component2.setPackage(AbstractModelTestFactory.PACKAGE);
		component2.setSignature(AbstractModelTestFactory.JOINT_COMPONENT_SIGNATURE);
		component2.getProvidedOperations().put(AbstractModelTestFactory.OP_COMPILE_SIGNATURE, AbstractModelTestFactory
				.createOperationType("compile", "Model", AbstractModelTestFactory.OP_COMPILE_SIGNATURE));
		result.getComponentTypes().put(AbstractModelTestFactory.JOINT_COMPONENT_SIGNATURE, component2);

		return result;
	}

	public static AssemblyModel createAssemblyModel(final TypeModel typeModel) {
		final AssemblyFactory factory = AssemblyFactory.eINSTANCE;

		final AssemblyModel result = factory.createAssemblyModel();

		DarModelFactory.createAssemblyComponent(result, typeModel, DarModelFactory.DAR_COMPONENT_SIGNATURE,
				DarModelFactory.DAR_ASSEMBLY_SIGNATURE, AbstractModelTestFactory.OP_SIGNATURE);
		DarModelFactory.createAssemblyComponent(result, typeModel, AbstractModelTestFactory.JOINT_COMPONENT_SIGNATURE,
				AbstractModelTestFactory.JOINT_ASSEMBLY_SIGNATURE, AbstractModelTestFactory.OP_COMPILE_SIGNATURE);

		return result;
	}

	private static void createAssemblyComponent(final AssemblyModel result, final TypeModel typeModel,
			final String componentSignature, final String assemblySignature, final String operationSignature) {
		final ComponentType type = AbstractModelTestFactory.findType(typeModel, componentSignature);
		final AssemblyComponent component = AbstractModelTestFactory.createAssemblyComponent(assemblySignature, type);
		component.getOperations().put(operationSignature, AbstractModelTestFactory
				.createAssemblyOperation(AbstractModelTestFactory.findOperationType(type, operationSignature)));

		result.getComponents().put(assemblySignature, component);
	}

	public static DeploymentModel createDeploymentModel(final AssemblyModel assemblyModel) {
		final DeploymentFactory factory = DeploymentFactory.eINSTANCE;

		final DeploymentModel result = factory.createDeploymentModel();

		final DeploymentContext context = AbstractModelTestFactory
				.createDeploymentContext(AbstractModelTestFactory.HOSTNAME);

		DarModelFactory.createDeploymentComponent(context, assemblyModel, DarModelFactory.DAR_ASSEMBLY_SIGNATURE,
				AbstractModelTestFactory.OP_SIGNATURE);
		DarModelFactory.createDeploymentComponent(context, assemblyModel,
				AbstractModelTestFactory.JOINT_ASSEMBLY_SIGNATURE, AbstractModelTestFactory.OP_COMPILE_SIGNATURE);

		result.getContexts().put(AbstractModelTestFactory.HOSTNAME, context);

		return result;
	}

	private static void createDeploymentComponent(final DeploymentContext context, final AssemblyModel assemblyModel,
			final String assemblySignature, final String operationSignature) {
		final AssemblyComponent assemblyComponent = AbstractModelTestFactory.findComponent(assemblyModel,
				assemblySignature);
		context.getComponents()
				.put(assemblySignature, AbstractModelTestFactory.createDeploymentComponent(assemblySignature,
						assemblyComponent,
						AbstractModelTestFactory.createDeployedOperation(operationSignature, assemblyComponent)));
	}

	public static ExecutionModel createExecutionModel(final DeploymentModel deploymentModel) {
		final ExecutionFactory factory = ExecutionFactory.eINSTANCE;

		final ExecutionModel result = factory.createExecutionModel();

		final DeployedOperation operation = AbstractModelTestFactory.findOperation(deploymentModel,
				AbstractModelTestFactory.HOSTNAME, DarModelFactory.DAR_ASSEMBLY_SIGNATURE,
				AbstractModelTestFactory.OP_SIGNATURE);
		final DeployedOperation compile = AbstractModelTestFactory.findOperation(deploymentModel,
				AbstractModelTestFactory.HOSTNAME, AbstractModelTestFactory.JOINT_ASSEMBLY_SIGNATURE,
				AbstractModelTestFactory.OP_COMPILE_SIGNATURE);

		// operation -> operation
		AbstractModelTestFactory.createAggregatedInvocation(result.getInvocations(), operation, operation);
		// operation -> compile
		AbstractModelTestFactory.createAggregatedInvocation(result.getInvocations(), operation, compile);
		AbstractModelTestFactory.createAggregatedInvocation(result.getInvocations(), compile, compile);

		return result;
	}

	public static StatisticsModel createStatisticsModel(final ExecutionModel executionModel) {
		final StatisticsFactory factory = StatisticsFactory.eINSTANCE;

		final StatisticsModel result = factory.createStatisticsModel();

		for (final Invocation key : executionModel.getInvocations().values()) {
			result.getStatistics().put(key, AbstractModelTestFactory.createStatistics());
		}

		return result;
	}

}
