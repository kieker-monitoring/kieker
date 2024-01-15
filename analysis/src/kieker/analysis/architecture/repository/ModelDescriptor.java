/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;

/**
 * Contains information for a single model in the model repository.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ModelDescriptor {

	private final String filename;
	private final EClass rootClass;
	private final EFactory factory;
	private final boolean required;

	/**
	 * Model descriptor.
	 *
	 * @param filename
	 *            filename for the model
	 * @param rootClass
	 *            root class of the model as EClass instance
	 * @param factory
	 *            factory for the model
	 */
	public ModelDescriptor(final String filename, final EClass rootClass, final EFactory factory) {
		this(filename, rootClass, factory, true);
	}

	/**
	 * Model descriptor.
	 *
	 * @param filename
	 *            filename for the model
	 * @param rootClass
	 *            root class of the model as EClass instance
	 * @param factory
	 *            factory for the model
	 * @param required
	 *            true when the model file must exist
	 */
	public ModelDescriptor(final String filename, final EClass rootClass, final EFactory factory, final boolean required) {
		this.filename = filename;
		this.rootClass = rootClass;
		this.factory = factory;
		this.required = required;
	}

	public String getFilename() {
		return this.filename;
	}

	public EClass getRootClass() {
		return this.rootClass;
	}

	public EFactory getFactory() {
		return this.factory;
	}

	public boolean isRequired() {
		return this.required;
	}
}
