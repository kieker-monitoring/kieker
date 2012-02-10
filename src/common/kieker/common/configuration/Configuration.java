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

package kieker.common.configuration;

import java.util.Properties;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * A Configuration
 * 
 * @author Jan Waller
 */
public final class Configuration extends Properties {
	private static final long serialVersionUID = 3364877592243422259L;
	private static final Log LOG = LogFactory.getLog(Configuration.class);

	public Configuration() {
		this(null);
	}

	public Configuration(final Properties defaults) {
		super(defaults);
	}

	public final String getStringProperty(final String key) {
		final String s = this.getProperty(key);
		return (s == null) ? "" : s; // NOCS
	}

	public final boolean getBooleanProperty(final String key) {
		return Boolean.parseBoolean(this.getStringProperty(key));
	}

	public final int getIntProperty(final String key) {
		final String s = this.getStringProperty(key);
		try {
			return Integer.parseInt(s);
		} catch (final NumberFormatException ex) {
			Configuration.LOG.warn("Error parsing configuration property '" + key + "', found value '" + s + "', using default value 0");
			return 0;
		}
	}

	public final long getLongProperty(final String key) {
		final String s = this.getStringProperty(key);
		try {
			return Long.parseLong(s);
		} catch (final NumberFormatException ex) {
			Configuration.LOG.warn("Error parsing configuration property '" + key + "', found value '" + s + "', using default value 0");
			return 0;
		}
	}

	/**
	 * Property values have to be split by '|'.
	 * 
	 * @param key
	 * @return
	 */
	public final String[] getStringArrayProperty(final String key) {
		final String s = this.getStringProperty(key);
		if (s.isEmpty()) {
			return new String[0];
		} else {
			return s.split("\\|");
		}
	}

	/**
	 * Converts the String[] to a String split by '|'.
	 * 
	 * @param values
	 * @return
	 */
	public static final String toProperty(final String[] values) {
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
	 * Converts the Long[] to a String split by '|'.
	 * 
	 * @param values
	 * @return
	 */
	public static final String toProperty(final Long[] values) {
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
	 * Flattens the Properties hierarchies and returns an Configuration object containing only keys starting with the prefix.
	 * 
	 * <p>
	 * Any implementation should probably be this: (where Configuration is the concrete class)
	 * <p>
	 * <blockquote>
	 * 
	 * <pre>
	 * public final Configuration getPropertiesStartingWith(final String prefix) {
	 * 	return (Configuration) getPropertiesStartingWith(new Configuration(null), prefix);
	 * }
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * </p>
	 * 
	 * @param prefix
	 * @return
	 */
	public final Configuration getPropertiesStartingWith(final String prefix) {
		final Configuration configuration = new Configuration(null);
		for (final String property : this.stringPropertyNames()) {
			if (property.startsWith(prefix)) {
				configuration.setProperty(property, this.getProperty(property));
			}
		}
		return configuration;
	}

	/**
	 * You should know what you do if you use this method!
	 * Currently it is used for a (dirty) hack to implement writers.
	 * 
	 * @param defaultConfiguration
	 * @throws IllegalAccessException
	 */
	public final void setDefaultConfiguration(final Configuration defaultConfiguration) throws IllegalAccessException {
		if (this.defaults == null) {
			this.defaults = defaultConfiguration;
		} else if (defaultConfiguration != null) {
			throw new IllegalAccessException();
		}
	}

	/**
	 * This method should never be used directly!
	 * Use {@link #setProperty(String, String)} instead!
	 */
	@Override
	@Deprecated
	public final synchronized Object put(final Object key, final Object value) {
		return super.put(key, value);
	}
}
