package kieker.panalysis.concurrent;

import java.util.List;

public class TokenBundle<T> {

	private final List<T> tokens;

	public TokenBundle(final List<T> tokens) {
		this.tokens = tokens;
	}

	public List<T> getTokens() {
		return this.tokens;
	}
}
