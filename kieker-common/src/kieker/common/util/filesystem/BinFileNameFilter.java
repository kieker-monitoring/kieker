package kieker.common.util.filesystem;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class BinFileNameFilter implements FilenameFilter {

	public static final BinFileNameFilter INSTANCE = new BinFileNameFilter();

	private BinFileNameFilter() {
		// singleton
	}

	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(FSUtil.BINARY_FILE_EXTENSION);
	}
}
