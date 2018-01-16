/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.util;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.writer.dump.DumpWriter;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public final class DefaultConfigurationFactory {

	/** The name of the writer used for the configuration. */
	public static final String WRITER_NAME = DumpWriter.class.getName();

	/**
	 * Private constructor to avoid instantiation.
	 */
	private DefaultConfigurationFactory() {}

	/**
	 * This method creates a simple default configuration containing the dummy writer.
	 *
	 * @return The configuration object.
	 */
	public static Configuration createDefaultConfigurationWithDummyWriter() {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "Kieker-Test");
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DefaultConfigurationFactory.WRITER_NAME);
		// add ignored values
		configuration.setProperty(ConfigurationFactory.PREFIX + "test", "true");
		configuration.setProperty(DefaultConfigurationFactory.WRITER_NAME + ".test", "true");
		return configuration;
	}
}
