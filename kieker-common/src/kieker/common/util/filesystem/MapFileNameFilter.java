package kieker.common.util.filesystem;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class MapFileNameFilter implements FilenameFilter {

	public static final MapFileNameFilter INSTANCE = new MapFileNameFilter();

	private MapFileNameFilter() {
		// singleton
	}

	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(".map");
	}
}
