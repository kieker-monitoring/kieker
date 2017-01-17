package kieker.common.util.filesystem;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class ZipFileNameFilter implements FilenameFilter {

	public static final ZipFileNameFilter INSTANCE = new ZipFileNameFilter();

	private ZipFileNameFilter() {
		// singleton
	}

	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(FSUtil.ZIP_FILE_EXTENSION);
	}
}
