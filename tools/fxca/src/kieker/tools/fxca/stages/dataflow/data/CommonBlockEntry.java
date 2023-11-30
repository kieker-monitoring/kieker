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
package kieker.tools.fxca.stages.dataflow.data;

import java.util.HashSet;
import java.util.Set;

import kieker.tools.fxca.model.FortranModule;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CommonBlockEntry {

	private final Set<FortranModule> modules = new HashSet<>();

	private final String name;

	private final Set<String> variables = new HashSet<>();

	public CommonBlockEntry(final String name) {
		this.name = name;
	}

	public void merge(final CommonBlockEntry entry) {
		entry.modules.forEach(module -> this.modules.add(module));
		entry.variables.forEach(variable -> this.variables.add(variable));
	}

	public Set<FortranModule> getModules() {
		return this.modules;
	}

	public String getName() {
		return this.name;
	}

	public Set<String> getVariables() {
		return this.variables;
	}
}
