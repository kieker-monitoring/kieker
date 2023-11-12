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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class FortranVariable extends DataflowObject implements IContainable, IDataflowEndpoint {

	private static final long serialVersionUID = -7967091842094930275L;

	private final Set<IDataflowEndpoint> sources = new HashSet<>();

	public FortranVariable(final String name) {
		super(name);
	}

	public Set<IDataflowEndpoint> getSources() {
		return this.sources;
	}

}
