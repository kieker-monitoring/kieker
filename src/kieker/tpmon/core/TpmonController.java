package kieker.tpmon.core;

import kieker.common.monitoringRecord.AbstractKiekerMonitoringRecord;

import kieker.tpmon.writer.util.async.TpmonShutdownHook;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;
import kieker.tpmon.writer.IKiekerMonitoringLogWriter;
import kieker.tpmon.writer.databaseSync.SyncDbConnector;
import kieker.tpmon.writer.filesystemSync.SyncFsWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.common.monitoringRecord.KiekerDummyMonitoringRecord;
import kieker.tpmon.writer.databaseAsync.AsyncDbConnector;
import kieker.tpmon.writer.filesystemAsync.AsyncFsConnector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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

/**
 * Simple class to store monitoroing data in the file system. Although a
 * buffered writer is used, outliers (delays of 1000 ms) occur from time
 * to time if many monitoring events have to be writen. We believe that
 * outliers result from a flush on the buffer of the writer. 
 * 
 * A more sophisticated writer to store data in the file system is 
 * the AsyncFsWriter. This does not introduce the outliers that result
 * from flushing the writing buffer, since provides an asynchronous 
 * insertMonitoringData method. However, the AsyncFsWriter introduces
 * a little more overhead because a writing queue is required and it isn't
 * tested as much as the FileSystenWriter.
 * 
 * The AsyncFsWriter should usually be used instead of this class to avoid 
 * the outliers described above.
 * 
 * @author Matthias Rohr, Andre van Hoorn
 * 
 * History:
 * 2008/09/01: Removed a lot "synchronized" from the Aspects
 * 2008/08/29: Controller now singleton class
 *             Many (performance) improvements to synchronization
 * 2008/08/06: Using tpmon.properties instead of dbconnector.properties and support
 *             of using java.io.tmpdir as file system storage directory. The storage
 *             directory may be set via the properties file, or (higher priority)
 *             via a java command line parameter 
 *             (-Dtpmon.storeInJavaIoTmpdir=false -Dtpmon.customStoragePath=/var/log/)
 * 2008/07/07: New feature to encode method and component names
 *             before making data persistent. This speeds up storage
 *             and saves space.
 * 2008/05/29: Changed vmid to vmname (defaults to hostname), 
 *             which may be changed during runtime
 * 2008/01/04: Refactoring for the first release of 
 *             Kieker and publication under an open source licence
 * 2007/03/13: Refactoring
 * 2006/12/20: Initial Prototype
 */
public final class TpmonController {

    private static final Log log = LogFactory.getLog(TpmonController.class);
    public final static String WRITER_SYNCDB = "SyncDB";
    public final static String WRITER_ASYNCDB = "AsyncDB";
    public final static String WRITER_SYNCFS = "SyncFS";
    public final static String WRITER_ASYNCFS = "AsyncFS";
    private String monitoringDataWriterClassname = null;
    private String monitoringDataWriterInitString = null;
    private IKiekerMonitoringLogWriter monitoringDataWriter = null;
    private String vmname = "unknown";    // the following configuration values are overwritten by tpmonLTW.properties in tpmonLTW.jar
    private String dbDriverClassname = "com.mysql.jdbc.Driver";
    private String dbConnectionAddress = "jdbc:mysql://HOSTNAME/DATABASENAME?user=DBUSER&password=DBPASS";
    private String dbTableName = "turbomon10";
    private boolean debug = false;
    private String filenamePrefix = ""; // e.g. path "/tmp/"
    private boolean storeInJavaIoTmpdir = true;
    private String customStoragePath = "/tmp"; // only used as default if storeInJavaIoTmpdir == false
    private boolean logMonitoringRecordTypeIds = false; // eventually, true should become default
    private int asyncRecordQueueSize = 8000;
    // database only configuration configuration values that are overwritten by tpmon.properties included in the tpmon library
    private boolean setInitialExperimentIdBasedOnLastId = false;    // only use the asyncDbconnector in server environments, that do not directly terminate after the executions, or some 
    private TpmonShutdownHook shutdownhook = null;
    private static TpmonController ctrlInst = new TpmonController();

    //marks the end of monitoring to the writer threads
    public static final AbstractKiekerMonitoringRecord END_OF_MONITORING_MARKER = new KiekerDummyMonitoringRecord();

    @TpmonInternal()
    public final static TpmonController getInstance() {
        return TpmonController.ctrlInst;
    }

    private TpmonController() {
        try {
            vmname = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
        } // nothing to do -- vmname will be "unknown"

        log.info(">Kieker-Tpmon: The VM has the name " + vmname + " Thread:" +
                Thread.currentThread().getId());
        log.info(">Kieker-Tpmon: Virtual Machine start time " +
                ManagementFactory.getRuntimeMXBean().getStartTime());

        shutdownhook = new TpmonShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownhook);

        loadPropertiesFile();

        /* We will now determine and load the monitoring Writer to use */
        try {
            if (this.monitoringDataWriterClassname == null || this.monitoringDataWriterClassname.length() == 0) {
                throw new Exception("Property monitoringDataWriter not set");
            } else if (this.monitoringDataWriterClassname.equals(WRITER_SYNCFS)) {
                String filenameBase = filenamePrefix;
                this.monitoringDataWriter = new SyncFsWriter(filenameBase);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_ASYNCFS)) {
                String filenameBase = filenamePrefix;
                this.monitoringDataWriter = new AsyncFsConnector(filenameBase, asyncRecordQueueSize);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_SYNCDB)) {
                this.monitoringDataWriter = new SyncDbConnector(
                        dbDriverClassname, dbConnectionAddress,
                        dbTableName,
                        setInitialExperimentIdBasedOnLastId);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_ASYNCDB)) {
                this.monitoringDataWriter = new AsyncDbConnector(
                        dbDriverClassname, dbConnectionAddress,
                        dbTableName,
                        setInitialExperimentIdBasedOnLastId, asyncRecordQueueSize);
            } else {
                /* try to load the class by name */
                this.monitoringDataWriter = (IKiekerMonitoringLogWriter) Class.forName(this.monitoringDataWriterClassname).newInstance();
                //add asyncRecordQueueSize
                monitoringDataWriterInitString += " | asyncRecordQueueSize="+asyncRecordQueueSize;
                if (!this.monitoringDataWriter.init(monitoringDataWriterInitString)) {
                    this.monitoringDataWriter = null;
                    throw new Exception("Initialization of writer failed!");
                }

            }
            this.monitoringDataWriter.setWriteRecordTypeIds(this.logMonitoringRecordTypeIds);
            Vector<AbstractWorkerThread> worker = this.monitoringDataWriter.getWorkers(); // may be null
            if (worker != null) {
                for (AbstractWorkerThread w : worker) {
                    this.registerWorker(w);
                }
            }
            // TODO: we should add a getter to all writers like isInitialized.
            //       right now, the following even appears in case init failed.
            //       Or can we simply throw an exception from within the constructors
            log.info(">Kieker-Tpmon: Initialization completed.\n Connector Info: " + this.getConnectorInfo());
        } catch (Exception exc) {
            log.error(">Kieker-Tpmon: Disabling monitoring", exc);
            this.terminateMonitoring();
        }
    }

    /**
     * The vmname which defaults to the hostname, and may be set by tpmon-control-servlet.
     * The vmname will be part of the monitoring data and allows to assing observations
     * in cases where the software system is deployed on more than one host.
     * 
     * When you want to distinguish multiple Virtual Machines on one host,
     * you have to set the vmname manually (e.g., via the tpmon-control-servlet, 
     * or by directly implementing a call to TpmonController.setVmname(...).
     */
    @TpmonInternal()
    public final String getVmname() {
        return this.vmname;
    }

    /**
     * Allows to set an own vmname, a field in the monitoring data to distinguish
     * multiple hosts / vms in a system. This method is for instance used by
     * the tpmon control servlet. 
     * 
     * The vmname defaults to the hostname.
     * 
     * When you want to distinguish multiple Virtual Machines on one host,
     * you have to set the vmname manually (e.g., via the tpmon-control-servlet, 
     * or by directly implementing a call to TpmonController.setVmname(...).
     * 
     * @param newVmname
     */
    @TpmonInternal()
    public final void setVmname(String newVmname) {
        log.info(">Kieker-Tpmon: The VM has the NEW name " + newVmname +
                " Thread:" + Thread.currentThread().getId());
        this.vmname = newVmname;
    }

    /**
     * See TpmonShutdownHook.registerWorker
     * @param newWorker
     */
    @TpmonInternal()
    private void registerWorker(AbstractWorkerThread newWorker) {
        this.shutdownhook.registerWorker(newWorker);
    }
    private AtomicLong numberOfInserts = new AtomicLong(0);
    // private Date startDate = new Date(initializationTime);
    // TODO: should be volatile? -> more overhead, but correct!
    private boolean monitoringEnabled = true;
    // if monitoring terminated, it is not allowed to enable monitoring afterwards
    private boolean monitoringPermanentlyTerminated = false;

    @TpmonInternal()
    public final boolean isDebug() {
        return debug;
    }

    /**
     * Shows how many inserts have been performed since last restart of the execution
     * environment.
     */
    @TpmonInternal()
    public long getNumberOfInserts() {
        return numberOfInserts.longValue();
    }

    @TpmonInternal()
    public final boolean isMonitoringEnabled() {
        return monitoringEnabled;
    }

    @TpmonInternal()
    public final boolean isMonitoringPermanentlyTerminated() {
        return monitoringPermanentlyTerminated;
    }
    private static final int STANDARDEXPERIMENTID = 0;
    // we do not use AtomicInteger since we only rarely 
    // set the value (common case -- getting -- faster now).
    // instead, we decided to provide an "expensive" increment method.
    private int experimentId = STANDARDEXPERIMENTID;

    @TpmonInternal()
    public final int getExperimentId() {
        return this.experimentId;
    }

    @TpmonInternal()
    public synchronized int incExperimentId() {
        return this.experimentId++;
    }

    @TpmonInternal()
    public void setExperimentId(int newExperimentID) {
        this.experimentId = newExperimentID;
    }

    /**
     * Enables monitoring.
     */
    @TpmonInternal()
    public final void enableMonitoring() {
        log.info("Enabling monitoring");
        if (this.monitoringPermanentlyTerminated) {
            log.error("Refused to enable monitoring because monitoring has been permanently terminated before");
        } else {
            this.monitoringEnabled = true;
        }
    }

    /**
     * Disables to store monitoring data.
     * Monitoring may be enabled again by calling enableMonitoring().
     */
    @TpmonInternal()
    public final void disableMonitoring() {
        log.info("Disabling monitoring");
        this.monitoringEnabled = false;
    }

    /**
     * Permanently terminates monitoring (e.g., due to a failure).
     * Subsequent tries to enable monitoring will be refused.
     */
    @TpmonInternal()
    public final synchronized void terminateMonitoring() {
        log.info("Permanently terminating monitoring");
        if (this.monitoringDataWriter != null) {
            /* if the initialization of the writer failed, it is set to null*/
            this.monitoringDataWriter.writeMonitoringRecord(END_OF_MONITORING_MARKER);
        }
        this.disableMonitoring();
        this.monitoringPermanentlyTerminated = true;
    }
    /**
     * If true, the loggingTimestamp is not set by the logMonitoringRecord
     * method. This is required to replay recorded traces with the
     * original timestamps.
     */
    private boolean replayMode = false;

    public final void setReplayMode(boolean replayMode) {
        this.replayMode = replayMode;
    }

    @TpmonInternal()
    public final boolean logMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) {
        if (!this.monitoringEnabled) {
            return false;
        }

        numberOfInserts.incrementAndGet();
        // now it fails fast, it disables monitoring when a queue once is full
        if (!this.replayMode) {
            monitoringRecord.setLoggingTimestamp(this.getTime());
        }
        if (!this.monitoringDataWriter.writeMonitoringRecord(monitoringRecord)) {
            log.fatal("Error writing the monitoring data. Will terminate monitoring!");
            this.terminateMonitoring();
            return false;
        }

        return true;
    }
    private static final long offsetA = System.currentTimeMillis() * 1000000 - System.nanoTime();

    /**
     * This method can used by the probes to get the time stamps. It uses nano seconds as precision.
     *
     * In contrast to System.nanoTime(), it gives the nano seconds between the current time and midnight, January 1, 1970 UTC.
     * (The value returned by System.nanoTime() only represents nanoseconds since *some* fixed but arbitrary time.)
     */
    @TpmonInternal()
    public final long getTime() {
        return System.nanoTime() + offsetA;
    }

    /**    
     * Loads configuration values from the file
     * tpmonLTW.jar/META-INF/dbconnector.properties or another
     * tpmon configuration file specified by the JVM parameter
     * tpmon.configuration.
     *
     * If it fails, it uses hard-coded standard values.    
     */
    @TpmonInternal()
    private void loadPropertiesFile() {
        String configurationFile = "META-INF/tpmon.properties";
        InputStream is = null;
        Properties prop = new Properties();

        try {
            if (System.getProperty("tpmon.configuration") != null) { // we use the present virtual machine parameter value
                log.info("Tpmon: Loading properties JVM-specified path '" + configurationFile + "'");
                configurationFile = System.getProperty("tpmon.configuration");
                is = new FileInputStream(configurationFile);
            } else {
                log.info("Tpmon: Loading properties from tpmon library jar/" + configurationFile);
                log.info("You can specify an alternative properties file using the property 'tpmon.configuration'");
                is = TpmonController.class.getClassLoader().getResourceAsStream(configurationFile);
            }
            // TODO: the fall-back file in the tpmon library should be renamed to
            //       META-INF/tpmon.properties.default or alike, in order to
            //       avoid strange behavior caused by the order of jars being
            //       being loaded by the classloader.
            prop.load(is);
        } catch (Exception ex) {
            log.error("Error loading tpmon.properties file '" + configurationFile + "'", ex);
            // TODO: introduce static variable 'terminated' or alike
        } finally {
            try {
                is.close();
            } catch (Exception ex) { /* nothing we can do */ }
        }

        // load property monitoringDataWriter
        monitoringDataWriterClassname = prop.getProperty("monitoringDataWriter");
        monitoringDataWriterInitString = prop.getProperty("monitoringDataWriterInitString");

        String dbDriverClassnameProperty;
        if (System.getProperty("tpmon.dbConnectionAddress") != null) { // we use the present virtual machine parameter value
            dbDriverClassnameProperty = System.getProperty("tpmon.dbDriverClassname");
        } else { // we use the parameter in the properties file
            dbDriverClassnameProperty = prop.getProperty("dbDriverClassname");
        }
        if (dbDriverClassnameProperty != null && dbDriverClassnameProperty.length() != 0) {
            dbDriverClassname = dbDriverClassnameProperty;
        } else {
            log.info("No dbDriverClassname parameter found in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + dbDriverClassname + ".");
        }

        // load property "dbConnectionAddress"
        String dbConnectionAddressProperty;
        if (System.getProperty("tpmon.dbConnectionAddress") != null) { // we use the present virtual machine parameter value
            dbConnectionAddressProperty = System.getProperty("tpmon.dbConnectionAddress");
        } else { // we use the parameter in the properties file
            dbConnectionAddressProperty = prop.getProperty("dbConnectionAddress");
        }
        if (dbConnectionAddressProperty != null && dbConnectionAddressProperty.length() != 0) {
            dbConnectionAddress = dbConnectionAddressProperty;
        } else {
            log.warn("No dbConnectionAddress parameter found in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + dbConnectionAddress + ".");
        }

// the filenamePrefix (folder where tpmon stores its data) 
// for monitoring data depends on the properties tpmon.storeInJavaIoTmpdir 
// and tpmon.customStoragePath         
// these both parameters may be provided (with higher priority) as java command line parameters as well (example in the properties file)
        String storeInJavaIoTmpdirProperty;
        if (System.getProperty("tpmon.storeInJavaIoTmpdir") != null) { // we use the present virtual machine parameter value
            storeInJavaIoTmpdirProperty = System.getProperty("tpmon.storeInJavaIoTmpdir");
        } else { // we use the parameter in the properties file
            storeInJavaIoTmpdirProperty = prop.getProperty("tpmon.storeInJavaIoTmpdir");
        }

        if (storeInJavaIoTmpdirProperty != null && storeInJavaIoTmpdirProperty.length() != 0) {
            if (storeInJavaIoTmpdirProperty.toLowerCase().equals("true") || storeInJavaIoTmpdirProperty.toLowerCase().equals("false")) {
                storeInJavaIoTmpdir = storeInJavaIoTmpdirProperty.toLowerCase().equals("true");
            } else {
                log.warn("Bad value for tpmon.storeInJavaIoTmpdir (or provided via command line) parameter (" + storeInJavaIoTmpdirProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + storeInJavaIoTmpdir);
            }
        } else {
            log.warn("No tpmon.storeInJavaIoTmpdir parameter found in tpmonLTW.jar/" + configurationFile +
                    " (or provided via command line). Using default value '" + storeInJavaIoTmpdir + "'.");
        }

        if (storeInJavaIoTmpdir) {
            filenamePrefix = System.getProperty("java.io.tmpdir");
        } else { // only now we consider tpmon.customStoragePath
            String customStoragePathProperty;
            if (System.getProperty("tpmon.customStoragePath") != null) { // we use the present virtual machine parameter value
                customStoragePathProperty = System.getProperty("tpmon.customStoragePath");
            } else { // we use the parameter in the properties file
                customStoragePathProperty = prop.getProperty("tpmon.customStoragePath");
            }

            if (customStoragePathProperty != null && customStoragePathProperty.length() != 0) {
                filenamePrefix = customStoragePathProperty;
            } else {
                log.warn("No tpmon.customStoragePath parameter found in tpmonLTW.jar/" + configurationFile +
                        " (or provided via command line). Using default value '" + customStoragePath + "'.");
                filenamePrefix =
                        customStoragePath;
            }
        }

        // load property "dbTableNameProperty"
        String dbTableNameProperty;
        if (System.getProperty("tpmon.dbTableName") != null) { // we use the present virtual machine parameter value
            dbTableNameProperty = System.getProperty("tpmon.dbTableName");
        } else { // we use the parameter in the properties file
            dbTableNameProperty = prop.getProperty("dbTableName");
        }
        if (dbTableNameProperty != null && dbTableNameProperty.length() != 0) {
            dbTableName = dbTableNameProperty;
        } else {
            log.warn("No dbTableName  parameter found in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + dbTableName + ".");
        }

        // load property "debug"
        String debugProperty = prop.getProperty("debug");
        if (debugProperty != null && debugProperty.length() != 0) {
            if (debugProperty.toLowerCase().equals("true") || debugProperty.toLowerCase().equals("false")) {
                debug = debugProperty.toLowerCase().equals("true");
            } else {
                log.warn("Bad value for debug parameter (" + debugProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + debug);
            }
        } else {
            log.warn("Could not find debug parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + debug);
        }

        // load property "setInitialExperimentIdBasedOnLastId"
        String setInitialExperimentIdBasedOnLastIdProperty = prop.getProperty("setInitialExperimentIdBasedOnLastId");
        if (setInitialExperimentIdBasedOnLastIdProperty != null && setInitialExperimentIdBasedOnLastIdProperty.length() != 0) {
            if (setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("true") || setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("false")) {
                setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("true");
            } else {
                log.warn("Bad value for setInitialExperimentIdBasedOnLastId parameter (" + setInitialExperimentIdBasedOnLastIdProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + setInitialExperimentIdBasedOnLastId);
            }
        } else {
            log.warn("Could not find setInitialExperimentIdBasedOnLastId parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + setInitialExperimentIdBasedOnLastId);
        }

        // load property "logMonitoringRecordTypeIds"
        String logMonitoringRecordTypeIdsProperty = null;
        if (System.getProperty("tpmon.logMonitoringRecordTypeIds") != null) { // we use the present virtual machine parameter value
            logMonitoringRecordTypeIdsProperty = System.getProperty("tpmon.logMonitoringRecordTypeIds");
        } else { // we use the parameter in the properties file
            logMonitoringRecordTypeIdsProperty = prop.getProperty("logMonitoringRecordTypeIds");
        }
        if (logMonitoringRecordTypeIdsProperty != null && logMonitoringRecordTypeIdsProperty.length() != 0) {
            if (logMonitoringRecordTypeIdsProperty.toLowerCase().equals("true") || logMonitoringRecordTypeIdsProperty.toLowerCase().equals("false")) {
                logMonitoringRecordTypeIds = logMonitoringRecordTypeIdsProperty.toLowerCase().equals("true");
            } else {
                log.warn("Bad value for logMonitoringRecordTypeIds parameter (" + logMonitoringRecordTypeIdsProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + logMonitoringRecordTypeIds);
            }
        } else {
            log.warn("Could not find logMonitoringRecordTypeIds parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + logMonitoringRecordTypeIds);
        }

        // load property "asyncRecordQueueSize"
        String asyncRecordQueueSizeProperty = null;
        if (System.getProperty("tpmon.asyncRecordQueueSize") != null) { // we use the present virtual machine parameter value
            asyncRecordQueueSizeProperty = System.getProperty("tpmon.asyncRecordQueueSize");
        } else { // we use the parameter in the properties file
            asyncRecordQueueSizeProperty = prop.getProperty("asyncRecordQueueSize");
        }
        if (asyncRecordQueueSizeProperty != null && asyncRecordQueueSizeProperty.length() != 0) {
            int asyncRecordQueueSizeValue = -1;
            try {
                asyncRecordQueueSizeValue = Integer.parseInt(asyncRecordQueueSizeProperty);
            } catch (NumberFormatException ex) {
            }
            if (asyncRecordQueueSizeValue >= 0) {
                asyncRecordQueueSize = asyncRecordQueueSizeValue;
            } else {
                log.warn("Bad value for asyncRecordQueueSize parameter (" + asyncRecordQueueSizeProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + asyncRecordQueueSize);
            }
        } else {
            log.warn("Could not find asyncRecordQueueSize parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + asyncRecordQueueSize);
        }

        String monitoringEnabledProperty = prop.getProperty("monitoringEnabled");
        if (monitoringEnabledProperty != null && monitoringEnabledProperty.length() != 0) {
            if (monitoringEnabledProperty.toLowerCase().equals("true") || monitoringEnabledProperty.toLowerCase().equals("false")) {
                monitoringEnabled = monitoringEnabledProperty.toLowerCase().equals("true");
            } else {
                log.warn("Bad value for monitoringEnabled parameter (" + monitoringEnabledProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + monitoringEnabled);
            }

        } else {
            log.warn("Could not find monitoringEnabled parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + monitoringEnabled);
        }

        if (monitoringEnabled == false) {
            log.info(">Kieker-Tpmon: Notice, monitoring is deactived (monitoringEnables=false in dbconnector.properties within tpmonLTW.jar)");
        }

        if (debug) {
            log.info(getConnectorInfo());
        }
    }

    @TpmonInternal()
    public String getConnectorInfo() {
        StringBuilder strB = new StringBuilder();

        strB.append("monitoringDataWriter : " + this.monitoringDataWriter.getClass().getCanonicalName());
        strB.append(",");
        strB.append(" monitoringDataWriter config : (below), " + this.monitoringDataWriter.getInfoString());
        strB.append(",");
        strB.append(" version :" + this.getVersion() + ", debug :" + debug + ", enabled :" + isMonitoringEnabled() + ", terminated :" + isMonitoringPermanentlyTerminated() + ", experimentID :" + getExperimentId() + ", vmname :" + getVmname());

        return strB.toString();
    }

    @TpmonInternal()
    public String getDateString() {
        return java.util.Calendar.getInstance().getTime().toString();
    }

    @TpmonInternal()
    public String getVersion() {
        return TpmonVersion.getVERSION();
    }

    @TpmonInternal()
    public final void setDebug(boolean debug) {
        this.debug = debug;
    }

    AtomicInteger nextMonitoringRecordType = new AtomicInteger(1);

    /**
     * Registers monitoring record type and returns its id.
     * If logging of record ids is disabled, -1 is returned and no
     * registration takes place.
     *
     * @param recordTypeClass
     * @return
     */
    @TpmonInternal()
    public final int registerMonitoringRecordType(Class recordTypeClass) {
        if (this.isMonitoringPermanentlyTerminated()) {
            log.warn("Didn't register record type '" + recordTypeClass +
                    "' because monitoring has been permanently terminated");
            return -1;
        }

        String name = recordTypeClass.getCanonicalName();
        if (this.logMonitoringRecordTypeIds) {
            int id = this.nextMonitoringRecordType.getAndIncrement();
            log.info("Registering monitoring record type with id '" + id + "':" + name);
            this.monitoringDataWriter.registerMonitoringRecordType(id, name);
            return id;
        } else {
            log.info("Didn't register the following monitoring record type since " +
                    "logging of type ids disabled: " + name);
            return -1;
        }
    }
}


// TODO: remove Leichen!
// values might be not written to the database in case of an system.exit(0)!
// The place holders are usually much smaller and storage therefore much faster and requires less space.
//private boolean encodeMethodNames = false;
// trace sampling:
// if activated, approximately every n-th (traceSampleingFrequency) trace will be made persistend
// this allows to save the overhead and space for storing data.
// WARNING: Trace sampling should not be used if a session-based evaluation is targeted!
//          For this, a sessionid based sampleing is required (not implemented yet)
//private boolean traceSampleing = false;
//private int traceSampleingFrequency = 2;

// only used if encodeMethodNames == true
//    private HashMap<String, String> methodNameEncoder = new HashMap<String, String>();
// lastEncodedMethodName provides some kind of distributed system unique offset, numbers are increased by 1 for
// each monitoring point after that
// (The following might produce in very very few cases a colision in a large DISTRIBUTED system with a large number
// of instrumented methods. For save usage in a critical distributed system, where the monitoring data is extremely critical,
// only file system storage should be used and component and methodnames should be decoded locally to avoid this problem (or disable encodeMethodNames).)
//    private int lastEncodedMethodName = Math.abs(getVmname().hashCode() % 10000);

//      Not supported any more
//        if (traceSampleing) { // approximately (!) every traceSampleingFrequency-th trace will be monitored
//            if (!(monitoringRecord.traceId % traceSampleingFrequency == 0)) {
//                return true;
//            }
//        }
//log.info("ComponentName "+componentname);
//log.info("Methodname "+methodname);

// methodname: A.a(), componentname: de.comp.A
// therefore componentname+methodname = de.comp.AA.a()
// The "A" is double, this is not nice

//Example:
//ComponentName ts5.de.store.Catalog
//MethodName ts5.de.store.dataModel.Book ts5.de.store.Catalog.getBook(boolean, java.lang.String)

//int whereToCut = methodname.lastIndexOf(".");
//int doublePointPosition = methodname.lastIndexOf("..");
//if (doublePointPosition != -1) whereToCut = methodname.lastIndexOf(".",doublePointPosition-1);
//String newMethodname = ""+methodname.subSequence(whereToCut,methodname.length());

// A methodname looks like this *.*(*
// the "(" is only once in a methodname

// Encoding method and component names stores just placeholders for the component and method names.
// The place holders are usually much smaller and storage therefore much faster and requires less space.
//            if (encodeMethodNames) {
//                String combinedName = componentname + methodname;
//                String encodedName = methodNameEncoder.get(combinedName);
//                if (encodedName == null) { // Method unknown
//                    //           log.info("Kieker-Tpmon: First time logging of "+component+" and "+methodname);
//                    lastEncodedMethodName++; // remember we are synchronized here :)
//                    encodedName = new String("E-" + lastEncodedMethodName); // the method names in java are not allowed to have "-" in it
//                    methodNameEncoder.put(combinedName, encodedName);
//                    storeEncodedName(componentname, formatMethodName(methodname), encodedName);
//                }
//                newMethodname = encodedName;
//                componentname = ""; // we do not need a seperate componentname
//            } else { // methodname is formated, and a full method and component name be made persistent
//                newMethodname = formatMethodName(methodname);
//            }
//        } else {

//        }

//        newMethodname = methodname;

/**
 * This method is only rarely used to store the name recordings in the same
 * datasource that the monitoring data.
 *
 * The encodings can be distinguished from normal monitoring data by
 * tin == tout == executionOrderIndex == executionStacksize == -5.
 * For those entries, the *sessionid* field represents the encoded operation name (= component.method) and
 * the *operation* field will be the full component.methodname.
 *
 * Therefore,
 * grep "-5,-5,-5,-5,-5$" will identify the lines that contain encoding information in monitoring files.
 */
//   Not supported any more
//    @TpmonInternal()
//    private void storeEncodedName(String component, String newMethodname, String encodedName) {
//        // log.info("Kieker-Tpmon: Encoding "+component+""+newMethodname+" by "+encodedName);
//        String opname = component + newMethodname;
//        numberOfInserts.incrementAndGet();
//        AbstractKiekerMonitoringRecord monitoringRecord = AbstractKiekerMonitoringRecord.getInstance();
//        monitoringRecord.componentName = opname;
//        monitoringRecord.opname = encodedName;
//        monitoringRecord.traceId = -5;
//        monitoringRecord.tin = -5;
//        monitoringRecord.tout = -5;
//        monitoringRecord.eoi = -5;
//        monitoringRecord.ess = -5;
//        // NOTE: experimentId and vmname will be set inside insertMonitoringDataNow(.)
//        this.monitoringDataWriter.insertMonitoringDataNow(monitoringRecord);
//    }
/**
 * Internal method to convert the method names into a proper format
 * @param methodname
 * @return methodname without a double componentname
 */
//    @TpmonInternal()
//    private String formatMethodName(String methodname) {
//        // methodname: A.a(), componentname: de.comp.A
//        // therefore componentname+methodname = de.comp.AA.a()
//        // The "A" is double, this is not nice
//
//        //Example:
//        //ComponentName ts5.de.store.Catalog
//        //MethodName ts5.de.store.dataModel.Book ts5.de.store.Catalog.getBook(boolean, java.lang.String)
//
//        //int whereToCut = methodname.lastIndexOf(".");
//        //int doublePointPosition = methodname.lastIndexOf("..");
//        //if (doublePointPosition != -1) whereToCut = methodname.lastIndexOf(".",doublePointPosition-1);
//        //String newMethodname = ""+methodname.subSequence(whereToCut,methodname.length());
//
//        // A methodname looks like this *.*(*
//        // the "(" is only once in a methodname
//
//        int indexOfOpenBrace = methodname.lastIndexOf("(");
//        int indexBeginOfMethodname = methodname.lastIndexOf(".", indexOfOpenBrace);
//        if (indexBeginOfMethodname == -1) {
//            return methodname;
//        } else {
//            return methodname.substring(indexBeginOfMethodname, methodname.length()).replaceAll(" ", "");
//        }
//    }
//private long seed = 0;
//private double d3 = 0.3d;
