/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public interface IMapFileHandler {

	/**
	 * Create a map file with the given name and location adhering to the given charset.
	 *
	 * @param location
	 *            complete path for the map file including its name
	 * @param charset
	 *            character set to be used for the file
	 *
	 * @since 1.14
	 */
	void create(Path location, Charset charset);

	/**
	 * Close the mapping file.
	 *
	 * @since 1.14
	 */
	void close();

	/**
	 * Add a key value pair to the file.
	 *
	 * @param id
	 *            class id
	 * @param eventClassName
	 *            name of the event class
	 *
	 * @since 1.14
	 */
	void add(int id, String eventClassName);

}
