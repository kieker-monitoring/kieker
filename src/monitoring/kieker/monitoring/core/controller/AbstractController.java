package kieker.monitoring.core.controller;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractController {
	
	private final AtomicBoolean terminated = new AtomicBoolean(false);

	/**
	 * Permanently terminates this controller.
	 * 
	 * @see #isTerminated()
	 */
	protected final void terminate() {
		if (!terminated.getAndSet(true)) {
			cleanup();
		}
	}

	/**
	 * Returns whether this controller is terminated.
	 * 
	 * @see #terminate()
	 * @return true if terminated
	 */
	protected final boolean isTerminated() {
		return terminated.get();
	}
	
	/**
	 * a String representation of the current state of this controller
	 */
	protected abstract void getState(final StringBuilder sb);
	
	/**
	 * cleans up
	 */
	protected abstract void cleanup();
}
