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

import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ModelTester {

	public ModelTester() {}

	public static void main(final String[] args) {

		final ArchitectureModelBuilder architectureModelBuilder = new ArchitectureModelBuilder();

		architectureModelBuilder.addRecord("org.organisation.firstcomponent", "public void doSomething()");

		architectureModelBuilder.addRecord("org.organisation.firstcomponent", "public void doSomethingDifferent()");

		architectureModelBuilder.addRecord("org.organisation.firstcomponent", "public void doSomething()");

		architectureModelBuilder.addRecord("org.organisation.secondcomponent", "public void doSomething()");

		architectureModelBuilder.addRecord("org.organisation.secondcomponent", "protected String getSomeString()");

		architectureModelBuilder.addRecord("org.organisation.firstcomponent", "public void doSomething()");

		final ArchitectureRoot architectureRoot = architectureModelBuilder.build();

		for (final ComponentType componentType : architectureRoot.getComponentTypes()) {
			componentType.getSignature();
			for (final OperationType operationType : componentType.getProvidedOperations()) {
				operationType.getSignature();
			}
		}

		// Now we want to find a component/operation by its name (String)
		// How to do this?
		// -> no method available, so we have to iterate an look in every one
		// -> we could build a "Builder"/"Modifier"/... so we only have to index everything once
		// -> this leads to a surround data structure, which has to be in sync with the model itself

	}

}
