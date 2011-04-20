package kieker.monitoring.timer;

import kieker.monitoring.core.configuration.Configuration;

public class DefaultSystemTimer extends AbstractTimer {
	/**
	 * Offset used to determine the number of nanoseconds since 1970-1-1. This
	 * is necessary since System.nanoTime() returns the elapsed nanoseconds
	 * since *some* fixed but arbitrary time.)
	 */
	private static final long offsetA = System.currentTimeMillis() * 1000000 - System.nanoTime();
		
	public DefaultSystemTimer(final Configuration configuration) {
		super(configuration);
	}
	
	/**
	 * @return the singleton instance of DefaultSystemTimer
	 */
	public final static DefaultSystemTimer getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	@Override
	public long getTime() {
		return System.nanoTime() + offsetA;
	}

	/**
	 * SINGLETON
	 */
	private final static class LazyHolder {
		private static final DefaultSystemTimer INSTANCE = new DefaultSystemTimer(Configuration.createDefaultConfiguration().getPropertiesStartingWith(DefaultSystemTimer.class.getName()));
	}
}
