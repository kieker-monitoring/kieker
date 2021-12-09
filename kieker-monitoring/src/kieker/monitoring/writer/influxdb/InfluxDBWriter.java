/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
<<<<<<< HEAD
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
=======
import java.util.List;
>>>>>>> origin/issue-1522-influxdb-writer
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
<<<<<<< HEAD
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
=======

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
>>>>>>> origin/issue-1522-influxdb-writer
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.13
 */
<<<<<<< HEAD
public class InfluxDBWriter extends AbstractMonitoringWriter { // NOPMD is not a data class
=======
public class InfluxDBWriter extends AbstractMonitoringWriter {
>>>>>>> origin/issue-1522-influxdb-writer

	public static final String CONFIG_PROPERTY_DB_URL = "databaseURL";
	public static final String CONFIG_PROPERTY_DB_PORT = "databasePort";
	public static final String CONFIG_PROPERTY_DB_USERNAME = "databaseUsername";
	public static final String CONFIG_PROPERTY_DB_PASSWORD = "databasePassword";
	public static final String CONFIG_PROPERTY_DB_NAME = "databaseName";

<<<<<<< HEAD
	private static final Logger LOGGER = LoggerFactory.getLogger(InfluxDBWriter.class);
=======
	private static final Log LOG = LogFactory.getLog(InfluxDBWriter.class);
>>>>>>> origin/issue-1522-influxdb-writer

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
<<<<<<< HEAD
		LOGGER.info("Connecting to database using the following parameters:");
		LOGGER.info("URL = {}", this.dbURL);
		LOGGER.info("Port = {}", this.dbPort);
		LOGGER.info("Username = {}", this.dbUsername);
		LOGGER.info("Password = {}", this.dbPassword);
=======
		LOG.info("Connecting to database using the following parameters:");
		LOG.info("URL = " + this.dbURL);
		LOG.info("Port = " + this.dbPort);
		LOG.info("Username = " + this.dbUsername);
		LOG.info("Password = " + this.dbPassword);
>>>>>>> origin/issue-1522-influxdb-writer
		this.influxDB = InfluxDBFactory.connect(this.dbURL + ":" + this.dbPort, this.dbUsername, this.dbPassword);
		if (!this.influxDB.isBatchEnabled()) {
			this.influxDB.enableBatch(2000, 500, TimeUnit.MILLISECONDS);
		}

		// Test connection
		final Pong pong;
		try {
			pong = this.influxDB.ping();
<<<<<<< HEAD
			LOGGER.info("Connected to InfluxDB");
=======
			LOG.info("Connected to InfluxDB");
>>>>>>> origin/issue-1522-influxdb-writer
		} catch (final RuntimeException e) { // NOCS NOPMD (thrown by InfluxDB library)
			throw new IOException("Cannot connect to InfluxDB with the following parameters:"
					+ "URL = " + this.dbURL
					+ "; Port = " + this.dbPort
					+ "; Username = " + this.dbUsername
<<<<<<< HEAD
					+ "; Password = " + this.dbPassword, e);
=======
					+ "; Password = " + this.dbPassword
					, e);
>>>>>>> origin/issue-1522-influxdb-writer
		}
		final String influxDBVersion = pong.getVersion();
		final String[] splitVersion = influxDBVersion.split("\\.");
		this.influxDBMajorVersion = Integer.parseInt(splitVersion[0]);
<<<<<<< HEAD
		LOGGER.info("Version: {}", influxDBVersion);
		LOGGER.info("Response time: {}", pong.getResponseTime());
=======
		LOG.info("Version: " + influxDBVersion);
		LOG.info("Response time: " + pong.getResponseTime());
>>>>>>> origin/issue-1522-influxdb-writer

		// Create database if it does not exist
		final List<String> dbList = this.influxDB.describeDatabases();
		if (!dbList.contains(this.dbName)) {
<<<<<<< HEAD
			LOGGER.info("Database {} does not exist. Creating ...", this.dbName);
			this.influxDB.createDatabase(this.dbName);
			LOGGER.info("Done");
=======
			LOG.info("Database " + this.dbName + " does not exist. Creating ...");
			this.influxDB.createDatabase(this.dbName);
			LOG.info("Done");
>>>>>>> origin/issue-1522-influxdb-writer
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
<<<<<<< HEAD
				LOGGER.error("Cannot connect to InfluxDB. Dropping record.", e);
=======
				LOG.error("Cannot connect to InfluxDB. Dropping record.", e);
>>>>>>> origin/issue-1522-influxdb-writer
				return;
			}
		}

		// Extract data
		final String recordName = monitoringRecord.getClass().getSimpleName();
		final long timestamp = monitoringRecord.getLoggingTimestamp();
<<<<<<< HEAD
		final String[] propertyNames = monitoringRecord.getValueNames();
		final Class<?>[] valueTypes = monitoringRecord.getValueTypes();
		final Object[] values = monitoringRecord.getValueNames();

		// This is a temporary measure until the code can be adapted to proper record serialization using IValueSerializer
		final Method[] methods = monitoringRecord.getClass().getMethods();
=======
		// final String[] propertyNames = monitoringRecord.getPropertyNames();
		final String[] propertyNames = { "operationSignature", "sessionId", "traceId", "tin", "tout", "hostname", "eoi", "ess" }; // Mock array
		final Class<?>[] valueTypes = monitoringRecord.getValueTypes();
		final Object[] values = monitoringRecord.toArray();
>>>>>>> origin/issue-1522-influxdb-writer

		// Build data point
		final Point.Builder pointBuilder = Point.measurement(recordName);
		pointBuilder.time(timestamp, TimeUnit.NANOSECONDS);
		for (int i = 0; i < propertyNames.length; i++) {
			final String name = propertyNames[i];
			final Class<?> type = valueTypes[i];
<<<<<<< HEAD
			final Object value = this.invokeMethod(methods, type, name, monitoringRecord);
=======
			final Object value = values[i];
>>>>>>> origin/issue-1522-influxdb-writer
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

<<<<<<< HEAD
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

=======
>>>>>>> origin/issue-1522-influxdb-writer
	@Override
	public void onStarting() {
		try {
			this.connectToInfluxDB();
		} catch (final IOException e) {
<<<<<<< HEAD
			LOGGER.error("Cannot connect to InfluxDB.", e);
=======
			LOG.error("Cannot connect to InfluxDB.", e);
>>>>>>> origin/issue-1522-influxdb-writer
		}
	}

	@Override
	public void onTerminating() {
<<<<<<< HEAD
		LOGGER.info("Closing database");
		this.influxDB.close();
		LOGGER.info("Closing database done");
=======
		LOG.info("Closing database");
		this.influxDB.close();
		LOG.info("Closing database done");
>>>>>>> origin/issue-1522-influxdb-writer
	}

}
