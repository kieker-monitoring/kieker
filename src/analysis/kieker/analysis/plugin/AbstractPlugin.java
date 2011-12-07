/***************************************************************************
 * Copyright 2011 by
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

package kieker.analysis.plugin;

import kieker.analysis.plugin.port.APlugin;
import kieker.analysis.reader.IMonitoringReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * <b>Do not</b> inherit directly from this class! Instead inherit from the
 * class {@link AbstractAnalysisPlugin} or {@link AbstractMonitoringReader}.
 * 
 * @author Nils Christian Ehmke
 */
@APlugin(outputPorts = {})
public abstract class AbstractPlugin {

	private static final Log LOG = LogFactory.getLog(AbstractPlugin.class);

	private String name = null;

	protected final Configuration configuration;

	/**
	 * Each Plugin requires a constructor with a single Configuration object!
	 */
	public AbstractPlugin(final Configuration configuration) {
		try {
			// somewhat dirty hack...
			final Configuration defaultConfig = this.getDefaultConfiguration(); // NOPMD
			if (defaultConfig != null) {
				configuration.setDefaultConfiguration(defaultConfig);
			}
		} catch (final IllegalAccessException ex) {
			AbstractPlugin.LOG.error("Unable to set plugin default properties");
		}
		this.configuration = configuration;
	}

	/**
	 * This method should deliver an instance of {@code Properties} containing
	 * the default properties for this class. In other words: Every class
	 * inheriting from {@code AbstractPlugin} should implement this method to
	 * deliver an object which can be used for the constructor of this clas.
	 * 
	 * @return The default properties.
	 */
	protected abstract Configuration getDefaultConfiguration();

	/**
	 * This method should deliver a {@code Configuration} object containing the
	 * current configuration of this instance. In other words: The constructor
	 * should be able to use the given object to initialize a new instance of
	 * this class with the same intern properties.
	 * 
	 * @return A complete filled configuration object.
	 */
	public abstract Configuration getCurrentConfiguration();

	/**
	 * This method delivers the current name of this plugin. The name does not
	 * have to be unique.
	 * 
	 * @return The name of the plugin.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * This method sets the current name of this plugin. The name does not
	 * have to be unique.
	 * 
	 * @param name
	 *            The new name of the plugin.
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	protected final void deliver(final String outputPort, final Object data) {
		// TODO
		// data muss passen
	}

	public static final void connect(final AbstractPlugin src, final String output, final AbstractPlugin dst, final String input) {
		if (dst instanceof IMonitoringReader) {
			throw new InvalidConnectionException("");
		}

		// TODO
		// dst darf kein reader sein
		// ports müssen passen
		// ports müssen exisieren
	}
}
