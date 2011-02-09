package kieker.monitoring.writer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.IWriterController;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * Stores monitoring data into a database.
 * 
 * Warning! This class is an academic prototype and not intended
 * for usage in any critical system.
 * 
 * The insertMonitoringData methods should be thread-save
 * (also in combination with experimentId changes), so that they
 * may be used by multiple threads at the same time.
 * 
 * We have tested this in various applications, in combination with
 * the standard MySQL Connector/J database driver.
 * 
 * We experienced that the monitoring it is not a major bottleneck
 * if not too many measurement points are used (e.g., 30/second).
 * However, there is additional performance tuning possible. For instance,
 * by collecting multiple database commands before sending
 * it to the database, or by implementing "statistical profiling", which
 * stores only samples instead of all executions.
 * 
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 * 
 *         History:
 *         2008/05/29: Changed vmid to vmname (defaults to hostname), which may
 *         be changed during runtime
 *         2008/01/04: Refactoring for the first release of
 *         Kieker and publication under an open source licence
 *         2007/03/13: Refactoring
 *         2006/12/20: Initial Prototype
 * 
 */
public final class SyncDbWriter extends AbstractMonitoringWriter {
	private static final Log log = LogFactory.getLog(SyncDbWriter.class);
	
	private static final String PREFIX = SyncDbWriter.class.getName() + ".";
	private static final String DRIVERCLASSNAME = PREFIX + "DriverClassname";
	private static final String CONNECTIONSTRING = PREFIX + "ConnectionString";
	private static final String TABLENAME = PREFIX + "TableName";
	//private static final String LOADID = PREFIX + "loadInitialExperimentId";

	private final Connection conn;
	private final PreparedStatement psInsertMonitoringData;
	
	public SyncDbWriter(final IWriterController ctrl, final Configuration configuration) throws Exception {
		super(ctrl, configuration);
		try {
			// register correct Driver
			Class.forName(this.configuration.getStringProperty(DRIVERCLASSNAME)).newInstance();
		} catch (final Exception ex) {
			SyncDbWriter.log.error("DB driver registration failed. Perhaps the driver jar is missing?");
			throw ex;
		}
		try {
			this.conn = DriverManager.getConnection(this.configuration.getStringProperty(CONNECTIONSTRING));
			final String tablename = this.configuration.getStringProperty(TABLENAME);
			/* IS THIS STILL NEEDED?
			if (this.configuration.getBooleanProperty(LOADID)) {
				final Statement stm = this.conn.createStatement();
				final ResultSet res = stm.executeQuery("SELECT max(experimentid) FROM " + tablename);
				if (res.next()) {
					//TODO: this may not be fully constructed!!!! But it should mostly work?!?
					this.ctrl.setExperimentId(res.getInt(1) + 1);
				}
			} */
			this.psInsertMonitoringData = this.conn.prepareStatement(
					"INSERT INTO " + tablename + 
					" (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)" + 
					" VALUES (?,?,?,?,?,?,?,?,?)");
		} catch (final SQLException ex) {
			SyncDbWriter.log.error("SQLException: " + ex.getMessage());
			SyncDbWriter.log.error("SQLState: " + ex.getSQLState());
			SyncDbWriter.log.error("VendorError: " + ex.getErrorCode());
			throw ex;
		}
	}
	
	@Override
	public final synchronized boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			// connector only supports execution records so far
			final OperationExecutionRecord execRecord = (OperationExecutionRecord) monitoringRecord;
			this.psInsertMonitoringData.setInt(1, execRecord.experimentId);
			this.psInsertMonitoringData.setString(2, execRecord.className + "." + execRecord.operationName);
			this.psInsertMonitoringData.setString(3, execRecord.sessionId);
			this.psInsertMonitoringData.setLong(4, execRecord.traceId);
			this.psInsertMonitoringData.setLong(5, execRecord.tin);
			this.psInsertMonitoringData.setLong(6, execRecord.tout);
			this.psInsertMonitoringData.setString(7, execRecord.hostName);
			this.psInsertMonitoringData.setLong(8, execRecord.eoi);
			this.psInsertMonitoringData.setLong(9, execRecord.ess);
			this.psInsertMonitoringData.execute();
		} catch (final Exception ex) {
			SyncDbWriter.log.error("Failed to write new monitoring record:", ex);
			return false;
		} finally {
			try {
				this.psInsertMonitoringData.clearParameters();
			} catch (final Exception ex) {
				SyncDbWriter.log.error(ex);
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void terminate() {
		try {
			this.conn.close();
		} catch (final SQLException ex) {
			SyncDbWriter.log.error("SQLException: " + ex.getMessage());
			SyncDbWriter.log.error("SQLState: " + ex.getSQLState());
			SyncDbWriter.log.error("VendorError: " + ex.getErrorCode());
		}
		SyncDbWriter.log.info("Writer: SyncDbWriter shutdown complete");
	}

	@Override
	public String getInfoString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getInfoString());
		sb.append("\n\tConnection: '");
		sb.append(conn.toString());
		sb.append("'");
		return sb.toString();
	}
}
