package kieker.panalysis.examples.wordcount;

import java.io.File;

import kieker.panalysis.base.AbstractSource;

public class FindFilesSource extends AbstractSource<FindFilesSource.OUTPUT_PORT> {

	public static enum OUTPUT_PORT {
		FILE
	}

	private final String inputDir;

	public FindFilesSource(final long id, final String inputDir) {
		super(id, OUTPUT_PORT.class);
		this.inputDir = inputDir;
	}

	public void execute() {
		final String inputDir = this.inputDir;

		final File[] availableFiles = new File(inputDir).listFiles();
		for (final File file : availableFiles) {
			this.put(OUTPUT_PORT.FILE, file);
		}
	}

}
