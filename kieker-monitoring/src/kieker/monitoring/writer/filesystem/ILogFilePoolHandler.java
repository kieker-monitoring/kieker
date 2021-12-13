/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.filesystem;

import java.nio.file.Path;

/**
 * An {@link ILogFilePoolHandler} is used to manage log data files in Kieker.
 * After construction, each request of a new file path will return a new file path.
 *
 * The corresponding constructor must have the following signature:
 * - Path location
 * - String file extension prefixed by a dot
 * - Integer with the max amount of files
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public interface ILogFilePoolHandler {

	/**
	 * Create a new path for a log file.
	 *
	 * @return return a proper stream
	 *
	 * @since 1.14
	 */
	Path requestFile();

}
