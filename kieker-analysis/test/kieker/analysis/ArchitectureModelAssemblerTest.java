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

package kieker.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.model.TypeModelAssembler;
import kieker.analysis.model.data.OperationEvent;
import kieker.analysis.signature.JavaComponentSignatureExtractor;
import kieker.analysis.signature.JavaOperationSignatureExtractor;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.sources.SourcesFactory;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class ArchitectureModelAssemblerTest {

	private static final String EXAMPLE_HOSTNAME = "example-host";

	private static final String EXAMPLE_OPERATION_SIGNATURE_1 = "public void doSomething()";
	private static final String EXAMPLE_OPERATION_SIGNATURE_2 = "private void doSomethingDifferent()";
	private static final String EXAMPLE_OPERATION_SIGNATURE_3 = "public String getSomeString()";

	private static final String EXAMPLE_CLASS_SIGNATURE_1 = "org.package.FirstClass";
	private static final String EXAMPLE_CLASS_SIGNATURE_2 = "org.package.SecondClass";
	private static final String TEST_SOURCE = "test-source";

	private final OperationEvent beforeOperationEvent1 = new OperationEvent(EXAMPLE_HOSTNAME, EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_1);
	private final OperationEvent beforeOperationEvent2 = new OperationEvent(EXAMPLE_HOSTNAME, EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
	private final OperationEvent beforeOperationEvent3 = new OperationEvent(EXAMPLE_HOSTNAME, EXAMPLE_CLASS_SIGNATURE_2, EXAMPLE_OPERATION_SIGNATURE_3);

	private final TypeFactory factory = TypeFactory.eINSTANCE;

	public ArchitectureModelAssemblerTest() {
		// default empty constructor
	}

	/**
	 * Test method for {@link kieker.analysis.model.TypeModelAssembler#addOperation(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testComponentsExistsAfterAddRecordFromRecord() {
		final TypeModel typeModel = this.factory.createTypeModel();
		final SourceModel sourceModel = SourcesFactory.eINSTANCE.createSourceModel();
		final TypeModelAssembler typeModelAssembler = new TypeModelAssembler(typeModel, sourceModel, TEST_SOURCE, new JavaComponentSignatureExtractor(),
				new JavaOperationSignatureExtractor());

		typeModelAssembler.addOperation(this.beforeOperationEvent1);
		typeModelAssembler.addOperation(this.beforeOperationEvent2);
		typeModelAssembler.addOperation(this.beforeOperationEvent3);

		final List<String> actualList = typeModel.getComponentTypes().values().stream().map(c -> c.getSignature()).collect(Collectors.toList());
		final List<String> expectedList = Arrays.asList(EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_CLASS_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));

	}

	/**
	 * Test method for {@link kieker.analysis.model.TypeModelAssembler#addOperation(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testComponentKeysExistsAfterAddRecordFromRecord() {
		final TypeModel typeModel = this.factory.createTypeModel();
		final SourceModel sourceModel = SourcesFactory.eINSTANCE.createSourceModel();

		final TypeModelAssembler typeModelAssembler = new TypeModelAssembler(typeModel, sourceModel, TEST_SOURCE, new JavaComponentSignatureExtractor(),
				new JavaOperationSignatureExtractor());

		typeModelAssembler.addOperation(this.beforeOperationEvent1);
		typeModelAssembler.addOperation(this.beforeOperationEvent2);
		typeModelAssembler.addOperation(this.beforeOperationEvent3);

		final List<String> actualList = new ArrayList<>(typeModel.getComponentTypes().keySet());
		final List<String> expectedList = Arrays.asList(EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_CLASS_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));
	}

	/**
	 * Test method for {@link kieker.analysis.model.TypeModelAssembler#addOperation(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testOperationExistsAfterAddRecordFromRecord() {
		final TypeModel typeModel = this.factory.createTypeModel();
		final SourceModel sourceModel = SourcesFactory.eINSTANCE.createSourceModel();

		final TypeModelAssembler typeModelAssembler = new TypeModelAssembler(typeModel, sourceModel, TEST_SOURCE, new JavaComponentSignatureExtractor(),
				new JavaOperationSignatureExtractor());

		typeModelAssembler.addOperation(this.beforeOperationEvent1);
		typeModelAssembler.addOperation(this.beforeOperationEvent2);
		typeModelAssembler.addOperation(this.beforeOperationEvent3);

		final List<String> actualList = typeModel.getComponentTypes().get(EXAMPLE_CLASS_SIGNATURE_1).getProvidedOperations().values().stream()
				.map(c -> c.getSignature()).collect(Collectors.toList());
		final List<String> expectedList = Arrays.asList(EXAMPLE_OPERATION_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));
	}

	/**
	 * Test method for {@link kieker.analysis.model.TypeModelAssembler#addOperation(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testOperationKeysExistsAfterAddRecordFromRecord() {
		final TypeModel typeModel = this.factory.createTypeModel();
		final SourceModel sourceModel = SourcesFactory.eINSTANCE.createSourceModel();

		final TypeModelAssembler typeModelAssembler = new TypeModelAssembler(typeModel, sourceModel, TEST_SOURCE, new JavaComponentSignatureExtractor(),
				new JavaOperationSignatureExtractor());

		typeModelAssembler.addOperation(this.beforeOperationEvent1);
		typeModelAssembler.addOperation(this.beforeOperationEvent2);
		typeModelAssembler.addOperation(this.beforeOperationEvent3);

		final List<String> actualList = new ArrayList<>(typeModel.getComponentTypes().get(EXAMPLE_CLASS_SIGNATURE_1).getProvidedOperations().keySet());
		final List<String> expectedList = Arrays.asList(EXAMPLE_OPERATION_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));
	}

	/**
	 * Test method for {@link kieker.analysis.model.TypeModelAssembler#addEvent(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testOperationExistsAfterAddRecordFromString() {
		final TypeModel typeModel = this.factory.createTypeModel();
		final SourceModel sourceModel = SourcesFactory.eINSTANCE.createSourceModel();

		final TypeModelAssembler typeModelAssembler = new TypeModelAssembler(typeModel, sourceModel, TEST_SOURCE, new JavaComponentSignatureExtractor(),
				new JavaOperationSignatureExtractor());

		typeModelAssembler.addEvent(EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_1);
		typeModelAssembler.addEvent(EXAMPLE_CLASS_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
		typeModelAssembler.addEvent(EXAMPLE_CLASS_SIGNATURE_2, EXAMPLE_OPERATION_SIGNATURE_3);

		final List<String> actualList = typeModel.getComponentTypes().get(EXAMPLE_CLASS_SIGNATURE_1).getProvidedOperations().values().stream()
				.map(c -> c.getSignature()).collect(Collectors.toList());
		final List<String> expectedList = Arrays.asList(EXAMPLE_OPERATION_SIGNATURE_1, EXAMPLE_OPERATION_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));
	}

}
