/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.architecture.recovery.assembler;

import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class OperationAssemblyModelAssembler extends AbstractModelAssembler<OperationEvent> {

	private final AssemblyFactory factory = AssemblyFactory.eINSTANCE;

	private final TypeModel typeModel;
	private final AssemblyModel assemblyModel;

	public OperationAssemblyModelAssembler(final TypeModel typeModel, final AssemblyModel assemblyModel, final SourceModel sourceModel, final String sourceLabel) {
		super(sourceModel, sourceLabel);
		this.typeModel = typeModel;
		this.assemblyModel = assemblyModel;
	}

	@Override
	public void assemble(final OperationEvent event) {
		final String componentSignature = event.getComponentSignature();
		final String operationSignature = event.getOperationSignature();

		this.addOperation(componentSignature, operationSignature);
	}

	public void addOperation(final String componentSignature, final String operationSignature) {
		final AssemblyComponent component = this.addAssemblyComponent(componentSignature);
		this.addAssemblyOperation(component, operationSignature);
	}

	private AssemblyComponent addAssemblyComponent(final String componentSignature) {
		final String componentKey = componentSignature;
		AssemblyComponent component = this.assemblyModel.getComponents().get(componentKey);
		if (component == null) {
			component = this.factory.createAssemblyComponent();
			this.assemblyModel.getComponents().put(componentKey, component);

			final String componentTypeKey = componentSignature;
			final ComponentType componentType = this.typeModel.getComponentTypes().get(componentTypeKey);
			component.setComponentType(componentType);
			component.setSignature(componentSignature);
		}
		this.updateSourceModel(component);

		return component;
	}

	private AssemblyOperation addAssemblyOperation(final AssemblyComponent component, final String operationSignature) {
		final String operationKey = operationSignature;
		AssemblyOperation operation = component.getOperations().get(operationKey);
		if (operation == null) {
			operation = this.factory.createAssemblyOperation();
			component.getOperations().put(operationKey, operation);

			final ComponentType componentType = component.getComponentType();
			final String operationTypeKey = operationSignature;
			final OperationType operationType = componentType.getProvidedOperations().get(operationTypeKey);
			operation.setOperationType(operationType);
		}

		this.updateSourceModel(operation);

		return operation;
	}

}
