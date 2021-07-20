/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.model;

import org.eclipse.emf.common.util.EList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.signature.IComponentSignatureExtractor;
import kieker.analysis.signature.IOperationSignatureExtractor;
import kieker.analysis.signature.JavaComponentSignatureExtractor;
import kieker.analysis.signature.JavaOperationSignatureExtractor;
import kieker.analysis.stage.model.TypeModelAssembler;
import kieker.analysis.stage.model.data.OperationEvent;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.sources.SourcesFactory;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class TypeModelAssemblerTest { // NOCS test do not need constructors

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
		this.sourceModel = SourcesFactory.eINSTANCE.createSourceModel();
	}

	/**
	 * Test method for {@link kieker.analysis.stage.model.TypeModelAssembler#addOperation(kieker.common.record.flow.IOperationRecord)}.
	 */
	@Test
	public void testAddRecordIOperationRecord() {

	}

	/**
	 * Test method for {@link kieker.analysis.stage.model.TypeModelAssembler#addEvent(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddRecordStringString() {
		final IComponentSignatureExtractor componentSignatureExtractor = new JavaComponentSignatureExtractor();
		final IOperationSignatureExtractor operationSignatureExtractor = new JavaOperationSignatureExtractor();
		final TypeModelAssembler assembler = new TypeModelAssembler(this.typeModel, this.sourceModel, LABEL, componentSignatureExtractor,
				operationSignatureExtractor);
		assembler.addOperation(new OperationEvent(HOSTNAME, COMPONENT_TYPE_SIGNATURE, OPERATION_TYPE_SIGNATURE));

		// check type model

		// check sources model
		final ComponentType type = this.typeModel.getComponentTypes().values().iterator().next();

		final EList<String> list = this.sourceModel.getSources().get(type);
		Assert.assertEquals("Number of labels must be 1", 1, list.size());
		Assert.assertEquals("Label is not " + LABEL, LABEL, list.get(0));

		// Extend model
		assembler.addOperation(new OperationEvent(HOSTNAME, COMPONENT_TYPE_SIGNATURE, OPERATION_TYPE_SIGNATURE));
		assembler.addOperation(new OperationEvent(HOSTNAME, COMPONENT_TYPE_SIGNATURE, OPERATION_TYPE_SIGNATURE));

		// check type model

		// check sources model
		final ComponentType type2 = this.typeModel.getComponentTypes().values().iterator().next();

		final EList<String> list2 = this.sourceModel.getSources().get(type2);
		Assert.assertEquals("Number of labels must be 1", 1, list2.size());
		Assert.assertEquals("Label is not " + LABEL, LABEL, list2.get(0));
	}

}
