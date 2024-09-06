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

package kieker.analysis.plugin;

import java.util.Arrays;
import java.util.List;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * Extends the {@link AbstractFilterPlugin} with possibilities to update properties that are marked as updateable.
 *
 * @author Thomas Duellmann, Tobias Rudolph, Markus Fischer
 *
 * @since 1.10
 * @deprecated since 1.15.1 old plugin api
 */
@Deprecated
public abstract class AbstractUpdateableFilterPlugin extends AbstractFilterPlugin {

	/**
	 * Each Plugin requires a constructor with a Configuration object and an IProjectContext.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 */
	public AbstractUpdateableFilterPlugin(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	/**
	 * Set current configuration. Example implementation:
	 *
	 * <pre>
	 * // The following condition is true, if key exists in config object AND (update and isUpdateable is true OR update is false)
	 * if (!update || isPropertyUpdateable(CONFIG_PROPERTY_PROP_NAME)) {
	 * 	this.localProperty = config.getLongProperty(CONFIG_PROPERTY_PROP_NAME);
	 * }
	 * </pre>
	 *
	 * @param config
	 *            Configuration object that contains the configuration to be set.
	 * @param update
	 *            If false, set all properties, else overwrite only properties that are marked as updateable
	 */
	public abstract void setCurrentConfiguration(Configuration config, boolean update);

	/**
	 * Checks whether the property with the given name is marked as updateable.
	 *
	 * @param propertyName
	 *            Name of the property to check
	 * @return true if marked as updateable, else false.
	 */
	public boolean isPropertyUpdateable(final String propertyName) {
		final Plugin pluginAnnotations = this.getClass().getAnnotation(Plugin.class);
		final Property[] properties = pluginAnnotations.configuration();
		final List<Property> propertyList = Arrays.asList(properties);

		for (final Property p : propertyList) {
			if (p.name().equals(propertyName)) {
				return p.updateable();
			}
		}
		return false;
	}

}
