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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;

import kieker.analysis.util.RunningMedian;
import kieker.model.analysismodel.statistics.EPropertyType;
import kieker.model.analysismodel.statistics.StatisticRecord;

/**
 *
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class MedianCalculator<T> implements ICalculator<T> {

	private final Map<EObject, RunningMedian<Long>> runningMedians = new HashMap<>(); // NOPMD (class not designed for concurrent access)
	private final Function<T, Long> valueAccessor;

	public MedianCalculator(final Function<T, Long> valueAccessor) {
		this.valueAccessor = valueAccessor;
	}

	@Override
	public void calculate(final StatisticRecord statistic, final T input, final EObject modelObject) {
		final RunningMedian<Long> runningMedian = this.runningMedians.computeIfAbsent(modelObject, o -> RunningMedian.forLong());
		runningMedian.add(this.valueAccessor.apply(input));
		final long newMedian = runningMedian.getMedian();
		statistic.getProperties().put(EPropertyType.MEDIAN, newMedian);
	}

}
