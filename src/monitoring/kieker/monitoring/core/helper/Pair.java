package kieker.monitoring.core.helper;

public class Pair<P, I> {
	private P pattern;
	private I active;

	public Pair(final P pattern, final I active) {
		super();
		this.pattern = pattern;
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int hashFirst = this.pattern != null ? this.pattern.hashCode() : 0;
		final int hashSecond = this.active != null ? this.active.hashCode() : 0;

		return ((hashFirst + hashSecond) * hashSecond) + hashFirst;
	}

	@Override
	public boolean equals(final Object other) {
		if (other instanceof Pair) {
			final Pair<?, ?> otherPair = (Pair<?, ?>) other;
			return (((this.pattern == otherPair.pattern) || ((this.pattern != null) && (otherPair.pattern != null) && this.pattern.equals(otherPair.pattern))));
		}

		return false;
	}

	@Override
	public String toString()
	{
		return "(" + this.pattern + ", " + this.active + ")";
	}

	public P getPattern() {
		return this.pattern;
	}

	public void setPattern(final P pattern) {
		this.pattern = pattern;
	}

	public I isActive() {
		return this.active;
	}

	public void setActive(final I active) {
		this.active = active;
	}
}
