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

class ConfigurationFileConstants {
	public final static String WRITER_ASYNCDB = "AsyncDB";
	public final static String WRITER_ASYNCFS = "AsyncFS";

	public final static String WRITER_SYNCDB = "SyncDB";
	public final static String WRITER_SYNCFS = "SyncFS";
	
	public final static String JVM_ARG_PREFIX = "kieker.monitoring.";
}

public enum ConfigurationProperty {
	/* */
	DB_CONNECTION_ADDRESS("dbConnectionAddress",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "dbConnectionAddress",
			"jdbc:mysql://HOSTNAME/DATABASENAME?user=DBUSER&password=DBPASS"),
	/* */
	MONITORING_DATA_WRITER_CLASSNAME("monitoringDataWriter",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "monitoringDataWriter",
			ConfigurationFileConstants.WRITER_ASYNCFS),
	/* */
	MONITORING_DATA_WRITER_INIT_STRING("monitoringDataWriterInitString",
			"monitoringDataWriter" + "monitoringDataWriterInitString", ""),
	/* */
	DEBUG_ENABLED("debug", ConfigurationFileConstants.JVM_ARG_PREFIX + "debug",
			"false"),
	/* */
	MONITORING_ENABLED("monitoringEnabled",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "monitoringEnabled",
			"true"),
	/* */
	ASYNC__RECORD_QUEUE_SIZE("asyncRecordQueueSize",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "asyncRecordQueueSize",
			"8000"),
	/* */
	ASYNC__BLOCK_ON_FULL_QUEUEU(
			"asyncBlockOnFullQueue",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "asyncBlockOnFullQueue",
			"false"),
	/*
	 * For historic reasons ;-), the following two properties have the prefix
	 * also in the configuration file
	 */
	FS_WRITER__STORE_IN_JAVAIOTMPDIR(ConfigurationFileConstants.JVM_ARG_PREFIX
			+ "storeInJavaIoTmpdir", ConfigurationFileConstants.JVM_ARG_PREFIX
			+ "kieker.monitoring.storeInJavaIoTmpdir", "true"), FS_WRITER__CUSTOM_STORAGE_PATH(
			ConfigurationFileConstants.JVM_ARG_PREFIX + "customStoragePath",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "customStoragePath",
			"/tmp"),
	/* Actually, this is not a property but only a default value */
	FS_FN_PREFIX(null, null, ""),
	/* */
	INITIAL_EXPERIMENT_ID("initialExperimentId",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "initialExperimentId",
			"0"),
	/* */
	DB__DRIVER_CLASSNAME("dbDriverClassname",
			ConfigurationFileConstants.JVM_ARG_PREFIX + "dbDriverClassname",
			"com.mysql.jdbc.Driver"),
	/* */
	DB__TABLE_NAME("dbTableName", ConfigurationFileConstants.JVM_ARG_PREFIX
			+ "dbTableName", "kieker"),
	/* */
	DB__SET_INITIAL_EXP_ID_BASED_ON_LAST("setInitialExperimentIdBasedOnLastId",
			ConfigurationFileConstants.JVM_ARG_PREFIX
					+ "setInitialExperimentIdBasedOnLastId", "true");

	private final String propertyName;
	private final String jvmArgName;
	private final String defaultValue;

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
			final String defaultValue) {
		this.propertyName = propertyName;
		this.jvmArgName = jvmArgName;
		this.defaultValue = defaultValue;
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
	 * Returns the string representation of the default value.
	 * 
	 * @return
	 */
	public String defaultValue() {
		return this.defaultValue;
	}
}
