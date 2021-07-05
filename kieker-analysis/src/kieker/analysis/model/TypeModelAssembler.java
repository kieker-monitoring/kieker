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

import kieker.analysis.model.data.OperationEvent;
import kieker.analysis.signature.IComponentSignatureExtractor;
import kieker.analysis.signature.IOperationSignatureExtractor;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TypeModelAssembler extends AbstractSourceModelAssembler {

	private final TypeFactory factory = TypeFactory.eINSTANCE;
	private final IComponentSignatureExtractor componentSignatureExtractor;
	private final IOperationSignatureExtractor operationSignatureExtractor;

	private final TypeModel typeModel;

	public TypeModelAssembler(final TypeModel typeModel, final SourceModel sourceModel, final String sourceLabel,
			final IComponentSignatureExtractor componentSignatureExtractor,
			final IOperationSignatureExtractor operationSignatureExtractor) {
		super(sourceModel, sourceLabel);

		this.typeModel = typeModel;
		this.componentSignatureExtractor = componentSignatureExtractor;
		this.operationSignatureExtractor = operationSignatureExtractor;
	}

	public void addOperation(final OperationEvent event) {
		final String componentSignature = event.getComponentSignature();
		final String operationSignature = event.getOperationSignature();

		final ComponentType componentType = this.addComponentType(componentSignature);
		this.addOperationType(componentType, operationSignature);
	}

	private ComponentType addComponentType(final String componentSignature) {
		final String componentTypeKey = componentSignature;
		ComponentType componentType = this.typeModel.getComponentTypes().get(componentTypeKey);
		if (componentType == null) {
			componentType = this.factory.createComponentType();
			componentType.setSignature(componentSignature);
			this.componentSignatureExtractor.extract(componentType);
			this.typeModel.getComponentTypes().put(componentTypeKey, componentType);
		}
		this.updateSourceModel(componentType);
		return componentType;
	}

	private OperationType addOperationType(final ComponentType componentType, final String operationSignature) {
		final String operationTypeKey = operationSignature;
		OperationType operationType = componentType.getProvidedOperations().get(operationSignature);
		if (operationType == null) {
			operationType = this.factory.createOperationType();
			operationType.setSignature(operationSignature);
			this.operationSignatureExtractor.extract(operationType);
			componentType.getProvidedOperations().put(operationTypeKey, operationType);
		}
		this.updateSourceModel(operationType);
		return operationType;
	}

}
