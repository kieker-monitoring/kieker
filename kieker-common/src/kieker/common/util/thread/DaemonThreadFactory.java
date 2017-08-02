package kieker.common.util.thread;

import java.util.concurrent.ThreadFactory;

/**
 * A thread factory that creates daemon threads. The default thread parameters are based on
 * the default thread factory.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class DaemonThreadFactory implements ThreadFactory {

	/**
	 * Creates a new daemon thread factory. 
	 */
	public DaemonThreadFactory() {
		// Do nothing
	}

	@Override
	public Thread newThread(final Runnable runnable) {
		final Thread thread = new Thread(runnable);

		thread.setDaemon(true);
		thread.setPriority(Thread.NORM_PRIORITY);

		return thread;
	}
	
}
