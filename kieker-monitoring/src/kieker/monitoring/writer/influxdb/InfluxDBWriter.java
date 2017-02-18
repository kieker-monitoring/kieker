package kieker.monitoring.writer.influxdb;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * Created by Teerat Pitakrat on 2/11/17.
 */
@Plugin(description = "A filter to write Kieker records to InfluxDB",
		configuration = {
			@Property(name = InfluxDBWriter.CONFIG_PROPERTY_DB_URL, defaultValue = "localhost",
					description = "InfluxDB URL"),
			@Property(name = InfluxDBWriter.CONFIG_PROPERTY_DB_PORT, defaultValue = "8086",
					description = "InfluxDB port"),
			@Property(name = InfluxDBWriter.CONFIG_PROPERTY_DB_USERNAME, defaultValue = "root",
					description = "InfluxDB username (default: root)"),
			@Property(name = InfluxDBWriter.CONFIG_PROPERTY_DB_PASSWORD, defaultValue = "root",
					description = "InfluxDB password (default: root)"),
			@Property(name = InfluxDBWriter.CONFIG_PROPERTY_DB_NAME, defaultValue = "kieker",
					description = "InfluxDB database name")
		})
public class InfluxDBWriter extends AbstractMonitoringWriter {

	private static final Log LOG = LogFactory.getLog(InfluxDBWriter.class);

	public static final String INPUT_PORT_NAME_RECORD = "record";

	public static final String CONFIG_PROPERTY_DB_URL = "databaseURL";
	public static final String CONFIG_PROPERTY_DB_PORT = "databasePort";
	public static final String CONFIG_PROPERTY_DB_USERNAME = "databaseUsername";
	public static final String CONFIG_PROPERTY_DB_PASSWORD = "databasePassword";
	public static final String CONFIG_PROPERTY_DB_NAME = "databaseName";

	private final String dbURL;
	private final int dbPort;
	private final String dbUsername;
	private final String dbPassword;
	private final String dbName;
	private volatile InfluxDB influxDB;
	private volatile String influxDBVersion;
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

	protected final void connectToInfluxDB() {
		LOG.info("Connecting to database using the following parameters:");
		LOG.info("URL = " + this.dbURL);
		LOG.info("Port = " + this.dbPort);
		LOG.info("Username = " + this.dbUsername);
		LOG.info("Password = " + this.dbPassword);
		this.influxDB = InfluxDBFactory.connect(this.dbURL + ":" + this.dbPort, this.dbUsername, this.dbPassword);
		if (this.influxDB.isBatchEnabled() == false) {
			this.influxDB.enableBatch(2000, 500, TimeUnit.MILLISECONDS);
		}

		// Test connection
		final Pong pong;
		try {
			pong = this.influxDB.ping();
			LOG.info("Connected to database");
		} catch (final RuntimeException e) {
			throw new RuntimeException("Cannot connect to InfluxDB with the following parameters:"
					+ "URL = " + this.dbURL
					+ " Port = " + this.dbPort
					+ " Username = " + this.dbUsername
					+ " Password = " + this.dbPassword
					, e);
		}
		this.influxDBVersion = pong.getVersion();
		final String[] splitVersion = this.influxDBVersion.split("\\.");
		this.influxDBMajorVersion = Integer.valueOf(splitVersion[0]);
		LOG.info("Version: " + this.influxDBVersion);
		LOG.info("Response time: " + pong.getResponseTime());

		// Create database if it does not exist
		final List<String> dbList = this.influxDB.describeDatabases();
		if (dbList.contains(this.dbName) == false) {
			LOG.info("Database " + this.dbName + " does not exist. Creating ...");
			this.influxDB.createDatabase(this.dbName);
			LOG.info("Done");
		}
		this.isConnected = true;
	}

	@Override
	@InputPort(name = INPUT_PORT_NAME_RECORD,
			description = "Receives incoming records and writes to InfluxDB",
			eventTypes = { IMonitoringRecord.class }
			)
			public final void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		// Check connection to InfluxDB
		if (this.isConnected == false) {
			try {
				this.connectToInfluxDB();
			} catch (final RuntimeException e) {
				LOG.error("Cannot connect to InfluxDB. Dropping record.");
				LOG.error(e.getMessage());
				return;
			}
		}

		// Extract data
		final String recordName = monitoringRecord.getClass().getSimpleName();
		final long timestamp = monitoringRecord.getLoggingTimestamp();
		// final String[] propertyNames = monitoringRecord.getPropertyNames();
		final String[] propertyNames = { "operationSignature", "sessionId", "traceId", "tin", "tout", "hostname", "eoi", "ess" }; // Mock array
		final Class<?>[] valueTypes = monitoringRecord.getValueTypes();
		final Object[] values = monitoringRecord.toArray();

		// Build data point
		final Point.Builder pointBuilder = Point.measurement(recordName);
		pointBuilder.time(timestamp, TimeUnit.NANOSECONDS);
		for (int i = 0; i < propertyNames.length; i++) {
			final String name = propertyNames[i];
			final Class<?> type = valueTypes[i];
			final Object value = values[i];
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
		try {
			if (this.influxDBMajorVersion < 1)
			{
				this.influxDB.write(this.dbName, "default", point);
			} else {
				this.influxDB.write(this.dbName, "autogen", point);
			}
		} catch (final RuntimeException e) {
			LOG.error("Cannot write to InfluxDB. Dropping record.");
			LOG.error(e.getMessage());
			return;
		}
	}

	@Override
	public void onStarting() {
		try {
			this.connectToInfluxDB();
		} catch (final RuntimeException e) {
			LOG.error("Cannot connect to InfluxDB.");
			LOG.error(e.getMessage());
		}
	}

	@Override
	public void onTerminating() {
		LOG.info("Closing database");
		this.influxDB.close();
		LOG.info("Closing database done");
	}

}
