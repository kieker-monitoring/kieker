/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.util.filesystem;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public interface FSConstants { // NOCS NOPMD (constants interface)

	public static final String FILE_PREFIX = "kieker";
	public static final String MAP_FILENAME = "kieker.map";

	public static final String LEGACY_FILE_PREFIX = "tpmon";
	public static final String LEGACY_MAP_FILENAME = "tpmon.map";

	public static final String NORMAL_FILE_EXTENSION = ".dat";
	public static final String ZIP_FILE_EXTENSION = ".zip";

	public static final String ENCODING = "UTF-8";
}
