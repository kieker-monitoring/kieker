/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyFactory;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyOperation;
import kieker.analysisteetime.model.analysismodel.type.ComponentType;
import kieker.analysisteetime.model.analysismodel.type.OperationType;
import kieker.analysisteetime.model.analysismodel.type.TypeModel;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class AssemblyModelAssembler {

	private final AssemblyFactory factory = AssemblyFactory.eINSTANCE;

	private final TypeModel typeModel;
	private final AssemblyModel assemblyModel;

	public AssemblyModelAssembler(final TypeModel typeModel, final AssemblyModel assemblyModel) {
		this.typeModel = typeModel;
		this.assemblyModel = assemblyModel;
	}

	public void addRecord(final BeforeOperationEvent record) {
		final String classSignature = record.getClassSignature();
		final String operationSignature = record.getOperationSignature();

		this.addRecord(classSignature, operationSignature);
	}

	public void addRecord(final String componentSignature, final String operationSignature) {
		final AssemblyComponent component = this.addAssemblyComponent(componentSignature);
		this.addAssemblyOperation(component, operationSignature);
	}

	private AssemblyComponent addAssemblyComponent(final String componentSignature) {
		final String componentKey = componentSignature;
		AssemblyComponent component = this.assemblyModel.getAssemblyComponents().get(componentKey);
		if (component == null) {
			component = this.factory.createAssemblyComponent();
			this.assemblyModel.getAssemblyComponents().put(componentKey, component);

			final String componentTypeKey = componentSignature;
			final ComponentType componentType = this.typeModel.getComponentTypes().get(componentTypeKey);
			component.setComponentType(componentType);
		}
		return component;
	}

	private AssemblyOperation addAssemblyOperation(final AssemblyComponent component, final String operationSignature) {
		final String operationKey = operationSignature;
		AssemblyOperation operation = component.getAssemblyOperations().get(operationKey);
		if (operation == null) {
			operation = this.factory.createAssemblyOperation();
			component.getAssemblyOperations().put(operationKey, operation);

			final ComponentType componentType = component.getComponentType();
			final String operationTypeKey = operationSignature;
			final OperationType operationType = componentType.getProvidedOperations().get(operationTypeKey);
			operation.setOperationType(operationType);
		}
		return operation;
	}

}
