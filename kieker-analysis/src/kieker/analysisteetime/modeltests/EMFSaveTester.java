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

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.IndexedComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class EMFSaveTester {

	public static void main(final String[] args) {

		final ArchitectureFactory factory = ArchitectureFactory.eINSTANCE;
		final IndexedArchitectureFactory indexedFactory = IndexedArchitectureFactory.INSTANCE;

		// create the model
		final IndexedArchitectureRoot architectureRoot = indexedFactory.createIndexedArchitectureRoot();
		final IndexedComponentType componentType = indexedFactory.createIndexedComponentType();
		componentType.setSignature("org.software.component");
		componentType.setArchitectureRoot(architectureRoot);
		final OperationType operationType1 = factory.createOperationType();
		operationType1.setSignature("public void doSomething()");
		operationType1.setComponentType(componentType);
		final OperationType operationType2 = factory.createOperationType();
		operationType2.setSignature("public String createSomeString()");
		operationType2.setComponentType(componentType);

		// As of here we preparing to save the model content

		// Register the XMI resource factory for the .amm extension

		final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		final Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("amm", new XMIResourceFactoryImpl()); // TODO website

		// Obtain a new resource set
		final ResourceSet resSet = new ResourceSetImpl();

		// create a resource
		final Resource resource = resSet.createResource(URI.createURI("amm/My2.amm"));
		// Get the first model element and cast it to the right type, in my
		// example everything is hierarchical included in this first node
		resource.getContents().add(architectureRoot);

		// now save the content.
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
