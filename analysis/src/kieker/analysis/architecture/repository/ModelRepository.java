/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class ModelRepository {

	private final String name;

	private final Map<EClass, EObject> models = new ConcurrentHashMap<>();

	private final Map<EClass, ModelDescriptor> descriptors = new ConcurrentHashMap<>();

	/**
	 * Create a new model repository with the given name.
	 *
	 * @param name
	 *            name of the repository
	 */
	public ModelRepository(final String name) {
		this.name = name;
	}

	public Map<EClass, EObject> getModels() {
		return this.models;
	}

	@SuppressWarnings("unchecked")
	public <T extends EObject> T getModel(final EClass eClass) {
		return (T) this.models.get(eClass);
	}

	public <T extends EObject> ModelDescriptor getModelDescriptor(final EClass eClass) {
		return this.descriptors.get(eClass);
	}

	public void register(final ModelDescriptor descriptor, final EObject value) {
		this.models.put(descriptor.getRootClass(), value);
		this.descriptors.put(descriptor.getRootClass(), descriptor);
	}

	public String getName() {
		return this.name;
	}
}
