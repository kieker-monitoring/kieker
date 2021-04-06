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

import java.util.function.Function;

import kieker.analysis.statistics.calculating.CountCalculator;
import kieker.analysis.statistics.calculating.MaxCalculator;
import kieker.analysis.statistics.calculating.MeanCalculator;
import kieker.analysis.statistics.calculating.MedianCalculator;
import kieker.analysis.statistics.calculating.MinCalculator;
import kieker.analysis.statistics.calculating.TotalCalculator;
import kieker.model.analysismodel.statistics.EPredefinedUnits;
import kieker.model.analysismodel.statistics.StatisticsModel;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 *
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public class FullStatisticsDecoratorStage<T> extends CompositeStage {

	private final StatisticsDecoratorStage<T> countStatistics;
	private final StatisticsDecoratorStage<T> medianStatistics;

	public FullStatisticsDecoratorStage(final StatisticsModel statisticsModel, final EPredefinedUnits unit, final Function<T, Long> valueAccessor,
			final Function<T, Object> objectAccesor) {

		this.countStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new CountCalculator<>(), objectAccesor);
		final StatisticsDecoratorStage<T> totalStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new TotalCalculator<>(valueAccessor),
				objectAccesor);
		final StatisticsDecoratorStage<T> minStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new MinCalculator<>(valueAccessor), objectAccesor);
		final StatisticsDecoratorStage<T> maxStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new MaxCalculator<>(valueAccessor), objectAccesor);
		final StatisticsDecoratorStage<T> averageStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new MeanCalculator<>(), objectAccesor);
		this.medianStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new MedianCalculator<>(valueAccessor), objectAccesor);

		super.connectPorts(this.countStatistics.getOutputPort(), totalStatistics.getInputPort());
		super.connectPorts(totalStatistics.getOutputPort(), minStatistics.getInputPort());
		super.connectPorts(minStatistics.getOutputPort(), maxStatistics.getInputPort());
		super.connectPorts(maxStatistics.getOutputPort(), averageStatistics.getInputPort());
		super.connectPorts(averageStatistics.getOutputPort(), this.medianStatistics.getInputPort());
	}

	public InputPort<T> getInputPort() {
		return this.countStatistics.getInputPort();
	}

	public OutputPort<T> getOutputPort() {
		return this.medianStatistics.getOutputPort();
	}

}
