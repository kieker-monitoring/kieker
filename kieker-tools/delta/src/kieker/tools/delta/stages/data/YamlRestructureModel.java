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
package kieker.tools.delta.stages.data;

import java.util.HashMap;
import java.util.Map;

/**
 * YAML model for restructuring.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class YamlRestructureModel {

	private final String name;

	private final Map<String, YamlComponent> components = new HashMap<>();

	public YamlRestructureModel(final String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public Map<String, YamlComponent> getComponents() {
		return this.components;
	}

}
