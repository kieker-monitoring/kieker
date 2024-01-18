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
package kieker.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 2.0.0
 */
public class MoveTransformation extends AbstractCompositeTransformation {

	public MoveTransformation(final AssemblyModel model) {
		super(model);
	}

	@Override
	public void applyTransformation(final AssemblyModel model) {

		// TODO CHECK IF THE LIST FORMAT IS APPROPRIETE
		// 1ST OPERATION Split other operation move
		for (final AbstractTransformationStep t : this.steps) {
			t.applyTransformation(model);
		}
		this.model = model;
	}

	public void add(final AbstractTransformationStep transformation) {
		this.steps.add(transformation);
	}

	public CutTransformation getCutTransformation() {
		return (CutTransformation) this.steps.get(0);
	}

	public PasteTransformation getPasteTransformation() {
		return (PasteTransformation) this.steps.get(1);
	}
}
