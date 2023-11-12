/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.tools.restructuring.transformations.CreateTransformation;
import kieker.tools.restructuring.transformations.CutTransformation;
import kieker.tools.restructuring.transformations.DeleteTransformation;
import kieker.tools.restructuring.transformations.MoveTransformation;
import kieker.tools.restructuring.transformations.PasteTransformation;

class ModelTransformationTest {

	private AssemblyModel model;

	@BeforeEach
	void init() {
		/// AssemblyPackage.eINSTANCE;
		final AssemblyFactory factory = AssemblyFactory.eINSTANCE;

		this.model = factory.createAssemblyModel();

		final AssemblyComponent comp1 = factory.createAssemblyComponent();
		final AssemblyComponent comp2 = factory.createAssemblyComponent();

		this.model.getComponents().put("comp1", comp1);
		this.model.getComponents().put("comp2", comp2);
		// this.model.getComponents().add(c2);

		final AssemblyOperation comp1a = factory.createAssemblyOperation();
		final AssemblyOperation comp1b = factory.createAssemblyOperation();

		final AssemblyOperation comp2a = factory.createAssemblyOperation();

		this.model.getComponents().get("comp1").getOperations().put("1a", comp1a);
		this.model.getComponents().get("comp1").getOperations().put("1b", comp1b);
		this.model.getComponents().get("comp2").getOperations().put("1a", comp2a);
		// this.model.getComponents().get(comp1).getOperations().add(b1);
		// this.model.getComponents().get(comp2).getOperations().add(a2);
		// comp1.getOperations().add(a1);
		// comp1.getOperations().add(b1);
		// comp2.getOperations().add(a2);

	}

	@AfterEach
	void reset() {
		this.model = null;
	}

	@Test
	void test() {
		final boolean result = this.model.getComponents().containsKey("comp1");
		Assert.assertTrue(result);
	}

	@Test
	void testCreate() {
		final CreateTransformation transform = new CreateTransformation(null);
		transform.setComponentName("comp3");
		transform.applyTransformation(this.model);
		final boolean result = transform.getModel().getComponents().containsKey("comp3");
		Assert.assertTrue(result);

	}

	@Test
	void testDelete() {
		final DeleteTransformation transform = new DeleteTransformation(null);
		transform.setComponentName("comp2");
		transform.applyTransformation(this.model);
		final boolean result = transform.getModel().getComponents().containsKey("comp2");
		Assertions.assertFalse(result);
	}

	@Test
	void testCut() {
		final CutTransformation transform = new CutTransformation(null);
		transform.setComponentName("comp1");
		transform.setOperationName("1a");
		transform.applyTransformation(this.model);
		final boolean a = transform.getModel().getComponents().containsKey("comp1");
		final boolean b = transform.getModel().getComponents().get("comp1").getOperations().containsKey("1a");
		Assert.assertTrue(a);
		Assertions.assertFalse(b);
	}

	@Test
	void testPut() {
		final PasteTransformation transform = new PasteTransformation(null);
		transform.setComponentName("comp1");
		transform.setOperationName("1c");

		transform.applyTransformation(this.model);
		final boolean a = transform.getModel().getComponents().containsKey("comp1");
		final boolean b = transform.getModel().getComponents().get("comp1").getOperations().containsKey("1c");
		Assert.assertTrue(a);
		Assert.assertTrue(b);
	}

	@Test
	void testMerge() {
		final MoveTransformation transform = new MoveTransformation(this.model);

		Assert.assertTrue(true);
	}

	@Test
	void testSplit() {
		Assert.assertTrue(true);
	}

	void testMove() {
		Assert.assertTrue(true);
	}

}
