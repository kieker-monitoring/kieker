/***************************************************************************
 * Copyright (c) 2012-2013 Eduardo R. D'Avila (https://github.com/erdavila)
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
package kieker.analysis.generic.graph.mtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Some pre-defined implementations of {@linkplain IDistanceFunction distance
 * functions}.
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public final class DistanceFunctions {

	/**
	 * A {@linkplain IDistanceFunction distance function} object that calculates
	 * the distance between two {@linkplain IEuclideanCoordinate euclidean
	 * coordinates}.
	 */
	public static final IDistanceFunction<IEuclideanCoordinate> EUCLIDEAN = new IDistanceFunction<DistanceFunctions.IEuclideanCoordinate>() {
		@Override
		public double calculate(final IEuclideanCoordinate coord1, final IEuclideanCoordinate coord2) {
			return DistanceFunctions.euclidean(coord1, coord2);
		}
	};

	/**
	 * A {@linkplain IDistanceFunction distance function} object that calculates
	 * the distance between two coordinates represented by {@linkplain
	 * java.util.List lists} of {@link java.lang.Integer}s.
	 */
	public static final IDistanceFunction<List<Integer>> EUCLIDEAN_INTEGER_LIST = new IDistanceFunction<List<Integer>>() {
		@Override
		public double calculate(final List<Integer> data1, final List<Integer> data2) {
			class IntegerListEuclideanCoordinate implements IEuclideanCoordinate {
				private final List<Integer> list;

				public IntegerListEuclideanCoordinate(final List<Integer> list) {
					this.list = list;
				}

				@Override
				public int dimensions() {
					return this.list.size();
				}

				@Override
				public double get(final int index) {
					return this.list.get(index);
				}
			}

			final IntegerListEuclideanCoordinate coord1 = new IntegerListEuclideanCoordinate(data1);
			final IntegerListEuclideanCoordinate coord2 = new IntegerListEuclideanCoordinate(data2);
			return DistanceFunctions.euclidean(coord1, coord2);
		}
	};

	/**
	 * A {@linkplain IDistanceFunction distance function} object that calculates
	 * the distance between two coordinates represented by {@linkplain
	 * java.util.List lists} of {@link java.lang.Double}s.
	 */
	public static final IDistanceFunction<List<Double>> EUCLIDEAN_DOUBLE_LIST = new IDistanceFunction<List<Double>>() {
		@Override
		public double calculate(final List<Double> data1, final List<Double> data2) {
			class DoubleListEuclideanCoordinate implements IEuclideanCoordinate {
				private final List<Double> list;

				public DoubleListEuclideanCoordinate(final List<Double> list) {
					this.list = list;
				}

				@Override
				public int dimensions() {
					return this.list.size();
				}

				@Override
				public double get(final int index) {
					return this.list.get(index);
				}
			}

			final DoubleListEuclideanCoordinate coord1 = new DoubleListEuclideanCoordinate(data1);
			final DoubleListEuclideanCoordinate coord2 = new DoubleListEuclideanCoordinate(data2);
			return DistanceFunctions.euclidean(coord1, coord2);
		}
	};

	/**
	 * Don't let anyone instantiate this class.
	 */
	private DistanceFunctions() {}

	/**
	 * Creates a cached version of a {@linkplain IDistanceFunction distance
	 * function}. This method is used internally by {@link MTree} to create
	 * a cached distance function to pass to the {@linkplain ISplitFunction split
	 * function}.
	 *
	 * @param distanceFunction
	 *            The distance function to create a cached version
	 *            of.
	 * @param <D>
	 *            distance function type
	 * @return The cached distance function.
	 */
	public static <D> IDistanceFunction<D> cached(final IDistanceFunction<D> distanceFunction) {
		return new IDistanceFunction<D>() {
			class Pair {
				private final D data1;
				private final D data2;

				public Pair(final D data1, final D data2) {
					this.data1 = data1;
					this.data2 = data2;
				}

				@Override
				public int hashCode() {
					return this.data1.hashCode() ^ this.data2.hashCode();
				}

				@Override
				public boolean equals(final Object arg0) {
					if (arg0 instanceof Pair) {
						final Pair that = (Pair) arg0;
						return this.data1.equals(that.data1)
								&& this.data2.equals(that.data2);
					} else {
						return false;
					}
				}
			}

			private final Map<Pair, Double> cache = new HashMap<>();

			@Override
			public double calculate(final D data1, final D data2) {
				final Pair pair1 = new Pair(data1, data2);
				Double distance = this.cache.get(pair1);
				if (distance != null) {
					return distance;
				}

				final Pair pair2 = new Pair(data2, data1);
				distance = this.cache.get(pair2);
				if (distance != null) {
					return distance;
				}

				distance = distanceFunction.calculate(data1, data2);
				this.cache.put(pair1, distance);
				this.cache.put(pair2, distance);
				return distance;
			}
		};
	}

	/**
	 * Calculates the distance between two {@linkplain IEuclideanCoordinate
	 * euclidean coordinates}.
	 *
	 * @param coord1
	 *            first coordinate
	 * @param coord2
	 *            second coordinate
	 * @return returns the distance value
	 */
	public static double euclidean(final IEuclideanCoordinate coord1, final IEuclideanCoordinate coord2) {
		final int size = Math.min(coord1.dimensions(), coord2.dimensions());
		double distance = 0;
		for (int i = 0; i < size; i++) {
			final double diff = coord1.get(i) - coord2.get(i);
			distance += diff * diff;
		}
		distance = Math.sqrt(distance);
		return distance;
	}

	/**
	 * An interface to represent coordinates in Euclidean spaces.
	 *
	 * @see <a href="http://en.wikipedia.org/wiki/Euclidean_space">"Euclidean
	 *      Space" article at Wikipedia</a>
	 */
	public interface IEuclideanCoordinate {
		/**
		 * The number of dimensions.
		 *
		 * @return returns the number of dimensions.
		 */
		int dimensions();

		/**
		 * A method to access the {@code index}-th component of the coordinate.
		 *
		 * @param index
		 *            The index of the component. Must be less than {@link
		 *            #dimensions()}.
		 * @return returns value with the given index
		 */
		double get(int index);
	}

}
