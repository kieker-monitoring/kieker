package kieker.monitoring.core.configuration;

import java.util.Properties;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */

public enum ConfigurationProperty {
	/* */
	DB_CONNECTION_ADDRESS("dbConnectionAddress",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "dbConnectionAddress",
			"jdbc:mysql://HOSTNAME/DATABASENAME?user=DBUSER&password=DBPASS",
			/* must not be empty: */false),
	/* */
	MONITORING_DATA_WRITER_CLASSNAME("monitoringDataWriter",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "monitoringDataWriter",
			ConfigurationFileConstants.WRITER_ASYNCFS,
			/* must not be empty: */false),
	/* */
	MONITORING_DATA_WRITER_INIT_STRING("monitoringDataWriterInitString",
			"monitoringDataWriter" + "monitoringDataWriterInitString", "",
			/* can be empty: */true),
	/* */
	DEBUG_ENABLED("debug", ConfigurationFileConstants.JVM_ARG_PREFIX + "debug",
			"false",
			/* must not be empty: */false),
	/* */
	MONITORING_ENABLED("monitoringEnabled",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "monitoringEnabled",
			"true",
			/* must not be empty: */false),
	/* */
	ASYNC__RECORD_QUEUE_SIZE("asyncRecordQueueSize",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "asyncRecordQueueSize",
			"8000",
			/* must not be empty: */false),
	/* */
	ASYNC__BLOCK_ON_FULL_QUEUE(
			"asyncBlockOnFullQueue",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "asyncBlockOnFullQueue",
			"false",
			/* must not be empty: */false),
	/*
	 * For historic reasons ;-) this property has the prefix also in the
	 * configuration file:
	 */
	FS_WRITER__STORE_IN_JAVAIOTMPDIR(ConfigurationFileConstants.JVM_ARG_PREFIX
			+ "storeInJavaIoTmpdir", ConfigurationFileConstants.JVM_ARG_PREFIX
			+ "kieker.monitoring.storeInJavaIoTmpdir", "true",
	/* must not be empty: */false),
	/*
	 * For historic reasons ;-) this property has the prefix also in the
	 * configuration file:
	 */
	FS_WRITER__CUSTOM_STORAGE_PATH(ConfigurationFileConstants.JVM_ARG_PREFIX
			+ "customStoragePath", ConfigurationFileConstants.JVM_ARG_PREFIX
			+ "customStoragePath", "/tmp",
	/* must not be empty: */false),
	/* Actually, this is not a property but only a default value */
	FS_FN_PREFIX(null, null, "", /* can be empty: */true),
	/* */
	INITIAL_EXPERIMENT_ID("initialExperimentId",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "initialExperimentId",
			"0", /* can be empty: */false),
	/* */
	DB__DRIVER_CLASSNAME("dbDriverClassname",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "dbDriverClassname",
			"com.mysql.jdbc.Driver",
			/* must not be empty: */false),
	/* */
	DB__TABLE_NAME("dbTableName", ConfigurationFileConstants.JVM_ARG_PREFIX
			+ "dbTableName", "kieker",
	/* must not be empty: */false),
	/* */
	DB__SET_INITIAL_EXP_ID_BASED_ON_LAST("setInitialExperimentIdBasedOnLastId",
			ConfigurationFileConstants.JVM_ARG_PREFIX
					+ "setInitialExperimentIdBasedOnLastId", "true",
			/* must not be empty: */false);

	private final String propertyName;
	private final String jvmArgName;
	private final String defaultValue;
	private final boolean allowEmpty;

	/**
	 * Constructs an enum property.
	 * 
	 * @param propertyName
	 *            the property name used in the configuration file
	 * @param jvmArgName
	 *            set null if the property value cannot be specified via a JVM
	 *            argument
	 * @param defaultValue
	 *            the String representation of the default value or null if none
	 */
	ConfigurationProperty(final String propertyName, final String jvmArgName,
			final String defaultValue, final boolean allowEmpty) {
		this.propertyName = propertyName;
		this.jvmArgName = jvmArgName;
		this.defaultValue = defaultValue;
		this.allowEmpty = allowEmpty;
	}

	/**
	 * Returns a properties map with the default configuration.
	 * 
	 * @return
	 */
	public static Properties defaultProperties() {
		final Properties props = new Properties();

		for (final ConfigurationProperty prop : ConfigurationProperty.values()) {
			if (prop.propertyName == null) {
				continue;
			}
			props.setProperty(prop.propertyName, prop.defaultValue);
		}

		return props;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	/**
	 * Returns the name of the system property or null if none.
	 * 
	 * @return
	 */
	public String getJVMArgumentName() {
		return this.jvmArgName;
	}

	/**
	 * Returns whether this property can be configured via JVM argument.
	 * 
	 * @return
	 */
	public boolean hasJVMArgument() {
		return this.jvmArgName != null;
	}

	/**
	 * Returns whether the empty String is a valid property value.
	 * 
	 * @return
	 */
	public boolean isAllowEmpty() {
		return this.allowEmpty;
	}

	/**
	 * Returns the string representation of the default value.
	 * 
	 * @return
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}
}
