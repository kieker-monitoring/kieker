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

import java.util.ArrayList;
import java.util.List;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 2.0.0
 */
public class SplitTransformation extends AbstractCompositeTransformation {

	public SplitTransformation(final AssemblyModel model) {
		super(model);
	}

	@Override
	public void applyTransformation(final AssemblyModel model) {
		// TODO CHEC IF THE LIST FORMAT IS APPROPRIETE
		// 1ST OPERATION Split other operation move
		for (final AbstractTransformationStep t : this.steps) {
			t.applyTransformation(model);
		}
		this.model = model;
	}

	public void add(final AbstractTransformationStep step) {
		this.steps.add(step);
	}

	public CreateTransformation getCreateTransformation() {
		return (CreateTransformation) this.steps.get(0);
	}

	public List<AbstractTransformationStep> getMoveTransformation() {
		final List<AbstractTransformationStep> result = new ArrayList<>();
		result.addAll(this.steps);
		result.remove(0);
		return result;

	}

}
