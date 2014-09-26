/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.tslib;

import org.apache.commons.math3.stat.StatUtils;

/**
 * @author Tom Frotscher
 * @since 1.10
 */
public enum AggregationMethod {

	/**
	 * different aggregationmethods
	 */
	GEOMETRIC_MEAN, MAX, MEAN, MIN, PERCENTILE90, PERCENTILE95, PRODUCT, SUM, SUMLOG, SUMSQ, VARIANCE;

	/**
	 * This method returns the result of the aggregation under one of the defined aggregation methods.
	 *
	 * @param aggregationValues
	 *            Values to be aggregated
	 * @return
	 *         Result of the aggregation
	 */
	public double getAggregationValue(final double[] aggregationValues) {
		switch (this) {
		case GEOMETRIC_MEAN:
			return StatUtils.geometricMean(aggregationValues);
		case MAX:
			return StatUtils.max(aggregationValues);
		case MEAN:
			return StatUtils.mean(aggregationValues);
		case MIN:
			return StatUtils.min(aggregationValues);
		case PERCENTILE90:
			return StatUtils.percentile(aggregationValues, 90);
		case PERCENTILE95:
			return StatUtils.percentile(aggregationValues, 95);
		case PRODUCT:
			return StatUtils.product(aggregationValues);
		case SUM:
			return StatUtils.sum(aggregationValues);
		case SUMSQ:
			return StatUtils.sumSq(aggregationValues);
		case SUMLOG:
			return StatUtils.sumLog(aggregationValues);
		case VARIANCE:
			return StatUtils.variance(aggregationValues);
		default:
			return StatUtils.mean(aggregationValues);
		}
	}
}
