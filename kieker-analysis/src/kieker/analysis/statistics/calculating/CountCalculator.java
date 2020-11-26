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

package kieker.analysis.statistics.calculating;

import kieker.analysis.statistics.IProperty;
import kieker.analysis.statistics.Properties;
import kieker.analysis.statistics.Statistic;

/**
 *
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class CountCalculator<T> implements ICalculator<T> {

	private static final IProperty COUNT_PROPERTY = Properties.COUNT;

	public CountCalculator() {
		// Create Calculator
	}

	@Override
	public void calculate(final Statistic statistic, final T input, final Object modelObject) {
		final Long oldCount = statistic.getProperty(COUNT_PROPERTY);
		final long newCount = oldCount != null ? oldCount + 1 : 1; // NOCS (declarative)
		statistic.setProperty(COUNT_PROPERTY, newCount);
	}

}
