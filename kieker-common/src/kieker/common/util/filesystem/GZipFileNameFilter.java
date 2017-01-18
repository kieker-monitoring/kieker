package kieker.common.util.filesystem;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class GZipFileNameFilter implements FilenameFilter {

	public static final GZipFileNameFilter INSTANCE = new GZipFileNameFilter();

	private GZipFileNameFilter() {
		// singleton
	}

	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(FSUtil.GZIP_FILE_EXTENSION);
	}
}
