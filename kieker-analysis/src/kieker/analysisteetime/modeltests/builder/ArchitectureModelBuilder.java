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

package kieker.analysisteetime.modeltests.builder;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ArchitectureModelBuilder {

	private static final ArchitectureFactory FACTORY = ArchitectureFactory.eINSTANCE;

	private final ArchitectureRoot architectureRoot;
	private final Map<String, Pair<ComponentType, Map<String, OperationType>>> repository = new HashMap<>();

	public ArchitectureModelBuilder() {
		this.architectureRoot = FACTORY.createArchitectureRoot();
	}

	// TODO Maybe add a boolean return value
	public void addRecord(final String classSignature, final String operationSignature) {

		if (!this.repository.containsKey(classSignature)) {
			final ComponentType componentType = FACTORY.createComponentType();
			componentType.setSignature(classSignature);

			this.repository.put(classSignature, Pair.of(componentType, new HashMap<>()));
		}

		final Map<String, OperationType> operationTypes = this.repository.get(classSignature).getRight();
		if (!operationTypes.containsKey(operationSignature)) {
			final OperationType operationType = FACTORY.createOperationType();
			operationType.setSignature(operationSignature);

			operationTypes.put(operationSignature, operationType);
		}

	}

	public ArchitectureRoot build() {
		return this.architectureRoot;
	}

}
