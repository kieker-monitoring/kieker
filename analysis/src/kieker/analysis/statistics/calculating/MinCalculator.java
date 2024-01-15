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

package kieker.analysis.statistics.calculating;

import java.util.Optional;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.statistics.StatisticRecord;

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
public class MinCalculator<T> extends AbstractCalculator<T> {

	private final Function<T, Long> valueAccessor;

	public MinCalculator(final String propertyName, final Function<T, Long> valueAccessor) {
		super(propertyName);
		this.valueAccessor = valueAccessor;
	}

	@Override
	public void calculate(final StatisticRecord statistic, final T input, final EObject modelObject) {
		final long value = this.valueAccessor.apply(input);
		final Optional<Long> oldMin = Optional.ofNullable((Long) statistic.getProperties().get(this.getPropertyName()));
		if (!oldMin.isPresent() || (value < oldMin.get())) {
			statistic.getProperties().put(this.getPropertyName(), value);
		}
	}

}
