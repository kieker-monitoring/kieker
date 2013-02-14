/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Properties;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * This class represents a configuration object within the Kieker project. Technically it is a property list with some additional methods and possibilities.<br/>
 * 
 * Some of the methods are marked as deprecated. This is not because they will be removed, but rather because they should not be used anymore (at least not directly
 * - they are still used in a valid way within this class). Normally we would remove them, but they are inherited from the class {@link Properties} and can neither
 * be removed nor get a lower visibility modificator.
 * 
 * @author Jan Waller
 */
public final class Configuration extends Properties {

	private static final long serialVersionUID = 3364877592243422259L;
	private static final Log LOG = LogFactory.getLog(Configuration.class);

	/**
	 * Creates a new (empty) configuration.
	 */
	public Configuration() {
		this(null);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param defaults
	 *            The property object which delivers the default values for the new configuration.
	 */
	public Configuration(final Properties defaults) {
		super(defaults);
	}

	/**
	 * Reads the given property from the configuration and interprets it as a string.
	 * 
	 * @param key
	 *            The key of the property.
	 * 
	 * @return A string with the value of the given property or null, if the property does not exist.
	 */
	public final String getStringProperty(final String key) {
		final String s = super.getProperty(key);
		return (s == null) ? "" : s.trim(); // NOCS
	}

	/**
	 * Reads the given property from the configuration and interprets it as a boolean.
	 * 
	 * @param key
	 *            The key of the property.
	 * 
	 * @return A boolean with the value of the given property or null, if the property does not exist.
	 */
	public final boolean getBooleanProperty(final String key) {
		return Boolean.parseBoolean(this.getStringProperty(key));
	}

	/**
	 * Reads the given property from the configuration and interprets it as an integer.
	 * 
	 * @param key
	 *            The key of the property.
	 * 
	 * @return An integer with the value of the given property or null, if the property does not exist.
	 */
	public final int getIntProperty(final String key) {
		final String s = this.getStringProperty(key);
		try {
			return Integer.parseInt(s);
		} catch (final NumberFormatException ex) {
			LOG.warn("Error parsing configuration property '" + key + "', found value '" + s + "', using default value 0"); // ignore ex
			return 0;
		}
	}

	/**
	 * Reads the given property from the configuration and interprets it as a long.
	 * 
	 * @param key
	 *            The key of the property.
	 * @return A long with the value of the given property or null, if the property does not exist.
	 */
	public final long getLongProperty(final String key) {
		final String s = this.getStringProperty(key);
		try {
			return Long.parseLong(s);
		} catch (final NumberFormatException ex) {
			LOG.warn("Error parsing configuration property '" + key + "', found value '" + s + "', using default value 0"); // ignore ex
			return 0;
		}
	}

	/**
	 * Reads the given property from the configuration and interprets it as a path.
	 * 
	 * @param key
	 *            The key of the property.
	 * 
	 * @return A string with the value of the given property or null, if the property does not exist.
	 */
	public final String getPathProperty(final String key) {
		return Configuration.convertToPath(this.getStringProperty(key));
	}

	/**
	 * Interprets the property (defined by the given key) as an array of values and transforms it into a real array. Property values have to be split by '|'.
	 * 
	 * @param key
	 *            The key of the property.
	 * 
	 * @return A string array containing the single values of the properties.
	 */
	public final String[] getStringArrayProperty(final String key) {
		return this.getStringArrayProperty(key, "\\|");
	}

	/**
	 * Sets a property to the given string array. Note that the values <b>must not</b> contain the
	 * separator character '|'.
	 * 
	 * @param key
	 *            The key of the property to change
	 * @param value
	 *            The array to set
	 */
	public final void setStringArrayProperty(final String key, final String[] value) {
		this.setProperty(key, Configuration.toProperty(value));
	}

	/**
	 * Interprets the property (defined by the given key) as an array of values and transforms it into a real array. Property values have to be split by 'split'.
	 * 
	 * @param split
	 *            a regular expression
	 * @param key
	 *            The key of the property.
	 * 
	 * @return A string array containing the single values of the properties.
	 * 
	 * @see Configuration#toProperty(Object[])
	 */
	public final String[] getStringArrayProperty(final String key, final String split) {
		final String s = this.getStringProperty(key);
		if (s.length() == 0) {
			return new String[0];
		} else {
			return s.split(split);
		}
	}

	/**
	 * Converts the Object[] to a String split by '|'.
	 * 
	 * @param values
	 *            The values which will be transformed into a string.
	 * 
	 * @return A string representation of the given values array.
	 * 
	 * @see Configuration#getStringArrayProperty(String)
	 * @see Configuration#getStringArrayProperty(String, String)
	 */
	public static final String toProperty(final Object[] values) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			sb.append(values[i]);
			if (i < (values.length - 1)) {
				sb.append('|');
			}
		}
		return sb.toString();
	}

	/**
	 * Tries to simplify a given filesystem path.
	 * E.g., test/../x/y/./z/a/../x -> x/y/z/x
	 * 
	 * @param path
	 *            The path to be simplified.
	 * 
	 * @return A simplified version of the given path.
	 */
	public static final String convertToPath(final String path) {
		try {
			return new URI(null, null, null, -1, path.replace('\\', '/'), null, null).normalize().toASCIIString();
		} catch (final URISyntaxException ex) {
			LOG.warn("Failed to parse path: " + path, ex);
			return path;
		}
	}

	/**
	 * Flattens the Properties hierarchies and returns a Configuration object containing only keys starting with the prefix.
	 * 
	 * @param prefix
	 *            The prefix to be used during the flattening.
	 * 
	 * @return A new configuration object with a flattened properties hierarchy.
	 */
	public final Configuration getPropertiesStartingWith(final String prefix) {
		final Configuration configuration = new Configuration(null);
		// for Java 1.6 simply (also adjust below)
		// final Set<String> keys = this.stringPropertyNames();
		final Enumeration<?> keys = this.propertyNames();
		while (keys.hasMoreElements()) {
			final String property = (String) keys.nextElement();
			if (property.startsWith(prefix)) {
				configuration.setProperty(property, super.getProperty(property));
			}
		}
		return configuration;
	}

	/**
	 * Flattens the Properties hierarchies and returns a new Configuration object.
	 * 
	 * @param defaultConfiguration
	 *            The configuration which will be used as a base.
	 * 
	 * @return A new configuration object with a flattened properties hierarchy.
	 */
	public final Configuration flatten(final Configuration defaultConfiguration) {
		final Configuration configuration = new Configuration(defaultConfiguration);
		// for Java 1.6 simply (also adjust below)
		// final Set<String> keys = this.stringPropertyNames();
		final Enumeration<?> keys = this.propertyNames();
		while (keys.hasMoreElements()) {
			final String property = (String) keys.nextElement();
			configuration.setProperty(property, super.getProperty(property));
		}
		return configuration;
	}

	/**
	 * Flattens the Properties hierarchies and returns a new Configuration object.
	 * 
	 * @return A new configuration object with a flattened properties hierarchy.
	 */
	public final Configuration flatten() {
		return this.flatten(null);
	}

	/**
	 * You should know what you do if you use this method! Currently it is used for a (dirty) hack to implement writers.
	 * 
	 * @param defaultConfiguration
	 *            The default configuration for this configuration object.
	 * 
	 * @throws IllegalAccessException
	 *             If the default value has already been set.
	 */
	public final void setDefaultConfiguration(final Configuration defaultConfiguration) throws IllegalAccessException {
		if (this.defaults == null) {
			this.defaults = defaultConfiguration;
		} else if (defaultConfiguration != null) {
			throw new IllegalAccessException();
		}
	}

	/**
	 * Puts a given key value pair into the container. This method should never be used directly!
	 * Use {@link #setProperty(String, String)} instead!
	 * 
	 * @param key
	 *            The key which will be used to store the given value.
	 * @param value
	 *            The value to store.
	 * 
	 * @return The old object which was stored under the given key or null if there wasn't a value before.
	 * 
	 * @deprecated This method will not be removed (as this is for technical reasons not possible), but should only be used within this class. Don't call this method
	 *             directly.
	 */
	@Override
	@Deprecated
	public final synchronized Object put(final Object key, final Object value) { // NOPMD
		return super.put(key, value);
	}

	/**
	 * Searches for a given key. This method should never be used directly!
	 * Use {@link #getStringProperty(String)} instead!
	 * 
	 * @param key
	 *            The key for the value in question.
	 * 
	 * @return The value for the specified key if available, null otherwise.
	 * 
	 * @deprecated This method will not be removed (as this is for technical reasons not possible), but should only be used within this class. Don't call this method
	 *             directly.
	 */
	@Override
	@Deprecated
	public final synchronized Object get(final Object key) { // NOPMD
		return super.get(key);
	}

	/**
	 * Searches for a given property key. This method should never be used directly!
	 * Use {@link #getStringProperty(String)} instead!
	 * 
	 * @param key
	 *            The key for the property in question.
	 * 
	 * @return The property for the specified key if available, null otherwise.
	 * 
	 * @deprecated This method will not be removed (as this is for technical reasons not possible), but should only be used within this class. Don't call this method
	 *             directly.
	 */
	@Override
	@Deprecated
	public final String getProperty(final String key) {
		return super.getProperty(key);
	}

	/**
	 * Searches for a given property key. This method should never be used directly!
	 * 
	 * @param key
	 *            The key for the property in question.
	 * @param defaultValue
	 *            The default value to be returned.
	 * 
	 * @return The property for the specified key if available, the given default value otherwise.
	 * 
	 * @deprecated This method will not be removed (as this is for technical reasons not possible), but should only be used within this class. Don't call this method
	 *             directly.
	 */
	@Override
	@Deprecated
	public final String getProperty(final String key, final String defaultValue) {
		return super.getProperty(key, defaultValue);
	}
}
