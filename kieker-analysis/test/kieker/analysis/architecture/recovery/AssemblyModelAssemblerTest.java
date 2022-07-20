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
package kieker.analysis.architecture.recovery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeModel;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class AssemblyModelAssemblerTest {

	private TypeModel typeModel;
	private AssemblyModel assemblyModel;
	private SourceModel sourceModel;

	@Before
	public void setUp() {
		this.typeModel = RecoveryModelUtils.createTypeModel();
		this.assemblyModel = RecoveryModelUtils.createEmptyAssemblyModel();
		this.sourceModel = RecoveryModelUtils.createEmptySourceModel();

	}

	@Test
	public void testAddOperationOperationEvent() {
		final AssemblyModelAssembler assembler = new AssemblyModelAssembler(this.typeModel, this.assemblyModel, this.sourceModel, RecoveryModelUtils.SOURCE_LABEL);
		final OperationEvent operation = new OperationEvent(RecoveryModelUtils.HOST_NAME, RecoveryModelUtils.COMPONENT_SIGNATURE,
				RecoveryModelUtils.OPERATION_SIGNATURE);
		assembler.addOperation(operation);

		Assert.assertEquals("Component list must not be empty", 1, this.assemblyModel.getComponents().size());
		final AssemblyComponent assemblyComponent = this.assemblyModel.getComponents().get(RecoveryModelUtils.COMPONENT_SIGNATURE);
		Assert.assertNotNull("Must have an assembly component,", assemblyComponent);
		final ComponentType foundComponentType = this.typeModel.getComponentTypes().get(RecoveryModelUtils.COMPONENT_SIGNATURE);
		Assert.assertEquals("Must be the test component", foundComponentType, assemblyComponent.getComponentType());

		Assert.assertEquals("Operation list must not be empty", 1, assemblyComponent.getOperations().size());
		final AssemblyOperation foundOperation = assemblyComponent.getOperations().get(RecoveryModelUtils.OPERATION_SIGNATURE);
		Assert.assertNotNull("Must have one assembly operation", foundOperation);
		Assert.assertEquals("Must be the correct operation", foundComponentType.getProvidedOperations().get(RecoveryModelUtils.OPERATION_SIGNATURE),
				foundOperation.getOperationType());
		Assert.assertEquals("Operation must refer to correct component", assemblyComponent, foundOperation.getComponent());
	}

	@Test
	public void testAddOperationStringString() {
		final AssemblyModelAssembler assembler = new AssemblyModelAssembler(this.typeModel, this.assemblyModel, this.sourceModel, RecoveryModelUtils.SOURCE_LABEL);
		assembler.addOperation(RecoveryModelUtils.COMPONENT_SIGNATURE, RecoveryModelUtils.OPERATION_SIGNATURE);

		Assert.assertEquals("Component list must not be empty", 1, this.assemblyModel.getComponents().size());
		final AssemblyComponent assemblyComponent = this.assemblyModel.getComponents().get(RecoveryModelUtils.COMPONENT_SIGNATURE);
		Assert.assertNotNull("Must have an assembly component,", assemblyComponent);
		final ComponentType foundComponentType = this.typeModel.getComponentTypes().get(RecoveryModelUtils.COMPONENT_SIGNATURE);
		Assert.assertEquals("Must be the test component", foundComponentType, assemblyComponent.getComponentType());

		Assert.assertEquals("Operation list must not be empty", 1, assemblyComponent.getOperations().size());
		final AssemblyOperation foundOperation = assemblyComponent.getOperations().get(RecoveryModelUtils.OPERATION_SIGNATURE);
		Assert.assertNotNull("Must have one assembly operation", foundOperation);
		Assert.assertEquals("Must be the correct operation", foundComponentType.getProvidedOperations().get(RecoveryModelUtils.OPERATION_SIGNATURE),
				foundOperation.getOperationType());
		Assert.assertEquals("Operation must refer to correct component", assemblyComponent, foundOperation.getComponent());
	}

}
