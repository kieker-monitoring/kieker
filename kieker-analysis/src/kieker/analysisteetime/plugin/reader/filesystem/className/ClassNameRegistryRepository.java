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

package kieker.analysisteetime.plugin.reader.filesystem.className;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class represents a wrapper for a Map<String, ClassNameRegistry> ensuring that keys are
 * <ul>
 * <li>of the type <code>java.io.File</code> and
 * <li>passed as absolute file paths.
 * </ul>
 *
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class ClassNameRegistryRepository {

	private final ConcurrentHashMap<String, ClassNameRegistry> repository = new ConcurrentHashMap<>();

	public ClassNameRegistryRepository() {
		// create repository
	}

	public ClassNameRegistry get(final File directory) {
		return this.repository.get(directory.getAbsolutePath());
	}

	public void put(final File directory, final ClassNameRegistry classNameRegistry) {
		this.repository.put(directory.getAbsolutePath(), classNameRegistry);
	}

	public int size() {
		return this.repository.size();
	}

	@Override
	public String toString() {
		return this.repository.toString();
	}
}
