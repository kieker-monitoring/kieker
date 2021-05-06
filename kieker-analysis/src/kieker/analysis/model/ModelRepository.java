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
package kieker.analysis.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class ModelRepository {

	private final String name;

	private final Map<Class<? extends EObject>, EObject> models = new HashMap<>();

	public ModelRepository(final String name) {
		this.name = name;
	}

	public Map<Class<? extends EObject>, EObject> getModels() {
		return this.models;
	}

	@SuppressWarnings("unchecked")
	public <T extends EObject> T getModel(final Class<T> clazz) {
		return (T) this.models.get(clazz);
	}

	public void register(final Class<? extends EObject> key, final EObject value) {
		this.models.put(key, value);
	}

	public String getName() {
		return this.name;
	}
}
