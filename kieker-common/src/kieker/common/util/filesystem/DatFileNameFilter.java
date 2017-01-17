package kieker.common.util.filesystem;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class DatFileNameFilter implements FilenameFilter {

	public static final DatFileNameFilter INSTANCE = new DatFileNameFilter();

	private DatFileNameFilter() {
		// singleton
	}

	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(FSUtil.NORMAL_FILE_EXTENSION);
	}
}
