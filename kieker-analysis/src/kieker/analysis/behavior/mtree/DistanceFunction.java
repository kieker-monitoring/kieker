package kieker.analysis.behavior.mtree;

/**
 * An object that can calculate the distance between two data objects.
 *
 * @param <DATA>
 *            The type of the data objects.
 */
public interface DistanceFunction<DATA> {

	double calculate(DATA data1, DATA data2);

}
