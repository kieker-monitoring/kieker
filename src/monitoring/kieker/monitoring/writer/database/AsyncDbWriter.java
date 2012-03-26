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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

/**
 * Stores monitoring data into a database.
 * 
 * Warning ! This class is an academic prototype and not intended for
 * reliability or availability critical systems.
 * 
 * The insertMonitoringData methods are thread-save (also in combination with
 * experimentId changes), so that they may be used by multiple threads at the
 * same time. We have tested this in various applications, in combination with
 * the standard mysql-Jconnector database driver.
 * 
 * Our experience shows that it is not a major bottleneck if not too many
 * measurement points are used (e.g., 30/second). However, there are much
 * performance tuning possible in this class. For instance, performance
 * optimization should be possible by using a connection pool instead of a
 * single database connection. The current version uses prepared statements.
 * Alternatively, it could be tuned by collecting multiple database commands
 * before sending it to the database.
 * 
 * @author Matthias Rohr, Jan Waller
 * 
 *         History (Build) (change the String BUILD when this file is changed):
 *         2008/05/29: Changed vmid to vmname (defaults to hostname), which may
 *         be changed during runtime 2007/07/30: Initial Prototype
 */
public final class AsyncDbWriter extends AbstractAsyncWriter {
	private static final String PREFIX = AsyncDbWriter.class.getName() + ".";
	public static final String CONFIG_DRIVERCLASSNAME = AsyncDbWriter.PREFIX + "DriverClassname"; // NOCS (AfterPREFIX)
	public static final String CONFIG_CONNECTIONSTRING = AsyncDbWriter.PREFIX + "ConnectionString"; // NOCS (AfterPREFIX)
	public static final String CONFIG_TABLENAME = AsyncDbWriter.PREFIX + "TableName"; // NOCS (AfterPREFIX)
	public static final String CONFIG_NRCONN = AsyncDbWriter.PREFIX + "numberOfConnections"; // NOCS (AfterPREFIX)

	// private static final Log LOG = LogFactory.getLog(AsyncDbWriter.class);

	// private static final String LOADID = PREFIX + "loadInitialExperimentId";
	// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/189

	public AsyncDbWriter(final Configuration configuration) throws Exception {
		super(configuration);
		this.init();
	}

	@Override
	public void init() throws Exception {
		try {
			// register correct Driver
			Class.forName(this.configuration.getStringProperty(AsyncDbWriter.CONFIG_DRIVERCLASSNAME)).newInstance();
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			throw new Exception("DB driver registration failed. Perhaps the driver jar is missing?", ex);
		}
		final String connectionString = this.configuration.getStringProperty(AsyncDbWriter.CONFIG_CONNECTIONSTRING);
		final String tablename = this.configuration.getStringProperty(AsyncDbWriter.CONFIG_TABLENAME);
		final String preparedQuery = "INSERT INTO " + tablename
				+ " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)" + " VALUES (?,?,?,?,?,?,?,?,?)";
		try {
			// See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/168
			/*
			 * TODO: IS THIS STILL NEEDED?
			 * if (this.configuration.getBooleanProperty(LOADID)) {
			 * final Connection conn = DriverManager.getConnection(connectionString);
			 * final Statement stm = conn.createStatement();
			 * final ResultSet res = stm.executeQuery("SELECT max(experimentid) FROM " + tablename);
			 * if (res.next()) { // this may not be fully constructed!!!! But it should mostly work?!?
			 * this.ctrl.setExperimentId(res.getInt(1) + 1);
			 * }
			 * conn.close();
			 * }
			 */
			for (int i = 0; i < this.configuration.getIntProperty(AsyncDbWriter.CONFIG_NRCONN); i++) {
				this.addWorker(new DbWriterThread(super.monitoringController, this.blockingQueue, connectionString, preparedQuery));
			}
		} catch (final SQLException ex) {
			throw new Exception("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
	}
}

/**
 * @author Matthias Rohr, Jan Waller
 */
final class DbWriterThread extends AbstractAsyncThread {
	private static final Log LOG = LogFactory.getLog(DbWriterThread.class);

	private final Connection conn;
	private final PreparedStatement psInsertMonitoringData;

	public DbWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> blockingQueue, final String connectionString,
			final String preparedQuery) throws SQLException {
		super(monitoringController, blockingQueue);
		this.conn = DriverManager.getConnection(connectionString);
		this.psInsertMonitoringData = this.conn.prepareStatement(preparedQuery);
	}

	@Override
	protected final void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		// connector only supports execution records so far
		final OperationExecutionRecord execRecord = (OperationExecutionRecord) monitoringRecord;
		this.psInsertMonitoringData.setInt(1, execRecord.getExperimentId());
		this.psInsertMonitoringData.setString(2, execRecord.getOperationSignature());
		this.psInsertMonitoringData.setString(3, execRecord.getSessionId());
		this.psInsertMonitoringData.setLong(4, execRecord.getTraceId());
		this.psInsertMonitoringData.setLong(5, execRecord.getTin());
		this.psInsertMonitoringData.setLong(6, execRecord.getTout());
		this.psInsertMonitoringData.setString(7, execRecord.getHostname());
		this.psInsertMonitoringData.setLong(8, execRecord.getEoi());
		this.psInsertMonitoringData.setLong(9, execRecord.getEss());
		this.psInsertMonitoringData.execute();
	}

	@Override
	protected void cleanup() {
		try {
			if (this.conn != null) {
				this.conn.close();
			}
		} catch (final SQLException ex) {
			DbWriterThread.LOG.error("SQLException with SQLState: '" + ex.getSQLState() + "' and VendorError: '" + ex.getErrorCode() + "'", ex);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("; Connection: '");
		sb.append(this.conn.toString());
		sb.append("'");
		return sb.toString();
	}
}
