package kieker.monitoring.timer;

import java.util.concurrent.TimeUnit;

import kieker.common.configuration.Configuration;

/**
 * A timer implementation that just returns zero for overhead measurement purposes.
 * 
 * @since 2.0.4
 */
public class ZeroTimer extends AbstractTimeSource {

	protected ZeroTimer(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public long getTime() {
		return 0;
	}

	@Override
	public long getOffset() {
		return 0;
	}

	@Override
	public TimeUnit getTimeUnit() {
		return TimeUnit.MILLISECONDS;
	}

	@Override
	public String toString() {
		return "ZeroTimer";
	}
}
