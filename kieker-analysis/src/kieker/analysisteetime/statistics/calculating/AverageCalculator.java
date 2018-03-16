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

package kieker.analysisteetime.statistics.calculating;

import kieker.analysisteetime.statistics.IProperty;
import kieker.analysisteetime.statistics.Properties;
import kieker.analysisteetime.statistics.Statistic;

/**
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class AverageCalculator<T> implements ICalculator<T> {

	private static final IProperty TOTAL_PROPERTY = Properties.TOTAL;
	private static final IProperty COUNT_PROPERTY = Properties.COUNT;
	private static final IProperty AVERAGE_PROPERTY = Properties.AVERAGE;

	public AverageCalculator() {
		// Create Calculator
	}

	@Override
	public void calculate(final Statistic statistic, final T input, final Object modelObject) {
		final Long total = statistic.getProperty(TOTAL_PROPERTY);
		final Long count = statistic.getProperty(COUNT_PROPERTY);
		if ((total != null) && (count != null)) {
			final long avg = total / count;
			statistic.setProperty(AVERAGE_PROPERTY, avg);
		}
	}

}
