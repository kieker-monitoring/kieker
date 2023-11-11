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
package kieker.tools.restructuring.stages;

import kieker.tools.restructuring.restructuremodel.TransformationModel;
import kieker.tools.restructuring.stages.exec.OutputModelCreator;
import kieker.tools.restructuring.stages.exec.RestructureStepFinder;
import kieker.tools.restructuring.util.TransformationFactory;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Serafim Simonov -- initial contribution
 * @author Reiner Jung
 * @since 1.3.0
 */
public class GenerateRestructureModelStage extends AbstractTransformation<RestructureStepFinder, TransformationModel> {

	@Override
	protected void execute(final RestructureStepFinder element) throws Exception {
		if (TransformationFactory.areSameModels(element.getGoal(), element.getOriginal())) {
			final OutputModelCreator output = new OutputModelCreator();
			final String filename = String.format("%s-%s", element.getComponentMapper().getOriginalModelName(),
					element.getComponentMapper().getGoalModelName());
			this.outputPort.send(output.createOutputModel(filename, element.getSteps()));
		} else {
			this.logger.error("Faulty sequence");
		}
	}

}
