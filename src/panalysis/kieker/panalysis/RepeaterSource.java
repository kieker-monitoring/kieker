package kieker.panalysis;

import kieker.panalysis.base.AbstractSource;

public class RepeaterSource extends AbstractSource<RepeaterSource.OUTPUT_PORT> {

	private final Object outputRecord;
	private final int num;
	private long overallDuration;

	public static enum OUTPUT_PORT {
		OUTPUT
	}

	public RepeaterSource(final long id, final Object outputRecord, final int num) {
		super(id, OUTPUT_PORT.class);
		this.outputRecord = outputRecord;
		this.num = num;
	}

	public void execute() {
		final long start = System.currentTimeMillis();

		this.executeInternal();

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;
	}

	private void executeInternal() {
		int counter = this.num;
		while (counter-- > 0) {
			this.put(OUTPUT_PORT.OUTPUT, this.outputRecord);
		}
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

}
