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

import org.w3c.dom.Node;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class FortranOperation extends MMObject implements IDataflowEndpoint, IContainable {

	private static final long serialVersionUID = -3656752458538237388L;

	private final String name;

	private final Map<String, CommonBlock> commonBlocks = new ContainmentHashMap<>(this);

	private final Map<String, FortranVariable> variables = new ContainmentHashMap<>(this);

	private final Map<String, FortranParameter> parameters = new ContainmentHashMap<>(this);

	private final Set<String> usedModules = new HashSet<>();

	private final Node node;

	private FortranModule module;

	private boolean implicit;

	private final boolean variableArguments;

	private final boolean function;

	public FortranOperation(final String name, final Node node, final boolean function) {
		this.name = name;
		this.node = node;
		this.variableArguments = false;
		this.function = function;
	}

	public FortranOperation(final String name, final Node node, final boolean function,
			final boolean variableArguments) {
		this.name = name;
		this.node = node;
		this.variableArguments = variableArguments;
		this.function = function;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public Map<String, CommonBlock> getCommonBlocks() {
		return this.commonBlocks;
	}

	public Map<String, FortranVariable> getVariables() {
		return this.variables;
	}

	public Map<String, FortranParameter> getParameters() {
		return this.parameters;
	}

	public Set<String> getUsedModules() {
		return this.usedModules;
	}

	public Node getNode() {
		return this.node;
	}

	public FortranModule getModule() {
		return this.module;
	}

	public void setModule(final FortranModule module) {
		this.module = module;
	}

	public boolean isImplicit() {
		return this.implicit;
	}

	public void setImplicit(final boolean implicit) {
		this.implicit = implicit;
	}

	public boolean isVariableArguments() {
		return this.variableArguments;
	}

	public boolean isFunction() {
		return this.function;
	}

	@Override
	public String toString() {
		return "op " + this.name;
	}

}
