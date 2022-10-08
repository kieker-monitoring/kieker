package kieker.analysis.behavior.mtree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Some pre-defined implementations of {@linkplain DistanceFunction distance
 * functions}.
 */
public final class DistanceFunctions {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private DistanceFunctions() {}

	/**
	 * Creates a cached version of a {@linkplain DistanceFunction distance
	 * function}. This method is used internally by {@link MTree} to create
	 * a cached distance function to pass to the {@linkplain SplitFunction split
	 * function}.
	 *
	 * @param distanceFunction
	 *            The distance function to create a cached version
	 *            of.
	 * @return The cached distance function.
	 */
	public static <Data> DistanceFunction<Data> cached(final DistanceFunction<Data> distanceFunction) {
		return new DistanceFunction<Data>() {
			class Pair {
				Data data1;
				Data data2;

				public Pair(final Data data1, final Data data2) {
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
			public double calculate(final Data data1, final Data data2) {
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
	 * An interface to represent coordinates in Euclidean spaces.
	 *
	 * @see <a href="http://en.wikipedia.org/wiki/Euclidean_space">"Euclidean
	 *      Space" article at Wikipedia</a>
	 */
	public interface EuclideanCoordinate {
		/**
		 * The number of dimensions.
		 */
		int dimensions();

		/**
		 * A method to access the {@code index}-th component of the coordinate.
		 *
		 * @param index
		 *            The index of the component. Must be less than {@link
		 *            #dimensions()}.
		 */
		double get(int index);
	}

	/**
	 * Calculates the distance between two {@linkplain EuclideanCoordinate
	 * euclidean coordinates}.
	 */
	public static double euclidean(final EuclideanCoordinate coord1, final EuclideanCoordinate coord2) {
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
	 * A {@linkplain DistanceFunction distance function} object that calculates
	 * the distance between two {@linkplain EuclideanCoordinate euclidean
	 * coordinates}.
	 */
	public static final DistanceFunction<EuclideanCoordinate> EUCLIDEAN = new DistanceFunction<DistanceFunctions.EuclideanCoordinate>() {
		@Override
		public double calculate(final EuclideanCoordinate coord1, final EuclideanCoordinate coord2) {
			return DistanceFunctions.euclidean(coord1, coord2);
		}
	};

	/**
	 * A {@linkplain DistanceFunction distance function} object that calculates
	 * the distance between two coordinates represented by {@linkplain
	 * java.util.List lists} of {@link java.lang.Integer}s.
	 */
	public static final DistanceFunction<List<Integer>> EUCLIDEAN_INTEGER_LIST = new DistanceFunction<List<Integer>>() {
		@Override
		public double calculate(final List<Integer> data1, final List<Integer> data2) {
			class IntegerListEuclideanCoordinate implements EuclideanCoordinate {
				List<Integer> list;

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
			;
			final IntegerListEuclideanCoordinate coord1 = new IntegerListEuclideanCoordinate(data1);
			final IntegerListEuclideanCoordinate coord2 = new IntegerListEuclideanCoordinate(data2);
			return DistanceFunctions.euclidean(coord1, coord2);
		}
	};

	/**
	 * A {@linkplain DistanceFunction distance function} object that calculates
	 * the distance between two coordinates represented by {@linkplain
	 * java.util.List lists} of {@link java.lang.Double}s.
	 */
	public static final DistanceFunction<List<Double>> EUCLIDEAN_DOUBLE_LIST = new DistanceFunction<List<Double>>() {
		@Override
		public double calculate(final List<Double> data1, final List<Double> data2) {
			class DoubleListEuclideanCoordinate implements EuclideanCoordinate {
				List<Double> list;

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
			;
			final DoubleListEuclideanCoordinate coord1 = new DoubleListEuclideanCoordinate(data1);
			final DoubleListEuclideanCoordinate coord2 = new DoubleListEuclideanCoordinate(data2);
			return DistanceFunctions.euclidean(coord1, coord2);
		}
	};
}
