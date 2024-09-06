/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import org.eclipse.emf.ecore.EObject;

import kieker.analysis.statistics.calculating.AbstractCalculator;
import kieker.model.analysismodel.statistics.StatisticRecord;
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
	private final AbstractCalculator<T> statisticCalculator;
	private final Function<T, EObject> objectAccesor;

	public StatisticsDecorator(final StatisticsModel statisticsModel, final AbstractCalculator<T> statisticCalculator,
			final Function<T, EObject> objectAccesor) {
		this.statisticsModel = statisticsModel;
		this.statisticCalculator = statisticCalculator;
		this.objectAccesor = objectAccesor;
	}

	public void decorate(final T input) {
		final EObject object = this.objectAccesor.apply(input);
		StatisticRecord statistic = this.statisticsModel.getStatistics().get(object);
		if (statistic == null) {
			this.statisticsModel.getStatistics().put(object, StatisticsFactory.eINSTANCE.createStatisticRecord());
			statistic = this.statisticsModel.getStatistics().get(object);
		}
		this.statisticCalculator.calculate(statistic, input, object);
	}

}
