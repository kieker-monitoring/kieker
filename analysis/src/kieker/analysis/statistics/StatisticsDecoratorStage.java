/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import kieker.model.analysismodel.statistics.StatisticsModel;

import teetime.stage.basic.AbstractFilter;

/**
 *
 *
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class StatisticsDecoratorStage<T> extends AbstractFilter<T> {

	private final StatisticsDecorator<T> statisticsDecorator;

	public StatisticsDecoratorStage(final StatisticsModel statisticsModel, final AbstractCalculator<T> statisticCalculator,
			final Function<T, EObject> objectAccesor) {
		this.statisticsDecorator = new StatisticsDecorator<>(statisticsModel, statisticCalculator, objectAccesor);
	}

	@Override
	protected void execute(final T element) {
		this.statisticsDecorator.decorate(element);
		this.outputPort.send(element);
	}

}
