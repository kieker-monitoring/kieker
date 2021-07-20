/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.statistics;

import kieker.analysis.stage.model.ModelObjectFromOperationCallAccessors;
import kieker.analysis.stage.model.data.OperationCallDurationEvent;
import kieker.analysis.statistics.calculating.CountCalculator;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.statistics.EPredefinedUnits;
import kieker.model.analysismodel.statistics.StatisticsModel;

/**
 * Aggregates calls into execution model.
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public class CallStatisticsStage extends StatisticsDecoratorStage<OperationCallDurationEvent> {

	public CallStatisticsStage(final StatisticsModel statisticsModel, final ExecutionModel executionModel) {
		super(statisticsModel, EPredefinedUnits.INVOCATION, new CountCalculator<>(),
				ModelObjectFromOperationCallAccessors.findAggregatedInvocation4OperationTuple(executionModel));
	}

}
