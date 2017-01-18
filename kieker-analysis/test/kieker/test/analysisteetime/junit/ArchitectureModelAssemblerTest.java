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

package kieker.test.analysisteetime.junit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysisteetime.TypeModelAssembler;
import kieker.analysisteetime.model.analysismodel.type.TypeFactory;
import kieker.analysisteetime.model.analysismodel.type.TypeRoot;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ArchitectureModelAssemblerTest {

	private final static String EXAMPLE_OPERATION_SIGNATURE_1 = "public void doSomething()";
	private final static String EXAMPLE_OPERATION_SIGNATURE_2 = "private void doSomethingDifferent()";
	private final static String EXAMPLE_OPERATION_SIGNATURE_3 = "public String getSomeString()";

	private static final String EXAMPLE_CLASS_SIGNATURE_1 = "org.package.FirstClass";
	private static final String EXAMPLE_CLASS_SIGNATURE_2 = "org.package.SecondClass";

	private final BeforeOperationEvent beforeOperationEvent1 = new BeforeOperationEvent(0, 0, 0, EXAMPLE_OPERATION_SIGNATURE_1, EXAMPLE_CLASS_SIGNATURE_1);
	private final BeforeOperationEvent beforeOperationEvent2 = new BeforeOperationEvent(0, 0, 0, EXAMPLE_OPERATION_SIGNATURE_2, EXAMPLE_CLASS_SIGNATURE_1);
	private final BeforeOperationEvent beforeOperationEvent3 = new BeforeOperationEvent(0, 0, 0, EXAMPLE_OPERATION_SIGNATURE_3, EXAMPLE_CLASS_SIGNATURE_2);

	private final TypeFactory factory = TypeFactory.eINSTANCE;

	/**
	 * Test method for {@link kieker.analysisteetime.TypeModelAssembler#addRecord(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testComponentsExistsAfterAddRecordFromRecord() {
		final TypeRoot typeRoot = this.factory.createTypeRoot();
		final TypeModelAssembler architectureModelAssembler = new TypeModelAssembler(typeRoot);

		architectureModelAssembler.addRecord(this.beforeOperationEvent1);
		architectureModelAssembler.addRecord(this.beforeOperationEvent2);
		architectureModelAssembler.addRecord(this.beforeOperationEvent3);

		final List<String> actualList = typeRoot.getComponentTypes().values().stream().map(c -> c.getSignature()).collect(Collectors.toList());
		final List<String> expectedList = Arrays.asList(EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_CLASS_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));

	}

	/**
	 * Test method for {@link kieker.analysisteetime.TypeModelAssembler#addRecord(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testComponentKeysExistsAfterAddRecordFromRecord() {
		final TypeRoot typeRoot = this.factory.createTypeRoot();
		final TypeModelAssembler architectureModelAssembler = new TypeModelAssembler(typeRoot);

		architectureModelAssembler.addRecord(this.beforeOperationEvent1);
		architectureModelAssembler.addRecord(this.beforeOperationEvent2);
		architectureModelAssembler.addRecord(this.beforeOperationEvent3);

		final List<String> actualList = new ArrayList<>(typeRoot.getComponentTypes().keySet());
		final List<String> expectedList = Arrays.asList(EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_CLASS_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));
	}

	/**
	 * Test method for {@link kieker.analysisteetime.TypeModelAssembler#addRecord(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testOperationExistsAfterAddRecordFromRecord() {
		final TypeRoot typeRoot = this.factory.createTypeRoot();
		final TypeModelAssembler architectureModelAssembler = new TypeModelAssembler(typeRoot);

		architectureModelAssembler.addRecord(this.beforeOperationEvent1);
		architectureModelAssembler.addRecord(this.beforeOperationEvent2);
		architectureModelAssembler.addRecord(this.beforeOperationEvent3);

		final List<String> actualList = typeRoot.getComponentTypes().get(EXAMPLE_CLASS_SIGNATURE_1).getProvidedOperations().values().stream()
				.map(c -> c.getSignature()).collect(Collectors.toList());
		final List<String> expectedList = Arrays.asList(EXAMPLE_OPERATION_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));
	}

	/**
	 * Test method for {@link kieker.analysisteetime.TypeModelAssembler#addRecord(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testOperationKeysExistsAfterAddRecordFromRecord() {
		final TypeRoot typeRoot = this.factory.createTypeRoot();
		final TypeModelAssembler architectureModelAssembler = new TypeModelAssembler(typeRoot);

		architectureModelAssembler.addRecord(this.beforeOperationEvent1);
		architectureModelAssembler.addRecord(this.beforeOperationEvent2);
		architectureModelAssembler.addRecord(this.beforeOperationEvent3);

		final List<String> actualList = new ArrayList<>(typeRoot.getComponentTypes().get(EXAMPLE_CLASS_SIGNATURE_1).getProvidedOperations().keySet());
		final List<String> expectedList = Arrays.asList(EXAMPLE_OPERATION_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));
	}

	/**
	 * Test method for {@link kieker.analysisteetime.TypeModelAssembler#addRecord(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testOperationExistsAfterAddRecordFromString() {
		final TypeRoot typeRoot = this.factory.createTypeRoot();
		final TypeModelAssembler architectureModelAssembler = new TypeModelAssembler(typeRoot);

		architectureModelAssembler.addRecord(EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_1);
		architectureModelAssembler.addRecord(EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
		architectureModelAssembler.addRecord(EXAMPLE_CLASS_SIGNATURE_2, EXAMPLE_OPERATION_SIGNATURE_3);

		final List<String> actualList = typeRoot.getComponentTypes().get(EXAMPLE_CLASS_SIGNATURE_1).getProvidedOperations().values().stream()
				.map(c -> c.getSignature()).collect(Collectors.toList());
		final List<String> expectedList = Arrays.asList(EXAMPLE_OPERATION_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));
	}

}
