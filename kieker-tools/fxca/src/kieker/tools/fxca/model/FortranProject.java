/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import kieker.tools.fxca.utils.Pair;

/**
 * @author Henning Schnoor
 * @since 1.3.0
 */
public class FortranProject extends MMObject {

	private static final long serialVersionUID = -6785473767290507697L;

	/**
	 * Actual list to which List-calls are delegated.
	 */
	private final Map<String, FortranModule> modules;

	/** note this is a temporary hack until dataflow properly supports a default module. */
	private FortranModule defaultModule;

	private final Collection<Pair<Pair<FortranModule, FortranOperation>, Pair<FortranModule, FortranOperation>>> calls = new ArrayList<>();

	private final Collection<Pair<Pair<FortranModule, IDataflowEndpoint>, Pair<FortranModule, IDataflowEndpoint>>> dataflows = new ArrayList<>();

	/**
	 * Constructs Project Model with empty content.
	 */
	public FortranProject() {
		this.modules = new ContainmentHashMap<>(this);
	}

	public Map<String, FortranModule> getModules() {
		return this.modules;
	}

	public FortranModule getDefaultModule() {
		return this.defaultModule;
	}

	public void setDefaultModule(final FortranModule defaultModule) {
		this.defaultModule = defaultModule;
	}

	public Collection<Pair<Pair<FortranModule, FortranOperation>, Pair<FortranModule, FortranOperation>>> getCalls() {
		return this.calls;
	}

	public Collection<Pair<Pair<FortranModule, IDataflowEndpoint>, Pair<FortranModule, IDataflowEndpoint>>> getDataflows() {
		return this.dataflows;
	}

	@Override
	public String toString() {
		return this.modules.values().stream().map(module -> module.getFileName()).reduce("",
				(r, name) -> r + ", " + name);
	}
}
