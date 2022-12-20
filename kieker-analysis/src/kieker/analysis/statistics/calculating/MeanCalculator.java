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

import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.statistics.StatisticRecord;

/**
 * Computes mean from total amount and number of elements.
 *
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class MeanCalculator<T> extends AbstractCalculator<T> {

	private final String totalName;
	private final String countName;

	/**
	 * Create a mean calculator.
	 *
	 * @param propertyName
	 *            property name of the property containing the mean value
	 * @param totalName
	 *            property name of the property containing the total amount
	 * @param countName
	 *            property name of the property containing the number of elements
	 */
	public MeanCalculator(final String propertyName, final String totalName, final String countName) {
		super(propertyName);
		this.totalName = totalName;
		this.countName = countName;
	}

	@Override
	public void calculate(final StatisticRecord statistic, final T input, final EObject modelObject) {
		final Long total = (Long) statistic.getProperties().get(this.totalName);
		final Long count = (Long) statistic.getProperties().get(this.countName);
		if (total != null && count != null) {
			final Long avg = total / count;
			statistic.getProperties().put(this.getPropertyName(), avg);
		}
	}

}
