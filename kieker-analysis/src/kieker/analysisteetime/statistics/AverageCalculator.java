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

public class AverageCalculator<T> implements Calculator<T> {

	private final static Property TOTAL_PROPERTY = PredefinedProperties.TOTAL;
	private final static Property COUNT_PROPERTY = PredefinedProperties.COUNT;
	private final static Property AVERAGE_PROPERTY = PredefinedProperties.AVERAGE;

	public AverageCalculator() {}

	@Override
	public void calculate(final Statistic statistic, final T input, final Object modelObject) {
		final long total = statistic.getProperty(TOTAL_PROPERTY);
		final long count = statistic.getProperty(COUNT_PROPERTY);
		final long avg = total / count;
		statistic.setProperty(AVERAGE_PROPERTY, avg);
	}

}
