package de.chw.util;

public class Pair<F, S> {

	private final F first;
	private final S second;

	public Pair(final F first, final S second) {
		this.first = first;
		this.second = second;
	}

	public static <F, S> Pair<F, S> of(final F first, final S second) {
		return new Pair<F, S>(first, second);
	}

	public F getFirst() {
		return this.first;
	}

	public S getSecond() {
		return this.second;
	}

}
