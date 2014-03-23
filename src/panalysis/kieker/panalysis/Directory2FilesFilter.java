package kieker.panalysis;

import java.io.File;
import java.io.FileFilter;

import kieker.common.util.filesystem.BinaryCompressionMethod;
import kieker.common.util.filesystem.FSUtil;
import kieker.panalysis.base.AbstractSource;

public class Directory2FilesFilter extends AbstractSource<Directory2FilesFilter.OUTPUT_PORT> {

	static public enum OUTPUT_PORT {
		FILE
	}

	private final File inputDir;

	public Directory2FilesFilter(final long id, final File inputDir) {
		super(id, OUTPUT_PORT.class);
		this.inputDir = inputDir;
	}

	public void execute() {
		final File inputDir = this.inputDir;

		final File[] inputFiles = inputDir.listFiles(new FileFilter() {
			public boolean accept(final File pathname) {
				final String name = pathname.getName();
				return pathname.isFile()
						&& name.startsWith(FSUtil.FILE_PREFIX)
						&& (name.endsWith(FSUtil.NORMAL_FILE_EXTENSION) || BinaryCompressionMethod.hasValidFileExtension(name));
			}
		});

		for (final File file : inputFiles) {
			this.put(OUTPUT_PORT.FILE, file);
		}
	}

}
