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

package kieker.analysisteetime.modeltests;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;
import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class EMFLoadTester {

	public static void main(final String[] args) {

		ArchitecturePackage.eINSTANCE.eClass();

		// Register the XMI resource factory for the .amm extension

		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("amm", new XMIResourceFactoryImpl());

		// Obtain a new resource set
		final ResourceSet resSet = new ResourceSetImpl();

		// Get the resource
		final Resource resource = resSet.getResource(URI.createURI("amm/My2.amm"), true);
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		final ArchitectureRoot architectureRoot = (ArchitectureRoot) resource.getContents().get(0);

		// Accessing the model information
		// Lets see what info the webpage has
		for (final ComponentType componentType : architectureRoot.getComponentTypes()) {
			System.out.println("Component Type: " + componentType.getSignature());
			for (final OperationType operationType : componentType.getProvidedOperations()) {
				System.out.println("Operation Type: " + operationType.getSignature());
			}
		}

	}

}
