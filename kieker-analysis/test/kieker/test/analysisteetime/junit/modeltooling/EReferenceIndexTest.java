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

package kieker.test.analysisteetime.junit.modeltooling;

import java.util.Arrays;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;
import kieker.analysisteetime.modeltooling.EReferenceIndex;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class EReferenceIndexTest {

	private final static String EXAMPLE_OPERATION_SIGNATURE_1 = "public void doSomething()";
	private final static String EXAMPLE_OPERATION_SIGNATURE_2 = "private void doSomethingDifferent()";
	private final static String EXAMPLE_OPERATION_SIGNATURE_3 = "public String getSomeString()";

	private ComponentType componentType;
	private EReference reference;
	private EAttribute containmentAttribute;

	@Before
	public void setUp() throws Exception {
		this.componentType = ArchitectureFactory.eINSTANCE.createComponentType();
		this.reference = ArchitecturePackage.eINSTANCE.getComponentType_ProvidedOperations();
		this.containmentAttribute = ArchitecturePackage.eINSTANCE.getOperationType_Signature();
	}

	/**
	 * Test method for
	 * {@link kieker.analysisteetime.modeltooling.EReferenceIndex#create(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EReference, java.util.Collection, java.util.function.Function)}
	 * .
	 */
	@Test
	public void testElementsExistAfterCreate() {
		final OperationType operationType1 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType1.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType1.setComponentType(this.componentType);

		final OperationType operationType2 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType2.setSignature(EXAMPLE_OPERATION_SIGNATURE_2);
		operationType2.setComponentType(this.componentType);

		final OperationType operationType3 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType3.setSignature(EXAMPLE_OPERATION_SIGNATURE_3);
		operationType3.setComponentType(this.componentType);

		final EReferenceIndex<String, OperationType> index = EReferenceIndex.create(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature);

		final OperationType retrievedOperationType1 = index.get(EXAMPLE_OPERATION_SIGNATURE_1);
		final OperationType retrievedOperationType2 = index.get(EXAMPLE_OPERATION_SIGNATURE_2);
		final OperationType retrievedOperationType3 = index.get(EXAMPLE_OPERATION_SIGNATURE_3);

		final Object[] expecteds = this.componentType.getProvidedOperations().toArray();
		final OperationType[] actuals = new OperationType[] { retrievedOperationType1, retrievedOperationType2, retrievedOperationType3 };

		Assert.assertArrayEquals(expecteds, actuals);
	}

	/**
	 * Test method for
	 * {@link kieker.analysisteetime.modeltooling.EReferenceIndex#create(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EReference, java.util.Collection, java.util.function.Function, java.util.Collection)}
	 * .
	 */
	@Test
	public void testElementsExistAfterCreateWithProvidedContainments() {
		final OperationType operationType1 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType1.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType1.setComponentType(this.componentType);

		final OperationType operationType2 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType2.setSignature(EXAMPLE_OPERATION_SIGNATURE_2);
		operationType2.setComponentType(this.componentType);

		final OperationType operationType3 = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType3.setSignature(EXAMPLE_OPERATION_SIGNATURE_3);
		operationType3.setComponentType(this.componentType);

		final EReferenceIndex<String, OperationType> index = EReferenceIndex.create(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature, this.componentType.getProvidedOperations());

		final OperationType retrievedOperationType1 = index.get(EXAMPLE_OPERATION_SIGNATURE_1);
		final OperationType retrievedOperationType2 = index.get(EXAMPLE_OPERATION_SIGNATURE_2);
		final OperationType retrievedOperationType3 = index.get(EXAMPLE_OPERATION_SIGNATURE_3);

		final Object[] expecteds = this.componentType.getProvidedOperations().toArray();
		final OperationType[] actuals = new OperationType[] { retrievedOperationType1, retrievedOperationType2, retrievedOperationType3 };

		Assert.assertArrayEquals(expecteds, actuals);
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceIndex#get(java.lang.Object)}.
	 */
	@Test
	public void testContainsAfterInsert() {
		final EReferenceIndex<String, OperationType> index = EReferenceIndex.createEmpty(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature);

		final OperationType operationType = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType.setComponentType(this.componentType);

		final boolean containsReturn = index.contains(EXAMPLE_OPERATION_SIGNATURE_1);

		Assert.assertTrue(containsReturn);
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceIndex#contains(java.lang.Object)}.
	 */
	@Test
	public void testGetAfterInsert() {
		final EReferenceIndex<String, OperationType> index = EReferenceIndex.createEmpty(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature);

		final OperationType operationType = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType.setComponentType(this.componentType);

		final OperationType returnedOperationType = index.get(EXAMPLE_OPERATION_SIGNATURE_1);

		Assert.assertSame(operationType, returnedOperationType);
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceIndex#get(java.lang.Object)}.
	 */
	@Test
	public void testContainsAfterRemove() {
		final EReferenceIndex<String, OperationType> index = EReferenceIndex.createEmpty(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature);

		final OperationType operationType = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType.setComponentType(this.componentType);

		this.componentType.getProvidedOperations().remove(operationType);

		final boolean containsReturn = index.contains(EXAMPLE_OPERATION_SIGNATURE_1);

		Assert.assertFalse(containsReturn);
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceIndex#contains(java.lang.Object)}.
	 */
	@Test
	public void testGetAfterRemove() {
		final EReferenceIndex<String, OperationType> index = EReferenceIndex.createEmpty(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature);

		final OperationType operationType = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType.setComponentType(this.componentType);

		this.componentType.getProvidedOperations().remove(operationType);

		final OperationType returnedOperationType = index.get(EXAMPLE_OPERATION_SIGNATURE_1);

		Assert.assertNull(returnedOperationType);
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceIndex#get(java.lang.Object)}.
	 */
	@Test
	public void testContainsAfterModified() {
		final EReferenceIndex<String, OperationType> index = EReferenceIndex.createEmpty(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature);

		final OperationType operationType = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType.setComponentType(this.componentType);

		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_2);

		final boolean containsReturn = index.contains(EXAMPLE_OPERATION_SIGNATURE_2);

		Assert.assertTrue(containsReturn);
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceIndex#get(java.lang.Object)}.
	 */
	@Test
	public void testNotContainsAfterModified() {
		final EReferenceIndex<String, OperationType> index = EReferenceIndex.createEmpty(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature);

		final OperationType operationType = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType.setComponentType(this.componentType);

		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_2);

		final boolean containsReturn = index.contains(EXAMPLE_OPERATION_SIGNATURE_1);

		Assert.assertFalse(containsReturn);
	}

	/**
	 * Test method for {@link kieker.analysisteetime.modeltooling.EReferenceIndex#contains(java.lang.Object)}.
	 */
	@Test
	public void testGetAfterModified() {
		final EReferenceIndex<String, OperationType> index = EReferenceIndex.createEmpty(this.componentType, this.reference,
				Arrays.asList(this.containmentAttribute), OperationType::getSignature);

		final OperationType operationType = ArchitectureFactory.eINSTANCE.createOperationType();
		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_1);
		operationType.setComponentType(this.componentType);

		operationType.setSignature(EXAMPLE_OPERATION_SIGNATURE_2);

		final OperationType returnedOperationType = index.get(EXAMPLE_OPERATION_SIGNATURE_2);

		Assert.assertSame(operationType, returnedOperationType);
	}

}
