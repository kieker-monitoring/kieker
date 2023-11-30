/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.sar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class Storage {

	private final String name;

	private final List<String> files = new ArrayList<>();

	private final List<String> modules = new ArrayList<>();

	private final List<String> variables = new ArrayList<>();

	public Storage(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public List<String> getFiles() {
		return this.files;
	}

	public List<String> getModules() {
		return this.modules;
	}

	public List<String> getVariables() {
		return this.variables;
	}

}
