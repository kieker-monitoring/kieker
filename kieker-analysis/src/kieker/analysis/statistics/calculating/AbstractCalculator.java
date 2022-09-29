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
 *
 * @param <I>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public abstract class AbstractCalculator<I> {

	private final String propertyName;

	public AbstractCalculator(final String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @param statistic
	 *            the statistic record to process
	 * @param input
	 *            the input to be processed
	 * @param modelObject
	 *            the associated model object
	 *
	 * @since 1.14
	 */
	public abstract void calculate(final StatisticRecord statistic, final I input, final EObject modelObject);

	protected String getPropertyName() {
		return this.propertyName;
	}
}
