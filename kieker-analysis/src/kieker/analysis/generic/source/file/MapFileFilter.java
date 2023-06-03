/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.analysis.generic.source.file;

import java.io.File;
import java.io.FilenameFilter;

import kieker.common.util.filesystem.FSUtil;

/**
 * Map file filter.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class MapFileFilter implements FilenameFilter {

	/**
	 * Create a new file name filter for map files.
	 */
	public MapFileFilter() {
		// empty constructor
	}

	@Override
	public boolean accept(final File dir, final String name) {
		return name.endsWith(FSUtil.MAP_FILE_EXTENSION);
	}

}
