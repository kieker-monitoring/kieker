package kieker.panalysis.framework.concurrent;

import java.util.ArrayList;
import java.util.List;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

@Deprecated
public class TokenBundler<T> extends AbstractFilter<TokenBundler<T>> {

	public final IInputPort<TokenBundler<T>, T> INPUT_TOKEN = this.createInputPort();

	public final IOutputPort<TokenBundler<T>, TokenBundle<T>> OUTPUT_BUNDLE = this.createOutputPort();

	private List<T> collectedTokens;

	private int bundleSize;

	public TokenBundler() {
		this.collectedTokens = new ArrayList<T>(this.bundleSize);
	}

	@Override
	protected boolean execute(final Context<TokenBundler<T>> context) {
		final T token = this.tryTake(this.INPUT_TOKEN);
		if (token == null) {
			return false;
		}

		this.collectedTokens.add(token);

		if (this.collectedTokens.size() == this.bundleSize) {
			final TokenBundle<T> tokenBundle = new TokenBundle<T>(this.collectedTokens);
			this.put(this.OUTPUT_BUNDLE, tokenBundle);
			this.collectedTokens = new ArrayList<T>(this.bundleSize);
		}

		return true;
	}

}
