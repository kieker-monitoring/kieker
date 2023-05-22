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

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.signature.IComponentSignatureExtractor;
import kieker.analysis.architecture.recovery.signature.IOperationSignatureExtractor;
import kieker.analysis.architecture.recovery.signature.JavaComponentSignatureExtractor;
import kieker.analysis.architecture.recovery.signature.JavaOperationSignatureExtractor;
import kieker.model.analysismodel.source.SourceFactory;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class OperationTypeModelAssemblerTest { // NOCS test do not need constructors

	private static final String LABEL = "FIRST";
	private static final String COMPONENT_TYPE_SIGNATURE = "component.name";
	private static final String OPERATION_TYPE_SIGNATURE = "int function(Parameter a)";
	private static final String HOSTNAME = "test-host";
	private TypeModel typeModel;
	private SourceModel sourceModel;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.typeModel = TypeFactory.eINSTANCE.createTypeModel();
		this.sourceModel = SourceFactory.eINSTANCE.createSourceModel();
	}

	/**
	 * Test method for {@link kieker.analysis.architecture.recovery.operation.OperationTypeModelAssembler#addEvent(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddRecordStringString() {
		final IComponentSignatureExtractor componentSignatureExtractor = new JavaComponentSignatureExtractor();
		final IOperationSignatureExtractor operationSignatureExtractor = new JavaOperationSignatureExtractor();
		final OperationTypeModelAssembler assembler = new OperationTypeModelAssembler(this.typeModel, this.sourceModel, OperationTypeModelAssemblerTest.LABEL,
				componentSignatureExtractor,
				operationSignatureExtractor);
		assembler.addOperation(new OperationEvent(OperationTypeModelAssemblerTest.HOSTNAME, OperationTypeModelAssemblerTest.COMPONENT_TYPE_SIGNATURE,
				OperationTypeModelAssemblerTest.OPERATION_TYPE_SIGNATURE));

		// check type model

		// check sources model
		final ComponentType type = this.typeModel.getComponentTypes().values().iterator().next();

		final EList<String> list = this.sourceModel.getSources().get(type);
		Assert.assertEquals("Number of labels must be 1", 1, list.size());
		Assert.assertEquals("Label is not " + OperationTypeModelAssemblerTest.LABEL, OperationTypeModelAssemblerTest.LABEL, list.get(0));

		// Extend model
		assembler.addOperation(new OperationEvent(OperationTypeModelAssemblerTest.HOSTNAME, OperationTypeModelAssemblerTest.COMPONENT_TYPE_SIGNATURE,
				OperationTypeModelAssemblerTest.OPERATION_TYPE_SIGNATURE));
		assembler.addOperation(new OperationEvent(OperationTypeModelAssemblerTest.HOSTNAME, OperationTypeModelAssemblerTest.COMPONENT_TYPE_SIGNATURE,
				OperationTypeModelAssemblerTest.OPERATION_TYPE_SIGNATURE));

		// check type model

		// check sources model
		final ComponentType type2 = this.typeModel.getComponentTypes().values().iterator().next();

		final EList<String> list2 = this.sourceModel.getSources().get(type2);
		Assert.assertEquals("Number of labels must be 1", 1, list2.size());
		Assert.assertEquals("Label is not " + OperationTypeModelAssemblerTest.LABEL, OperationTypeModelAssemblerTest.LABEL, list2.get(0));
	}

}
