package kieker.monitoring.writer.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.writer.util.async.AbstractAsyncThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * 
 * ==================LICENCE=========================
 * Copyright 2006-2011 Matthias Rohr and the Kieker Project
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
 * @author Matthias Rohr, Jan Waller
 */
public final class DbWriterThread extends AbstractAsyncThread {
	private static final Log log = LogFactory.getLog(DbWriterThread.class);
	
	private final Connection conn;
	private PreparedStatement psInsertMonitoringData;
	boolean statementChanged = true;
	String nextStatementText;

	public DbWriterThread(final Connection initializedConnection, final BlockingQueue<IMonitoringRecord> writeQueue, final String statementtext) {
		super(writeQueue);
		this.conn = initializedConnection;
		this.nextStatementText = statementtext;
		DbWriterThread.log.debug("New DbWriter thread created");
	}

	final synchronized void changeStatement(final String statementtext) {
		this.nextStatementText = statementtext;
		this.statementChanged = true;
	}

	/**
	 * writes next item into database
	 */
	@Override
	protected final void consume(final IMonitoringRecord monitoringRecord) throws SQLException {
		// if (TpmonController.debug)
		// System.out.println("DbWriterThread "+this+" Consuming "+monitoringRecord);
		//TODO: RACE-CONDITION !?!?
		try {
			if (this.statementChanged || (this.psInsertMonitoringData == null)) {
				this.psInsertMonitoringData = this.conn.prepareStatement(this.nextStatementText);
				this.statementChanged = false;
			}
			final OperationExecutionRecord execData = (OperationExecutionRecord) monitoringRecord;
			this.psInsertMonitoringData.setString(1, execData.className + "." + execData.operationName);
			this.psInsertMonitoringData.setString(2, execData.sessionId);
			this.psInsertMonitoringData.setLong(3, execData.traceId);
			this.psInsertMonitoringData.setLong(4, execData.tin);
			this.psInsertMonitoringData.setLong(5, execData.tout);
			this.psInsertMonitoringData.setString(6, execData.hostName);
			this.psInsertMonitoringData.setLong(7, execData.eoi);
			this.psInsertMonitoringData.setLong(8, execData.ess);
			this.psInsertMonitoringData.execute();
		} catch (final SQLException ex) {
			DbWriterThread.log.error("DbWriter Error during database statement preparation: ", ex);
			throw ex;
		}
	}
}
