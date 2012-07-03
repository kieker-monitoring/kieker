/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.repository.kdm;

import java.io.IOException;

import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;

/**
 * @author Jan Waller
 */
@Repository(name = "KDM Repository", description = "A repository containing and managing an instance of the KDM.")
public final class KDMRepository extends AbstractRepository {

	public static final String CONFIG_PROPERTY_NAME_MODEL_NAME = "inputFile";
	private static final String CONFIG_PROPERTY_VALUE_DEFAULT_MODEL_NAME = "model.kdm";

	private final String filenameOfModel;

	public KDMRepository(final Configuration configuration) {
		super(configuration);
		this.filenameOfModel = configuration.getStringProperty(KDMRepository.CONFIG_PROPERTY_NAME_MODEL_NAME);

		// TODO: try to load new KDM model from filenameOfModel
	}

	/**
	 * Delivers the current configuration of this instance.
	 * 
	 * @return A configuration object with the current configuration.
	 */
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(KDMRepository.CONFIG_PROPERTY_NAME_MODEL_NAME, this.filenameOfModel);
		return configuration;
	}

	/**
	 * Delivers the default configuration of this class.
	 * 
	 * @return A configuration object with the default configuration.
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(KDMRepository.CONFIG_PROPERTY_NAME_MODEL_NAME, KDMRepository.CONFIG_PROPERTY_VALUE_DEFAULT_MODEL_NAME);
		return configuration;
	}

	/**
	 * This method saves the current state of the KDM model under the configured name.
	 * 
	 * @throws IOException
	 *             If something went wrong during saving.
	 */
	public void saveToFile() throws IOException {
		// TODO: do save
	}
}
