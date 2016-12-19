/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;
import kieker.common.record.flow.IOperationRecord;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ArchitectureModelAssembler {

	private final ArchitectureFactory factory = ArchitectureFactory.eINSTANCE;

	private final ArchitectureRoot architectureRoot;

	public ArchitectureModelAssembler(final ArchitectureRoot architectureRoot) {
		this.architectureRoot = architectureRoot;
	}

	public void addRecord(final IOperationRecord record) {
		final String classSignature = record.getClassSignature();
		final String operationSignature = record.getOperationSignature();

		this.addRecord(classSignature, operationSignature);
	}

	public void addRecord(final String componentSignature, final String operationSignature) {

		final String componentTypeKey = componentSignature;
		ComponentType componentType = this.architectureRoot.getComponentTypes().get(componentTypeKey);
		if (componentType == null) {
			componentType = this.factory.createComponentType();
			componentType.setSignature(componentSignature);
			this.architectureRoot.getComponentTypes().put(componentTypeKey, componentType);
		}

		final String operationTypeKey = operationSignature;
		OperationType operationType = componentType.getProvidedOperations().get(operationSignature);
		if (operationType == null) {
			operationType = this.factory.createOperationType();
			operationType.setSignature(operationSignature);
			componentType.getProvidedOperations().put(operationTypeKey, operationType);
		}

	}

}
