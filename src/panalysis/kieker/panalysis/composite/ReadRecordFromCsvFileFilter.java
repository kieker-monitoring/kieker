package kieker.panalysis.composite;

import java.util.Map;

import kieker.panalysis.File2TextLinesFilter;
import kieker.panalysis.MethodCallPipe;
import kieker.panalysis.TextLine2RecordFilter;
import kieker.panalysis.base.AbstractFilter;

public class ReadRecordFromCsvFileFilter extends AbstractFilter<File2TextLinesFilter.INPUT_PORT, TextLine2RecordFilter.OUTPUT_PORT> {

	private final File2TextLinesFilter stage0;
	private final TextLine2RecordFilter stage1;

	public ReadRecordFromCsvFileFilter() {
		super(File2TextLinesFilter.INPUT_PORT.class, TextLine2RecordFilter.OUTPUT_PORT.class);

		// FIXME replace null value
		final Map<Integer, String> stringRegistry = null;

		this.stage1 = new TextLine2RecordFilter(stringRegistry);
		this.stage0 = new File2TextLinesFilter();

		MethodCallPipe.connect(this.stage0, File2TextLinesFilter.OUTPUT_PORT.TEXT_LINE, this.stage1, TextLine2RecordFilter.INPUT_PORT.TEXT_LINE);
	}

	public void execute() {
		this.stage0.execute();
	}

}
