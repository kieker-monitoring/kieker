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

package kieker.analysisteetime.modeltests.adjusted;

import java.util.Arrays;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ModelTester {

	public ModelTester() {

	}

	public static void main(final String[] args) {

		final ComponentTypeAdjusted componentType = new ComponentTypeAdjustedImpl();

		final OperationType operationType1 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType1.setSignature("public void doSomething()");
		operationType1.setComponentType(componentType);

		final OperationType operationType2 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType2.setComponentType(componentType);
		operationType2.setSignature("public void doSomithengDeffirint()");
		operationType2.setSignature("public void doSomethingDifferent()");

		componentType.getProvidedOperations().remove(1);

		final OperationType operationType3 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType1.setSignature("public String getSomeString()");

		componentType.getProvidedOperations().addAll(Arrays.asList(new OperationType[] { operationType2, operationType3 }));

		componentType.setSignature("org.organization.Component");

	}

}
