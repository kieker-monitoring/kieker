package kieker.panalysis.composite;

import java.util.Map;

import kieker.panalysis.File2TextLinesFilter;
import kieker.panalysis.MethodCallPipe;
import kieker.panalysis.TextLine2RecordFilter;
import kieker.panalysis.base.Filter;

public class ReadRecordFromCsvFileFilter extends Filter<File2TextLinesFilter.INPUT_PORT, TextLine2RecordFilter.OUTPUT_PORT> {

	private final File2TextLinesFilter stage0;
	private final TextLine2RecordFilter stage1;

	public ReadRecordFromCsvFileFilter(final long id) {
		super(id, File2TextLinesFilter.INPUT_PORT.class, TextLine2RecordFilter.OUTPUT_PORT.class);

		// FIXME replace null value
		final Map<Integer, String> stringRegistry = null;

		this.stage1 = new TextLine2RecordFilter(0, stringRegistry);
		this.stage0 = new File2TextLinesFilter(1);

		MethodCallPipe.connect(this.stage0, File2TextLinesFilter.OUTPUT_PORT.TEXT_LINE, this.stage1, TextLine2RecordFilter.INPUT_PORT.TEXT_LINE);
	}

	public void execute() {
		this.stage0.execute();
	}

}
