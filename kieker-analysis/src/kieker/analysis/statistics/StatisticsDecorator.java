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

import kieker.analysis.statistics.calculating.ICalculator;
import kieker.model.analysismodel.statistics.EPredefinedUnits;
import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.Statistics;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsModel;

/**
 *
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class StatisticsDecorator<T> {

	private final StatisticsModel statisticsModel;
	private final EPredefinedUnits unit;
	private final ICalculator<T> statisticCalculator;
	private final Function<T, Object> objectAccesor;

	public StatisticsDecorator(final StatisticsModel statisticsModel, final EPredefinedUnits unit, final ICalculator<T> statisticCalculator,
			final Function<T, Object> objectAccesor) {
		this.statisticsModel = statisticsModel;
		this.unit = unit;
		this.statisticCalculator = statisticCalculator;
		this.objectAccesor = objectAccesor;
	}

	public void decorate(final T input) {
		final Object object = this.objectAccesor.apply(input);
		Statistics statistics = this.statisticsModel.getStatistics().get(object);
		if (statistics == null) {
			this.statisticsModel.getStatistics().put(object, StatisticsFactory.eINSTANCE.createStatistics());
			statistics = this.statisticsModel.getStatistics().get(object);
		}
		StatisticRecord statistic = statistics.getStatistics().get(this.unit);
		if (statistic == null) {
			statistics.getStatistics().put(this.unit, StatisticsFactory.eINSTANCE.createStatisticRecord());
			statistic = statistics.getStatistics().get(this.unit);
		}
		this.statisticCalculator.calculate(statistic, input, object);
	}

}
