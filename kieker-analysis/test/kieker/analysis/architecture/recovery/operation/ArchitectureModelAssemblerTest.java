/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.architecture.recovery.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.signature.JavaComponentSignatureExtractor;
import kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractor;
import kieker.model.analysismodel.source.SourceFactory;
import kieker.model.analysismodel.source.SourceModel;
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

	private final OperationEvent beforeOperationEvent1 = new OperationEvent(ArchitectureModelAssemblerTest.EXAMPLE_HOSTNAME,
			ArchitectureModelAssemblerTest.EXAMPLE_CLASS_SIGNATURE_1, ArchitectureModelAssemblerTest.EXAMPLE_OPERATION_SIGNATURE_1);
	private final OperationEvent beforeOperationEvent2 = new OperationEvent(ArchitectureModelAssemblerTest.EXAMPLE_HOSTNAME,
			ArchitectureModelAssemblerTest.EXAMPLE_CLASS_SIGNATURE_1, ArchitectureModelAssemblerTest.EXAMPLE_OPERATION_SIGNATURE_2);
	private final OperationEvent beforeOperationEvent3 = new OperationEvent(ArchitectureModelAssemblerTest.EXAMPLE_HOSTNAME,
			ArchitectureModelAssemblerTest.EXAMPLE_CLASS_SIGNATURE_2, ArchitectureModelAssemblerTest.EXAMPLE_OPERATION_SIGNATURE_3);

	private final TypeFactory factory = TypeFactory.eINSTANCE;

	public ArchitectureModelAssemblerTest() {
		// default empty constructor
	}

	/**
	 * Test method for
	 * {@link kieker.analysis.architecture.recovery.operation.OperationTypeModelAssembler#addOperation(kieker.analysis.architecture.recovery.events.OperationEvent)}.
	 */
	@Test
	public void testComponentsExistsAfterAddRecordFromRecord() {
		final TypeModel typeModel = this.factory.createTypeModel();
		final SourceModel sourcesModel = SourceFactory.eINSTANCE.createSourceModel();
		final OperationTypeModelAssembler typeModelAssembler = new OperationTypeModelAssembler(typeModel, sourcesModel, ArchitectureModelAssemblerTest.TEST_SOURCE,
				new JavaComponentSignatureExtractor(),
				new JavaOperationSignatureExtractor());

		typeModelAssembler.addOperation(this.beforeOperationEvent1);
		typeModelAssembler.addOperation(this.beforeOperationEvent2);
		typeModelAssembler.addOperation(this.beforeOperationEvent3);

		final List<String> actualList = typeModel.getComponentTypes().values().stream().map(c -> c.getSignature()).collect(Collectors.toList());
		final List<String> expectedList = Arrays.asList(ArchitectureModelAssemblerTest.EXAMPLE_CLASS_SIGNATURE_1,
				ArchitectureModelAssemblerTest.EXAMPLE_CLASS_SIGNATURE_2);
		Collections.sort(actualList);
		Collections.sort(expectedList);

		Assert.assertTrue(actualList.equals(expectedList));

	}

}
