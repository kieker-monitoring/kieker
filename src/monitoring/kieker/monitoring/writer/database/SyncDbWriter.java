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

package kieker.monitoring.writer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * Stores monitoring data into a database.
 * 
 * Warning! This class is an academic prototype and not intended for usage in any critical system.
 * 
 * The insertMonitoringData methods should be thread-save (also in combination with experimentId changes), so that they may be used by multiple threads at the same
 * time.
 * 
 * We have tested this in various applications, in combination with the standard MySQL Connector/J database driver.
 * 
 * We experienced that the monitoring it is not a major bottleneck if not too many measurement points are used (e.g., 30/second). However, there is additional
 * performance tuning possible. For instance, by collecting multiple database commands before sending it to the database, or by implementing "statistical profiling",
 * which stores only samples instead of all executions.
 * 
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
public final class SyncDbWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = SyncDbWriter.class.getName() + ".";
	public static final String CONFIG_DRIVERCLASSNAME = SyncDbWriter.PREFIX + "DriverClassname"; // NOCS (AfterPREFIX)
	public static final String CONFIG_CONNECTIONSTRING = SyncDbWriter.PREFIX + "ConnectionString"; // NOCS (AfterPREFIX)
	public static final String CONFIG_TABLEPREFIX = SyncDbWriter.PREFIX + "TablePrefix"; // NOCS (AfterPREFIX)

	private static final Log LOG = LogFactory.getLog(SyncDbWriter.class);

	private final String tablePrefix;
	private final Connection connection;
	private final DBHelper helper;

	public SyncDbWriter(final Configuration configuration) throws Exception {
		super(configuration);
		try {
			Class.forName(this.configuration.getStringProperty(SyncDbWriter.CONFIG_DRIVERCLASSNAME)).newInstance();
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new Exception("DB driver registration failed. Perhaps the driver jar is missing?", ex);
		}
		try {
			this.connection = DriverManager.getConnection(this.configuration.getStringProperty(SyncDbWriter.CONFIG_CONNECTIONSTRING));
			this.tablePrefix = this.configuration.getStringProperty(SyncDbWriter.CONFIG_TABLEPREFIX);
			this.helper = new DBHelper(this.connection, this.tablePrefix);
		} catch (final SQLException ex) {
			throw new Exception("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
	}

	@Override
	public final void init() throws Exception {
		// nothing to do
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		synchronized (this) {
			return this.helper.newMonitoringRecord(monitoringRecord);
		}
	}

	public void terminate() {
		try {
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (final SQLException ex) {
			SyncDbWriter.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
		SyncDbWriter.LOG.info("Writer: SyncDbWriter shutdown complete");
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n\tConnection: '");
		sb.append(this.connection.toString());
		sb.append("'");
		return sb.toString();
	}
}
