package kieker.panalysis.examples.wordcount;

import java.io.File;

import kieker.panalysis.base.AbstractFilter;

public class DirectoryName2Files extends AbstractFilter<DirectoryName2Files.INPUT_PORT, DirectoryName2Files.OUTPUT_PORT> {

	public static enum INPUT_PORT {
		DIRECTORY_NAME
	}

	public static enum OUTPUT_PORT {
		FILE
	}

	private long overallDuration;
	private int numFiles = 0;

	public DirectoryName2Files() {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
	}

	public void execute() {
		final long start = System.currentTimeMillis();

		final String inputDir = (String) this.take(INPUT_PORT.DIRECTORY_NAME);

		final File[] availableFiles = new File(inputDir).listFiles();
		for (final File file : availableFiles) {
			if (file.isFile()) {
				this.put(OUTPUT_PORT.FILE, file);
				this.numFiles++;
			}
		}

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

	public int getNumFiles() {
		return this.numFiles;
	}

}
