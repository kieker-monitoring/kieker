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
import org.junit.Test;

import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.sources.SourcesFactory;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeFactory;

/**
 * Test the abstract model assembler which updates the sources model.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class AbstractModelAssemblerTest { // NOCS NOPMD this is a test class

	private static final String LABEL = "LABEL";
	private static final String SECOND = "SECOND";

	@Test
	public void testUpdateSourceModel() {
		final SourceModel model = SourcesFactory.eINSTANCE.createSourceModel();
		final ComponentType[] types = {
			this.createType("Type1"), this.createType("Type2"), this.createType("Type3"), this.createType("Type4"),
		};
		final AbstractSourceModelAssembler assembler = new AbstractSourceModelAssembler(model, LABEL) {
		};
		for (final ComponentType type : types) {
			assembler.updateSourceModel(type);
		}
		for (final ComponentType type : types) {
			assembler.updateSourceModel(type);
		}
		for (final ComponentType type : types) {
			assembler.updateSourceModel(type);
		}
		for (final ComponentType type : types) {
			final EList<String> labels = model.getSources().get(type);
			Assert.assertEquals("The list must have exactly the size 1, but", 1, labels.size());
			Assert.assertEquals("The list values must be " + LABEL, LABEL, labels.get(0));
		}
	}

	@Test
	public void testUpdateSourceModel2Label() {
		final SourceModel model = SourcesFactory.eINSTANCE.createSourceModel();
		final ComponentType[] types = {
			this.createType("Type1"), this.createType("Type2"), this.createType("Type3"), this.createType("Type4"),
		};
		final AbstractSourceModelAssembler assembler = new AbstractSourceModelAssembler(model, LABEL) {
		};
		for (final ComponentType type : types) {
			assembler.updateSourceModel(type);
		}
		for (final ComponentType type : types) {
			assembler.updateSourceModel(type);
		}
		final AbstractSourceModelAssembler assembler2 = new AbstractSourceModelAssembler(model, SECOND) {
		};
		for (final ComponentType type : types) {
			assembler2.updateSourceModel(type);
			assembler2.updateSourceModel(type);
		}
		for (final ComponentType type : types) {
			final EList<String> labels = model.getSources().get(type);
			Assert.assertEquals("The list must have exactly the size 1, but", 2, labels.size());
			Assert.assertEquals("The list values must be " + LABEL, LABEL, labels.get(0));
			Assert.assertEquals("The list values must be " + SECOND, SECOND, labels.get(1));
		}
	}

	private ComponentType createType(final String name) {
		final ComponentType type = TypeFactory.eINSTANCE.createComponentType();
		type.setName(name);

		return type;
	}

}
