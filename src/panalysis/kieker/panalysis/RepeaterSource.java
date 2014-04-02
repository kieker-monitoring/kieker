package kieker.panalysis;

import kieker.panalysis.base.AbstractSource;
import kieker.panalysis.base.TaskBundle;

public class RepeaterSource extends AbstractSource<RepeaterSource.OUTPUT_PORT> {

	private final Object outputRecord;
	private final int num;
	private long overallDuration;

	public static enum OUTPUT_PORT {
		OUTPUT
	}

	public RepeaterSource(final Object outputRecord, final int num) {
		super(OUTPUT_PORT.class);
		this.outputRecord = outputRecord;
		this.num = num;
	}

	public void execute() {
		final long start = System.currentTimeMillis();

		int counter = this.num;
		while (counter-- > 0) {
			this.put(OUTPUT_PORT.OUTPUT, this.outputRecord);
		}

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

	public void execute(final TaskBundle taskBundle) {
		// TODO Auto-generated method stub

	}

}
