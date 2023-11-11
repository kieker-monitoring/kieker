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

import kieker.tools.restructuring.mapper.BasicComponentMapper;
import kieker.tools.restructuring.stages.exec.RestructureStepFinder;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class RestructurerStage extends AbstractConsumerStage<BasicComponentMapper> {

	// protected final InputPort<ComponentsMapper> compMapperPort =
	// this.createInputPort();

	protected final OutputPort<RestructureStepFinder> stepsOutputPort = this.createOutputPort();
	private final OutputPort<ResultRecord> numberOfStepsOutputPort = this.createOutputPort(ResultRecord.class);

	public OutputPort<RestructureStepFinder> getStepsOutputPort() {
		return this.stepsOutputPort;
	}

	public OutputPort<ResultRecord> getNumberOfStepsOutputPort() {
		return this.numberOfStepsOutputPort;
	}

	@Override
	protected void execute(final BasicComponentMapper mapper) throws Exception {
		final RestructureStepFinder restructureStepsFinder = new RestructureStepFinder(mapper);
		restructureStepsFinder.findTransformation();

		this.stepsOutputPort.send(restructureStepsFinder);
		this.numberOfStepsOutputPort.send(new ResultRecord(mapper.getOriginalModelName(), mapper.getGoalModelName(),
				restructureStepsFinder.getNumberOfSteps()));
	}
}
