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
package kieker.tools.fxca.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;

/**
 * @author Henning Schnoor
 * @since 2.0.0
 */
public class FortranModule extends MMObject implements IContainable {

	private static final long serialVersionUID = 870011289028135834L;

	private final Set<String> usedModules = new HashSet<>();
	private final Map<String, FortranOperation> operations = new ContainmentHashMap<>(this);
	private final String moduleName;
	private final boolean namedModule;
	private final Document document;
	private final String fileName;
	private final Map<String, CommonBlock> commonBlocks = new ContainmentHashMap<>(this);
	private final Map<String, FortranVariable> variables = new ContainmentHashMap<>(this);

	public FortranModule(final String moduleName, final String fileName, final boolean namedModule,
			final Document document) {
		this.moduleName = moduleName;
		this.fileName = fileName;
		this.namedModule = namedModule;
		this.document = document;
	}

	public Set<String> getUsedModules() {
		return this.usedModules;
	}

	public Map<String, FortranOperation> getOperations() {
		return this.operations;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public boolean isNamedModule() {
		return this.namedModule;
	}

	public Document getDocument() {
		return this.document;
	}

	public String getFileName() {
		return this.fileName;
	}

	public Map<String, CommonBlock> getCommonBlocks() {
		return this.commonBlocks;
	}

	public Map<String, FortranVariable> getVariables() {
		return this.variables;
	}

	@Override
	public String toString() {
		return this.fileName + ":" + this.moduleName;
	}

}
