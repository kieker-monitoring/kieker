package kieker.panalysis.examples.wordcount;

import java.io.File;

import kieker.panalysis.base.Source;

public class FindFilesSource extends Source<FindFilesSource.PORT> {

	public static enum PORT { FILE }

	private final String inputDir;

	public FindFilesSource(final long id, final String inputDir) {
		super(id, PORT.class);
		this.inputDir = inputDir;
	}

	@Override
	public void execute() {
		File[] availableFiles = new File(inputDir).listFiles();
		for (File file : availableFiles) {
			deliver(PORT.FILE, file);
		}
	}

}
