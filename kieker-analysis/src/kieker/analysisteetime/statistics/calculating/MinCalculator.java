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

package kieker.analysisteetime.statistics.calculating;

import java.util.Optional;
import java.util.function.Function;

import kieker.analysisteetime.statistics.Properties;
import kieker.analysisteetime.statistics.Property;
import kieker.analysisteetime.statistics.Statistic;

/**
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public class MinCalculator<T> implements Calculator<T> {

	private static final Property MIN_PROPERTY = Properties.MIN;

	private final Function<T, Long> valueAccessor;

	public MinCalculator(final Function<T, Long> valueAccessor) {
		this.valueAccessor = valueAccessor;
	}

	@Override
	public void calculate(final Statistic statistic, final T input, final Object modelObject) {
		final long value = this.valueAccessor.apply(input);
		final Optional<Long> oldMin = Optional.ofNullable(statistic.getProperty(MIN_PROPERTY));
		if (!oldMin.isPresent() || (value < oldMin.get())) {
			statistic.setProperty(MIN_PROPERTY, value);
		}
	}

}
