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

import kieker.analysisteetime.statistics.calculating.Calculator;

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
	private final Unit unit;
	private final Calculator<T> statisticCalculator;
	private final Function<T, Object> objectAccesor;

	public StatisticsDecorator(final StatisticsModel statisticsModel, final Unit unit, final Calculator<T> statisticCalculator,
			final Function<T, Object> objectAccesor) {
		this.statisticsModel = statisticsModel;
		this.unit = unit;
		this.statisticCalculator = statisticCalculator;
		this.objectAccesor = objectAccesor;
	}

	public void decorate(final T input) {
		final Object object = this.objectAccesor.apply(input);
		final Statistic statistic = this.statisticsModel.get(object).getStatistic(this.unit);
		this.statisticCalculator.calculate(statistic, input, object);
	}

}
