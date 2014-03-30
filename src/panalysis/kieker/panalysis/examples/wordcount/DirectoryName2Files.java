package kieker.panalysis.examples.wordcount;

import java.io.File;

import kieker.panalysis.base.Filter;

public class DirectoryName2Files extends Filter<DirectoryName2Files.INPUT_PORT, DirectoryName2Files.OUTPUT_PORT> {

	public static enum INPUT_PORT {
		DIRECTORY_NAME
	}

	public static enum OUTPUT_PORT {
		FILE
	}

	private long overallDuration;

	public DirectoryName2Files(final long id) {
		super(id, INPUT_PORT.class, OUTPUT_PORT.class);
	}

	public void execute() {
		final long start = System.currentTimeMillis();

		this.executeInternal();

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;
	}

	private void executeInternal() {
		final String inputDir = (String) this.take(INPUT_PORT.DIRECTORY_NAME);

		final File[] availableFiles = new File(inputDir).listFiles();
		for (final File file : availableFiles) {
			this.put(OUTPUT_PORT.FILE, file);
		}
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

}
