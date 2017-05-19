package kieker.analysisteetime.util.timeseries;

import java.time.Instant;

public abstract class AbstractTimeSeriesPoint implements TimeSeriesPoint {

	private final Instant time;

	public AbstractTimeSeriesPoint(final Instant time) {
		this.time = time;
	}

	@Override
	public Instant getTime() {
		return this.time;
	}

	@Override
	public final String toString() {
		return "[" + this.time + "=" + this.valueToString() + "]";
	}

	public abstract String valueToString();
}
