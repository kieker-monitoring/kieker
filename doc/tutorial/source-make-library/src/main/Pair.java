package main;

/**
 * This class supplies the possibility to save two (different) objects in one
 * object.
 * 
 * @author Nils Christian Ehmke
 * 
 * @version 1.0 The first implementation of the class.
 */
public class Pair<FST, SND> {

	/**
	 * The field for the first object.
	 */
	public FST first;
	/**
	 * The field for the second object.
	 */
	public SND second;

	/**
	 * Creates a new instance of the class <code>Pair</code> with the given
	 * parameters.
	 * 
	 * @param first
	 *            The first object.
	 * @param second
	 *            The second object.
	 * @since 1.0
	 */
	public Pair(FST first, SND second) {
		this.first = first;
		this.second = second;
	}
}
