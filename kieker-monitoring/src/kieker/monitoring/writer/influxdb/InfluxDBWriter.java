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

package kieker.monitoring.writer.influxdb;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.13
 * @deprecated 2.0.0
 */
// TODO influxDB API has changed
@Deprecated
public class InfluxDBWriter extends AbstractMonitoringWriter { // NOPMD is not a data class

	public static final String CONFIG_PROPERTY_DB_URL = "databaseURL";
	public static final String CONFIG_PROPERTY_DB_PORT = "databasePort";
	public static final String CONFIG_PROPERTY_DB_USERNAME = "databaseUsername";
	public static final String CONFIG_PROPERTY_DB_PASSWORD = "databasePassword";
	public static final String CONFIG_PROPERTY_DB_NAME = "databaseName";

	private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDBWriter.class);

	private final String dbURL;
	private final int dbPort;
	private final String dbUsername;
	private final String dbPassword;
	private final String dbName;
	private volatile InfluxDB influxDB;
	private volatile int influxDBMajorVersion;
	private volatile boolean isConnected;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 */
	public InfluxDBWriter(final Configuration configuration) {
		super(configuration);

		this.dbURL = this.configuration.getStringProperty(CONFIG_PROPERTY_DB_URL);
		this.dbPort = this.configuration.getIntProperty(CONFIG_PROPERTY_DB_PORT);
		this.dbUsername = this.configuration.getStringProperty(CONFIG_PROPERTY_DB_USERNAME);
		this.dbPassword = this.configuration.getStringProperty(CONFIG_PROPERTY_DB_PASSWORD);
		this.dbName = this.configuration.getStringProperty(CONFIG_PROPERTY_DB_NAME);
		this.isConnected = false;
	}

	/**
	 * Connect to InfluxDB.
	 *
	 * @throws IOException
	 *             If connection to InfluxDB fails.
	 **/
	protected final void connectToInfluxDB() throws IOException {
		LOGGER.info("Connecting to database using the following parameters:");
		LOGGER.info("URL = {}", this.dbURL);
		LOGGER.info("Port = {}", this.dbPort);
		LOGGER.info("Username = {}", this.dbUsername);
		LOGGER.info("Password = {}", this.dbPassword);
		this.influxDB = InfluxDBFactory.connect(this.dbURL + ":" + this.dbPort, this.dbUsername, this.dbPassword);
		if (!this.influxDB.isBatchEnabled()) {
			this.influxDB.enableBatch(2000, 500, TimeUnit.MILLISECONDS);
		}

		// Test connection
		final Pong pong;
		try {
			pong = this.influxDB.ping();
			LOGGER.info("Connected to InfluxDB");
		} catch (final RuntimeException e) { // NOCS NOPMD (thrown by InfluxDB library)
			throw new IOException("Cannot connect to InfluxDB with the following parameters:"
					+ "URL = " + this.dbURL
					+ "; Port = " + this.dbPort
					+ "; Username = " + this.dbUsername
					+ "; Password = " + this.dbPassword, e);
		}
		final String influxDBVersion = pong.getVersion();
		final String[] splitVersion = influxDBVersion.split("\\.");

		try {
			this.influxDBMajorVersion = Integer.parseInt(splitVersion[0]);
		} catch (final NumberFormatException ex) {
			this.influxDBMajorVersion = 0;
			LOGGER.error("InfluxDB major version number is not a number, but {}", splitVersion[0]);
		}
		LOGGER.info("Version: {}", influxDBVersion);
		LOGGER.info("Response time: {}", pong.getResponseTime());

		// Create database if it does not exist
		final List<String> dbList = this.influxDB.describeDatabases();
		if (!dbList.contains(this.dbName)) {
			LOGGER.info("Database {} does not exist. Creating ...", this.dbName);
			this.influxDB.createDatabase(this.dbName);
			LOGGER.info("Done");
		}
		this.isConnected = true;
	}

	@Override
	public final void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		// Check connection to InfluxDB
		if (!this.isConnected) {
			try {
				this.connectToInfluxDB();
			} catch (final IOException e) {
				LOGGER.error("Cannot connect to InfluxDB. Dropping record.", e);
				return;
			}
		}

		// Extract data
		final String recordName = monitoringRecord.getClass().getSimpleName();
		final long timestamp = monitoringRecord.getLoggingTimestamp();
		final String[] propertyNames = monitoringRecord.getValueNames();
		final Class<?>[] valueTypes = monitoringRecord.getValueTypes();
		final Object[] values = monitoringRecord.getValueNames();

		// This is a temporary measure until the code can be adapted to proper record serialization using IValueSerializer
		final Method[] methods = monitoringRecord.getClass().getMethods();

		// Build data point
		final Point.Builder pointBuilder = Point.measurement(recordName);
		pointBuilder.time(timestamp, TimeUnit.NANOSECONDS);
		for (int i = 0; i < propertyNames.length; i++) {
			final String name = propertyNames[i];
			final Class<?> type = valueTypes[i];
			final Object value = this.invokeMethod(methods, type, name, monitoringRecord);
			if (type == int.class) {
				pointBuilder.addField(name, (int) value);
			} else if (type == long.class) {
				pointBuilder.addField(name, (long) value);
			} else if (type == boolean.class) {
				pointBuilder.addField(name, (boolean) value);
			} else if (type == String.class) {
				pointBuilder.addField(name, (String) value);
				pointBuilder.tag(name, (String) value);
			}
		}
		final Point point = pointBuilder.build();

		// Write to InfluxDB
		if (this.influxDBMajorVersion < 1) {
			this.influxDB.write(this.dbName, "default", point);
		} else {
			this.influxDB.write(this.dbName, "autogen", point);
		}
	}

	private Object invokeMethod(final Method[] methods, final Class<?> type, final String name, final Object monitoringRecord) {
		final String prefix;
		if (type.equals(Boolean.class)) {
			prefix = "is";
		} else {
			prefix = "get";
		}
		final String methodName = prefix + name.substring(0, 1).toUpperCase(Locale.ROOT) + name.substring(1);
		for (final Method method : methods) {
			if (method.getName().equals(methodName)) {
				try {
					return method.invoke(monitoringRecord);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public void onStarting() {
		try {
			this.connectToInfluxDB();
		} catch (final IOException e) {
			LOGGER.error("Cannot connect to InfluxDB.", e);
		}
	}

	@Override
	public void onTerminating() {
		LOGGER.info("Closing database");
		this.influxDB.close();
		LOGGER.info("Closing database done");
	}

}
