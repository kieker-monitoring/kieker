/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.clustering;

import kieker.analysis.generic.clustering.mtree.BalancedPartitionFunction;
import kieker.analysis.generic.clustering.mtree.ComposedSplitFunction;
import kieker.analysis.generic.clustering.mtree.IDistanceFunction;
import kieker.analysis.generic.clustering.mtree.IPartitionFunction;
import kieker.analysis.generic.clustering.mtree.IPromotionFunction;
import kieker.analysis.generic.clustering.mtree.ISplitFunction;
import kieker.analysis.generic.clustering.mtree.RandomPromotionFunction;
import kieker.analysis.generic.clustering.optics.OpticsData;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class ClusteringHelper {

	private ClusteringHelper() {}

	public static IDistanceFunction<Integer> integerDistanceFunction() {
		return new IDistanceFunction<Integer>() {

			@Override
			public double calculate(final Integer data1, final Integer data2) {
				return Math.abs((double) data1 - data2);
			}

		};
	}

	public static ISplitFunction<Integer> integerSplitFunction() {
		final IPromotionFunction<Integer> promotionFunction = new RandomPromotionFunction<>();
		final IPartitionFunction<Integer> partitionFunction = new BalancedPartitionFunction<>();
		return new ComposedSplitFunction<>(promotionFunction, partitionFunction);
	}

	public static IDistanceFunction<OpticsData<Integer>> opticsIntegerDistanceFunction() {
		return new IDistanceFunction<OpticsData<Integer>>() {

			@Override
			public double calculate(final OpticsData<Integer> data1, final OpticsData<Integer> data2) {
				return Math.abs((double) data1.getData() - data2.getData());
			}

		};
	}

	public static ISplitFunction<OpticsData<Integer>> opticsIntegerSplitFunction() {
		final IPromotionFunction<OpticsData<Integer>> promotionFunction = new RandomPromotionFunction<>();
		final IPartitionFunction<OpticsData<Integer>> partitionFunction = new BalancedPartitionFunction<>();
		return new ComposedSplitFunction<>(promotionFunction, partitionFunction);
	}

}
