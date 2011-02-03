package kieker.monitoring.writer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.writer.IMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 the Kieker Project
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
 * Warning !
 * This class is an academic prototype and not intended
 * for reliability or availability critical systems.
 * 
 * The insertMonitoringData methods are thread-save (also in combination with
 * experimentId changes),
 * so that they may be used by multiple threads at the same time. We have tested
 * this in various
 * applications, in combination with the standard mysql-Jconnector database
 * driver.
 * 
 * Our experience shows that it is not a major bottleneck if not too many
 * measurement points are used (e.g., 30/second). However, there are much
 * performance tuning possible in this class. For instance, performance
 * optimization should be possible by using a connection pool
 * instead of a single database connection. The current version uses
 * prepared statements. Alternatively, it could be tuned by collecting
 * multiple database commands before sending it to the database.
 * 
 * @author Matthias Rohr, Jan Waller
 * 
 *         History (Build) (change the String BUILD when this file is changed):
 *         2008/05/29: Changed vmid to vmname (defaults to hostname),
 *                     which may be changed during runtime
 *         2007/07/30: Initial Prototype
 */
public final class AsyncDbWriter implements IMonitoringWriter {
	private static final Log log = LogFactory.getLog(AsyncDbWriter.class);

	private final static String defaultConstructionErrorMsg = 
		"Do not select this writer using the full-qualified classname. "
			+ "Use the the constant "
			+ ConfigurationConstants.WRITER_ASYNCDB
			+ " and the file system specific configuration properties.";

	public AsyncDbWriter() {
		throw new UnsupportedOperationException(AsyncDbWriter.defaultConstructionErrorMsg);
	}

	@Override
	public boolean init(final String initString) {
		throw new UnsupportedOperationException(AsyncDbWriter.defaultConstructionErrorMsg);
	}

	private Connection conn = null;
	private BlockingQueue<IMonitoringRecord> blockingQueue;
	private String dbDriverClassname = "com.mysql.jdbc.Driver";
	private String dbConnectionAddress = "jdbc:mysql://jupiter.informatik.uni-oldenburg.de/0610turbomon?user=root&password=xxxxxx";
	private String dbTableName = "turbomon10";
	private boolean setInitialExperimentIdBasedOnLastId = false;
	// only used if setInitialExperimentIdBasedOnLastId==true
	private int experimentId = -1;
	private final int asyncRecordQueueSize; // = 8000;
	private final boolean blockOnFullQueue;

	public AsyncDbWriter(final String dbDriverClassname, final String dbConnectionAddress, final String dbTableName,
			final boolean setInitialExperimentIdBasedOnLastId, final int asyncRecordQueueSize, final boolean blockOnFullQueue) {
		this.dbDriverClassname = dbDriverClassname;
		this.dbConnectionAddress = dbConnectionAddress;
		this.dbTableName = dbTableName;
		this.setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastId;
		this.asyncRecordQueueSize = asyncRecordQueueSize;
		this.blockOnFullQueue = blockOnFullQueue;
		this.init();
	}

	private final Vector<DbWriterThread> workers = new Vector<DbWriterThread>();

	/**
	 * Returns false if an error occurs. Errors are printed to stdout (e.g.,
	 * App-server logfiles), even if debug = false.
	 */
	public boolean init() {
		AsyncDbWriter.log.info("Tpmon asyncDbconnector init");
		try {
			if ((this.dbDriverClassname != null) && (this.dbDriverClassname.length() != 0)) {
				// NOTE: It's absolutely ok to have no class loaded at this point!
				// For example Java 6 and higher have an embedded DB driver
				Class.forName(this.dbDriverClassname).newInstance();
			}
		} catch (final Exception ex) {
			AsyncDbWriter.log.error("DB driver registration failed. Perhaps the driver jar missing? Exception: ", ex);
			return false;
		}
		try {
			this.conn = DriverManager.getConnection(this.dbConnectionAddress);
			final int numberOfConnections = 4;
			this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(this.asyncRecordQueueSize);
			if (this.setInitialExperimentIdBasedOnLastId) {
				// set initial experiment id based on last id (increased by 1)
				//TODO: FindBugs says this method may fail to close the database resource
				final Statement stm = this.conn.createStatement(); 
				final ResultSet res = stm.executeQuery("SELECT max(experimentID) FROM " + this.dbTableName);
				if (res.next()) {
					this.experimentId = res.getInt(1) + 1;
				}
				AsyncDbWriter.log.info(" set initial experiment id based on last id (=" + (this.experimentId - 1) + " + 1 = " + this.experimentId + ")");
			}
			final String preparedQuery = "INSERT INTO " + this.dbTableName
					+ " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)"
					+ "VALUES (" + this.experimentId + ",?,?,?,?,?,?,?,?)";
			for (int i = 0; i < numberOfConnections; i++) {
				DbWriterThread dbw = new DbWriterThread(DriverManager.getConnection(this.dbConnectionAddress), this.blockingQueue, preparedQuery);
				dbw.setDaemon(true); // might lead to inconsistent data due to harsh shutdown
				workers.add(dbw);
				dbw.start();
			}
			AsyncDbWriter.log.info("Tpmon (" + numberOfConnections + " threads) connected to database");
		} catch (final SQLException ex) {
			AsyncDbWriter.log.error("SQLException: " + ex.getMessage());
			AsyncDbWriter.log.error("SQLState: " + ex.getSQLState());
			AsyncDbWriter.log.error("VendorError: " + ex.getErrorCode());
			return false;
		}
		return true;
	}
	
	@Override
	public void terminate() {
		for (DbWriterThread dbw : workers) {
			dbw.initShutdown();
		}
		boolean finished = false;
		for (DbWriterThread dbw : workers) {
			finished = finished && dbw.isFinished(); 
		}
		while (!finished) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				// we should be able to ignore an interrupted sleep.
			}
			AsyncDbWriter.log.info("shutdown delayed - Worker is busy ... waiting additional 0.5 seconds");
			finished = true;
			for (DbWriterThread dbw : workers) {
				finished = finished && dbw.isFinished(); 
			}
		}
		AsyncDbWriter.log.info("Writer: AsyncDbWriter shutdown complete");
	}

	/**
	 * This method is not synchronized, in contrast to the insert method of the
	 * Dbconnector.java.
	 * It uses several dbconnections in parallel using the consumer, producer
	 * pattern.
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			// INSERT INTO `newSchema` ( `experimentid` , `operation` , `traceid` ,
			// `tin` , `tout` ) VALUES ( '0', '1231', '1231', '12312', '1221233' );
			/*
			 * BY ANDRE: I disabled this for the moment since we don't seem to use the
			 * db anyhow
			 * 
			 * if (experimentId != TpmonController.getExperimentId() ||
			 * !vmname.equals(TpmonController.getVmname())) { // ExperimentId and
			 * vmname may be changed
			 * experimentId = TpmonController.getExperimentId();
			 * vmname = TpmonController.getVmname();
			 * String preparedQuery = "INSERT INTO " + TpmonController.dbTableName +
			 * " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)"
			 * +
			 * "VALUES (" + experimentId + ",?,?,?,?,?," + vmname + ",?,?)";
			 * for (DbWriterThread wr : workers) {
			 * wr.changeStatement(preparedQuery);
			 * }
			 * }
			 */
			if (this.blockOnFullQueue) {
				this.blockingQueue.offer(monitoringRecord); // blocks when queue full
			} else {
				this.blockingQueue.add(monitoringRecord); // tries to add immediately!
			}
			// System.out.println("Queue is "+blockingQueue.size());
		} catch (final Exception ex) {
			AsyncDbWriter.log.error("" + System.currentTimeMillis() + " insertMonitoringData() failed: ", ex);
			return false;
		}
		return true;
	}

	@Override
	public String getInfoString() {
		final StringBuilder strB = new StringBuilder();
		// only show the password if debug is on
		String dbConnectionAddress2 = this.dbConnectionAddress;
		if (this.dbConnectionAddress.toLowerCase().contains("password")) {
			final int posPassw = this.dbConnectionAddress.toLowerCase().lastIndexOf("password");
			dbConnectionAddress2 = this.dbConnectionAddress.substring(0, posPassw) + "-PASSWORD-HIDDEN";
		}
		strB.append("dbDriverClassname : ");
		strB.append(this.dbDriverClassname);
		strB.append(", dbConnectionAddress : ");
		strB.append(dbConnectionAddress2);
		strB.append(", dbTableName : ");
		strB.append(this.dbTableName);
		strB.append(", setInitialExperimentIdBasedOnLastId : ");
		strB.append(this.setInitialExperimentIdBasedOnLastId);
		strB.append(", asyncRecordQueueSize : ");
		strB.append(this.asyncRecordQueueSize);
		strB.append(", blockOnFullQueue : ");
		strB.append(this.blockOnFullQueue);
		return strB.toString();
	}
}
