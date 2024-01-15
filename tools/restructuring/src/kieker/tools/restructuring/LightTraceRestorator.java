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

import kieker.analysis.exception.InternalErrorException;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.tools.restructuring.mapper.BasicComponentMapper;
import kieker.tools.restructuring.mapper.KuhnMatcherMapper;
import kieker.tools.restructuring.stages.exec.RestructureStepFinder;

/**
 *
 * @author Serafim Simonov
 * @since 2.0.0
 */
public class LightTraceRestorator {

	private final AssemblyModel original;
	private final AssemblyModel goal;

	public LightTraceRestorator(final AssemblyModel original, final AssemblyModel goal) {
		this.original = original;
		this.goal = goal;
	}

	public int getNumSteps() throws InternalErrorException {
		// TODO find better names
		final BasicComponentMapper mapper = new KuhnMatcherMapper(this.original, this.goal, "original", "goal");

		final RestructureStepFinder stepfinder = new RestructureStepFinder(mapper);
		stepfinder.findTransformation();

		return stepfinder.getNumberOfSteps();
	}

}
