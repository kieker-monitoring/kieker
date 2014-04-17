package kieker.panalysis.stage.basic;

import kieker.panalysis.base.AbstractDefaultFilter;
import kieker.panalysis.base.Context;
import kieker.panalysis.base.IInputPort;
import kieker.panalysis.base.IOutputPort;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <T>
 */
public class Delay<T> extends AbstractDefaultFilter<Delay<T>> {

	public final IInputPort<Delay<T>, T> INPUT_OBJECT = this.createInputPort();

	public final IOutputPort<Delay<T>, T> RELAYED_OBJECT = this.createOutputPort();

	private long initialDelayInMs;
	private long intervalDelayInMs;

	private boolean initialDelayExceeded = false;
	private long lastTimeInMs = this.getCurrentTime();

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<Delay<T>> context) {
		final long passedTimeInMs = this.getCurrentTime() - this.lastTimeInMs;
		final long thresholdInMs = (this.initialDelayExceeded) ? this.intervalDelayInMs : this.initialDelayInMs;

		if (passedTimeInMs >= thresholdInMs) {
			final T token = this.tryTake(this.INPUT_OBJECT);
			if (token == null) {
				return false;
			}

			this.lastTimeInMs += thresholdInMs;
			this.put(this.RELAYED_OBJECT, token);

			this.initialDelayExceeded = true;
		}

		return true;
	}

	/**
	 * @since 1.10
	 */
	private long getCurrentTime() {
		return System.currentTimeMillis();
	}
}
