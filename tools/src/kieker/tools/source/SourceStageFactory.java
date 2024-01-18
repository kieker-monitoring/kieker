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
package kieker.tools.source;

import kieker.analysis.generic.source.ISourceCompositeStage;
import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.common.util.classpath.InstantiationFactory;
import kieker.tools.common.CommonConfigurationKeys;

/**
 * Factory to create a source stage depending on the configuration object.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public final class SourceStageFactory { // NOPMD is a factory

	private SourceStageFactory() {
		// private constructor for factory
	}

	/**
	 * Create a source stage based on the given configuration.
	 *
	 * @param configuration
	 *            configuration
	 * @return instantiated reader composite stage
	 * @throws ConfigurationException
	 *             on error
	 */
	public static ISourceCompositeStage createSourceCompositeStage(final Configuration configuration)
			throws ConfigurationException {
		final String sourceStageClassName = configuration.getStringProperty(CommonConfigurationKeys.SOURCE_STAGE);

		return InstantiationFactory.createWithConfiguration(ISourceCompositeStage.class, sourceStageClassName,
				configuration);
	}
}
