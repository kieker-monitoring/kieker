package kieker.panalysis.stage.basic;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <T>
 */
public class Delay<T> extends AbstractFilter<Delay<T>> {

	public final IInputPort<Delay<T>, T> INPUT_OBJECT = this.createInputPort();

	public final IOutputPort<Delay<T>, T> RELAYED_OBJECT = this.createOutputPort();

	private long initialDelayInNs;
	private long intervalDelayInNs;

	private boolean initialDelayExceeded = false;
	private long lastTimeInNs = this.getCurrentTimeInNs();

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<Delay<T>> context) {
		final long passedTimeInNs = this.getCurrentTimeInNs() - this.lastTimeInNs;
		final long thresholdInNs = (this.initialDelayExceeded) ? this.intervalDelayInNs : this.initialDelayInNs;

		if (passedTimeInNs >= thresholdInNs) {
			final T token = context.tryTake(this.INPUT_OBJECT);
			if (token == null) {
				return false;
			}

			this.lastTimeInNs += thresholdInNs;
			context.put(this.RELAYED_OBJECT, token);

			this.initialDelayExceeded = true;
		}

		return true;
	}

	/**
	 * @since 1.10
	 */
	private long getCurrentTimeInNs() {
		return System.nanoTime();
	}

	public long getInitialDelayInNs() {
		return initialDelayInNs;
	}

	public void setInitialDelayInNs(long initialDelayInNs) {
		this.initialDelayInNs = initialDelayInNs;
	}

	public long getIntervalDelayInNs() {
		return intervalDelayInNs;
	}

	public void setIntervalDelayInNs(long intervalDelayInNs) {
		this.intervalDelayInNs = intervalDelayInNs;
	}
}
