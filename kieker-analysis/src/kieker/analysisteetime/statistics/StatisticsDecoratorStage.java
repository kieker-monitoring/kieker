/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Map;
import java.util.function.Function;

import kieker.analysisteetime.statistics.calculating.Calculator;

import teetime.stage.basic.AbstractFilter;

public class StatisticsDecoratorStage<T> extends AbstractFilter<T> {

	private final StatisticsDecorator<T> statisticsDecorator;

	public StatisticsDecoratorStage(final Map<Object, Statistics> statisticsModel, final Unit unit, final Calculator<T> statisticCalculator,
			final Function<T, Object> objectAccesor) {
		this.statisticsDecorator = new StatisticsDecorator<>(statisticsModel, unit, statisticCalculator, objectAccesor);
	}

	@Override
	protected void execute(final T element) {
		this.statisticsDecorator.decorate(element);
		this.outputPort.send(element);
	}

}
