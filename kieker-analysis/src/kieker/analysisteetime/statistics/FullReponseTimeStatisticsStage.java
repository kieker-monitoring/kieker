/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.statistics;

import java.util.function.Function;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;

/**
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public class FullReponseTimeStatisticsStage extends FullStatisticsDecoratorStage<OperationCall> {

	public FullReponseTimeStatisticsStage(final StatisticsModel statisticsModel, final Function<OperationCall, Object> objectAccesor) {
		super(statisticsModel, Units.RESPONSE_TIME, c -> c.getDuration().toNanos(), objectAccesor);
	}

}
