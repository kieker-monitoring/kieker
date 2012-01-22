package kieker.analysis.plugin;

/**
 * A helper class which can be used if for example a method should return two elements.
 * 
 * @author Nils Christian Ehmke
 * 
 * @param <T1>
 *            The type of the first element.
 * @param <T2>
 *            The type of the second element.
 */
public class Pair<T1, T2> {
	private T1 fst;
	private T2 snd;

	/**
	 * Creates an instance of this class whereas both elements of this container are null.
	 */
	public Pair() {
		this.fst = null;
		this.snd = null;
	}

	/**
	 * Creates an instance of this class using the given parameters.
	 * 
	 * @param fst
	 *            The content for the first element.
	 * @param snd
	 *            The content for the second element.
	 */
	public Pair(final T1 fst, final T2 snd) {
		this.fst = fst;
		this.snd = snd;
	}

	public T1 getFst() {
		return this.fst;
	}

	public T2 getSnd() {
		return this.snd;
	}

	public void setFst(final T1 fst) {
		this.fst = fst;
	}

	public void setSnd(final T2 snd) {
		this.snd = snd;
	}

}
