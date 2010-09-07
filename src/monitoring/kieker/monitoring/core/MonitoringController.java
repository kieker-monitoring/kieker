package kieker.monitoring.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import kieker.common.util.Version;
import kieker.common.record.AbstractMonitoringRecord;

import kieker.monitoring.writer.util.async.TpmonShutdownHook;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.database.SyncDbWriter;
import kieker.monitoring.writer.filesystem.SyncFsWriter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import kieker.common.record.DummyMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.writer.database.AsyncDbWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;
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
 * Simple class to store monitoring data in the file system. Although a
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
 * 2008/05/29: Changed vmid to vmName (defaults to hostname),
 *             which may be changed during runtime
 * 2008/01/04: Refactoring for the first release of 
 *             Kieker and publication under an open source licence
 * 2007/03/13: Refactoring
 * 2006/12/20: Initial Prototype
 */
public final class MonitoringController implements IMonitoringRecordReceiver {

    private static final Log log = LogFactory.getLog(MonitoringController.class);
    private static final MonitoringController ctrlInst = new MonitoringController();
    //marks the end of monitoring to the writer threads
    public static final AbstractMonitoringRecord END_OF_MONITORING_MARKER = new DummyMonitoringRecord();
    public final static String WRITER_SYNCDB = "SyncDB";
    public final static String WRITER_ASYNCDB = "AsyncDB";
    public final static String WRITER_SYNCFS = "SyncFS";
    public final static String WRITER_ASYNCFS = "AsyncFS";
    private final TpmonShutdownHook shutdownhook = new TpmonShutdownHook();
    private String monitoringDataWriterClassname = null;
    private String monitoringDataWriterInitString = null;
    private String dbDriverClassname = "com.mysql.jdbc.Driver";
    private String dbConnectionAddress = "jdbc:mysql://HOSTNAME/DATABASENAME?user=DBUSER&password=DBPASS";
    private String dbTableName = "turbomon10";

    public enum ControllerMode {

        /**
         * The loggingTimestamp is not set by the newMonitoringRecord method.
         * This is required to replay recorded traces with the original
         * timestamps.
         */
        REPLAY,
        /**
         * The controller sets the loggingTimestamp of incoming records
         * according to the current time.
         */
        REALTIME
    }
    private ControllerMode controllerMode = ControllerMode.REALTIME;

    private enum DebugMode {

        ENABLED, DISABLED;

        /** Returns true iff debug is enabled. */
        public final boolean isDebugEnabled() {
            return this.equals(ENABLED);
        }

        ;
    };

    private enum ControllerState {

        ENABLED, DISABLED, TERMINATED;
    }
    // The following variables are declared volatile since they are access by
    // multiple threads
    private final AtomicReference<ControllerState> controllerState =
            new AtomicReference<ControllerState>(ControllerState.ENABLED);
    private volatile DebugMode debugMode = DebugMode.DISABLED;
    private volatile String filenamePrefix = ""; // e.g. path "/tmp/"
    private volatile boolean storeInJavaIoTmpdir = true;
    private volatile String customStoragePath = "/tmp"; // only used as default if storeInJavaIoTmpdir == false
    private volatile int asyncRecordQueueSize = 8000;
    private volatile boolean asyncBlockOnFullQueue = false;
    private volatile IMonitoringLogWriter monitoringLogWriter = null;
    private volatile String vmName = "unknown";    // the following configuration values are overwritten by tpmonLTW.properties in tpmonLTW.jar
    // database only configuration configuration values that are overwritten by kieker.monitoring.properties included in the kieker.monitoring library
    private volatile boolean setInitialExperimentIdBasedOnLastId = false;    // only use the asyncDbconnector in server environments, that do not directly terminate after the executions, or some

    private static final String KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME =
            "kieker.monitoring.configuration";
    private static final String KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH =
            "META-INF/kieker.monitoring.properties";
    private static final String KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT =
            "META-INF/kieker.monitoring.properties.default";

    /** Returns the singleton instance. */
    public static MonitoringController getInstance() {


        return MonitoringController.ctrlInst;
    }

    /**
     * Creates a new monitoring controller instance based on the configuration
     * provided by the properties props.
     *
     * Note, that in this case, no Kieker properties passed to the JVM are
     * evaluated.
     *
     * @param props
     * @return
     */
    public static MonitoringController createInstance (final Properties props){
        return new MonitoringController(props,
                false // do not consider system properties
                );
    }

    /**
     * Creates a new monitoring controller instance based on the configuration
     * file configurationFn.
     *
     * Note, that in this case, no Kieker properties passed to the JVM are
     * evaluated.
     *
     * @param configurationFn
     * @return
     */
    public static MonitoringController createInstance (final String configurationFn) throws FileNotFoundException, IOException{
        return new MonitoringController(
                loadPropertiesFromFile(configurationFn),
                false // do not consider system properties
                );
    }

    /**
     * Returns the properties loaded from file propertiesFn.
     *
     * @param propertiesFn
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static Properties loadPropertiesFromFile (final String propertiesFn) throws FileNotFoundException, IOException{
        InputStream is = new FileInputStream(propertiesFn);
        Properties prop = new Properties();
        prop.load(is);
        return prop;
    }

    /**
     * Returns the properties loaded from the resource name or null if the
     * resource could not be found.
     *
     * @param classLoader
     * @param name
     * @return
     * @throws IOException
     */
    private static Properties loadPropertiesFromResource (final String name) throws IOException{
        InputStream is = MonitoringController.class.getClassLoader().getResourceAsStream(name);
        if (is == null) {
            return null;
        }
        Properties prop = new Properties();

        prop.load(is);
        return prop;
    }

    /**
     * Returns the properties used to construct the singleton instance.
     * If a custom configuration file location is passed to the JVM using the
     * property kieker.monitoring.configuration, this the properties are
     * loaded from this file. Otherwise, the method searches for a
     * configuration file META-INF/kieker.monitoring.properties in the classpath,
     * and if this does not exist, it loads the default properties contained in
     * the Kieker jar. 
     *
     * @return
     */
    private static Properties loadSingletonProperties() {
        Properties prop = null; // = new Properties();
        String configurationFile = null;
        try {
            /* 1. Searching for configuration file location passed to JVM */
            if (System.getProperty(KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME) != null) {
                configurationFile = System.getProperty(KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME);
                log.info("Loading properties from JVM-specified location '" + configurationFile + "'");
                prop = loadPropertiesFromFile(configurationFile);
            } else {
                /* 2. No JVM property; 
                 *    Trying to find configuration file in classpath */
                configurationFile = KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
                prop = loadPropertiesFromResource(configurationFile);
                if (prop != null) { // success
                    log.info("Loading properties from properties file in classpath: " + configurationFile);
                    log.info("You can specify an alternative properties file using the property '" + KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME + "'");
                } else {
                    /* 3. No  */
                    configurationFile = KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT;
                    log.info("Loading properties from Kieker.Monitoring library jar!" + configurationFile);
                    log.info("You can specify an alternative properties file using the property '" + KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME + "'");
                    prop = loadPropertiesFromResource(configurationFile);
                }
            }
        } catch (Exception ex) {
            log.error("Error loading kieker configuration file '" + configurationFile + "'", ex);
            // TODO: introduce static variable 'terminated' or alike
        }
        return prop;
    }

    /**
     * Constructs a controller instance which is configured based on the
     * properties props. If the boolean flag considerSystemProperties is set to 
     * true, the Kieker configuration properties passed the JVM are considered
     * to override properties in props.
     *
     * @param props the configuration properties
     * @param considerSystemProperties whether Kieker configuration parameters
     * passed to the JVM shall be considered
     */
    private MonitoringController(final Properties props, final boolean considerSystemProperties) {
        try {
            vmName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            log.warn("Failed to get hostname", ex);
        } // nothing to do -- vmName will be "unknown"

        log.info("The VM has the name " + vmName + " Thread:"
                + Thread.currentThread().getId());
        log.info("Virtual Machine start time "
                + ManagementFactory.getRuntimeMXBean().getStartTime());

        Runtime.getRuntime().addShutdownHook(shutdownhook);

        initFromProperties(props, considerSystemProperties);

        /* We will now determine and load the monitoring Writer to use */
        try {
            if (this.monitoringDataWriterClassname == null || this.monitoringDataWriterClassname.length() == 0) {
                throw new Exception("Property monitoringDataWriter not set");
            } else if (this.monitoringDataWriterClassname.equals(WRITER_SYNCFS)) {
                String filenameBase = filenamePrefix;
                this.monitoringLogWriter = new SyncFsWriter(filenameBase);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_ASYNCFS)) {
                String filenameBase = filenamePrefix;
                this.monitoringLogWriter = new AsyncFsWriter(filenameBase, asyncRecordQueueSize, asyncBlockOnFullQueue);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_SYNCDB)) {
                this.monitoringLogWriter = new SyncDbWriter(
                        dbDriverClassname, dbConnectionAddress,
                        dbTableName,
                        setInitialExperimentIdBasedOnLastId);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_ASYNCDB)) {
                this.monitoringLogWriter = new AsyncDbWriter(
                        dbDriverClassname, dbConnectionAddress,
                        dbTableName,
                        setInitialExperimentIdBasedOnLastId, asyncRecordQueueSize, asyncBlockOnFullQueue);
            } else {
                /* try to load the class by name */
                this.monitoringLogWriter = (IMonitoringLogWriter) Class.forName(this.monitoringDataWriterClassname).newInstance();
                //add asyncRecordQueueSize
                monitoringDataWriterInitString += " | asyncRecordQueueSize=" + asyncRecordQueueSize;
                if (!this.monitoringLogWriter.init(monitoringDataWriterInitString)) {
                    this.monitoringLogWriter = null;
                    throw new Exception("Initialization of writer failed!");
                }

            }
            Vector<AbstractWorkerThread> worker = this.monitoringLogWriter.getWorkers(); // may be null
            if (worker != null) {
                for (AbstractWorkerThread w : worker) {
                    this.registerWorker(w);
                }
            }
            // TODO: we should add a getter to all writers like isInitialized.
            //       right now, the following even appears in case init failed.
            //       Or can we simply throw an exception from within the constructors
            log.info("Initialization completed.\n Writer Info: " + this.getConnectorInfo());
        } catch (Exception exc) {
            log.error("Disabling monitoring", exc);
            this.terminate();
        }
    }

    /**
     * Constructor used for singleton instance. 
     */
    private MonitoringController() {
        this(loadSingletonProperties(),
                true // consider Kieker properties passed to JVM
                );
    }

    /**
     * The vmName which defaults to the hostname, and may be set by the control-servlet.
     * The vmName will be part of the monitoring data and allows to assing observations
     * in cases where the software system is deployed on more than one host.
     * 
     * When you want to distinguish multiple Virtual Machines on one host,
     * you have to set the vmName manually (e.g., via the control-servlet,
     * or by directly implementing a call to MonitoringController.setVmname(...).
     */
    public final String getVmName() {
        return this.vmName;
    }

    /**
     * Allows to set an own vmName, a field in the monitoring data to distinguish
     * multiple hosts / vms in a system. This method is for instance used by
     * the Kieker.Monitoring control servlet.
     * 
     * The vmName defaults to the hostname.
     * 
     * When you want to distinguish multiple Virtual Machines on one host,
     * you have to set the vmName manually (e.g., via the control-servlet,
     * or by directly implementing a call to MonitoringController.setVmname(...).
     * 
     * @param newVmname
     */
    public final void setVmname(String newVmname) {
        log.info("The VM has the NEW name " + newVmname
                + " Thread:" + Thread.currentThread().getId());
        this.vmName = newVmname;
    }

    /**
     * See TpmonShutdownHook.registerWorker
     * @param newWorker
     */
    private void registerWorker(AbstractWorkerThread newWorker) {
        this.shutdownhook.registerWorker(newWorker);
    }
    private AtomicLong numberOfInserts = new AtomicLong(0);

    public final boolean isDebug() {
        return this.debugMode.equals(DebugMode.ENABLED);
    }

    /**
     * Shows how many inserts have been performed since last restart of the execution
     * environment.
     */
    public long getNumberOfInserts() {
        return numberOfInserts.longValue();
    }

    public final boolean isMonitoringEnabled() {
        return this.controllerState.get().equals(ControllerState.ENABLED);
    }

    public final boolean isMonitoringPermanentlyTerminated() {
        return this.controllerState.get().equals(ControllerState.TERMINATED);
    }
    private static final int DEFAULT_EXPERIMENTID = 0;
    private AtomicInteger experimentId = new AtomicInteger(DEFAULT_EXPERIMENTID);

    public final int getExperimentId() {
        return this.experimentId.intValue();
    }

    /** Increments the experiment ID by 1 and returns the new value. */
    public synchronized int incExperimentId() {
        return this.experimentId.incrementAndGet();
    }

    public void setExperimentId(int newExperimentID) {
        this.experimentId.set(newExperimentID);
    }

    /**
     * Enables monitoring.
     *
     * @throws IllegalStateException if controller has been terminated prior to call
     */
    public final void enable() {
        log.info("Enabling monitoring");
        synchronized (this.controllerState) {
            if (this.controllerState.get().equals(ControllerState.TERMINATED)) {
                IllegalStateException ex = new IllegalStateException("Refused to enable monitoring because monitoring has been permanently terminated before");
                log.error("Monitoring cannot be enabled", ex);
                throw ex;
            }
            this.controllerState.set(ControllerState.ENABLED);
        }
    }

    /**
     * Disables monitoring.
     * Monitoring may be enabled again by calling enableMonitoring().
     *
     * @throws IllegalStateException if controller has been terminated prior to call
     */
    public final void disable() {
        log.info("Disabling monitoring");
        synchronized (this.controllerState) {
            if (this.controllerState.get().equals(ControllerState.TERMINATED)) {
                IllegalStateException ex = new IllegalStateException("Refused to enable monitoring because monitoring has been permanently terminated before");
                log.error("Monitoring cannot be enabled", ex);
                throw ex;
            }
            this.controllerState.set(ControllerState.DISABLED);
        }
    }

    /**
     * Permanently terminates monitoring (e.g., due to a failure).
     * Subsequent tries to enable monitoring will be refused.
     */
    public final synchronized void terminate() {
        log.info("Permanently terminating monitoring");
        synchronized (this.controllerState) {
            if (this.monitoringLogWriter != null) {
                /* if the initialization of the writer failed, it is set to null*/
                if (!this.monitoringLogWriter.newMonitoringRecord(END_OF_MONITORING_MARKER)) {
                    log.error("Failed to terminate writer");
                }
            }
            this.controllerState.set(ControllerState.TERMINATED);
        }
    }

    /**
     * Enables or disables the replay mode (for monitoring the value should 
     * be false, i.e. the replay mode is disabled). 
     * If the controller is in replay mode, the logMonitoringRecord method
     * does not set the logging timestamp of the passed monitoring record.
     *
     * @param replayMode
     */
    public final void setControllerMode(final ControllerMode mode) {
        this.controllerMode = mode;
    }

    /**
     * Passes the given monitoring record to the configured writer if the
     * controller is enabled.
     * 
     * If the controller is in replay mode (usually, this is only required to replay
     * already recorded log data), the logMonitoringRecord method does not set the logging
     * timestamp of the passed monitoring record.
     *
     * Notice, that this method won't throw any exceptions.
     *
     * @param monitoringRecord the record to be logged
     * @return true if the record has been passed the writer successfully; false
     *         in case an error occured or the controller is not enabled.
     */
    public final boolean newMonitoringRecord(final IMonitoringRecord record) {
        try {
            if (!this.controllerState.get().equals(ControllerState.ENABLED)) {
                return false;
            }
            numberOfInserts.incrementAndGet();
            if (this.controllerMode.equals(ControllerMode.REALTIME)) {
                record.setLoggingTimestamp(this.currentTimeNanos());
            }
            if (!this.monitoringLogWriter.newMonitoringRecord(record)) {
                log.fatal("Error writing the monitoring data. Will terminate monitoring!");
                this.terminate();
                return false;
            }
            return true;
        } catch (Exception ex) {
            log.error("Caught an Exception. Will terminate monitoring", ex);
            this.terminate();
            return false;
        }
    }
    /** Offset used to determine the number of nanoseconds since 1970-1-1.
     *  This is necessary since System.nanoTime() returns the elapsed nanoseconds
     *  since *some* fixed but arbitrary time.)
     */
    private static final long offsetA = System.currentTimeMillis() * 1000000 - System.nanoTime();

    /**
     * Returns the timestamp for the current time.
     * The value corresponds to the number of nano seconds elapsed  Jan 1, 1970 UTC.
     */
    public final long currentTimeNanos() {
        return System.nanoTime() + offsetA;
    }

    /**    
     *  Loads properties from configuration file. 
     */
    private void initFromProperties(final Properties props, final boolean considerSystemProperties) {
        // load property monitoringLogWriter
        monitoringDataWriterClassname = props.getProperty("monitoringDataWriter");
        monitoringDataWriterInitString = props.getProperty("monitoringDataWriterInitString");

        String dbDriverClassnameProperty;
        if (considerSystemProperties && System.getProperty("kieker.monitoring.dbConnectionAddress") != null) { // we use the present virtual machine parameter value
            dbDriverClassnameProperty = System.getProperty("kieker.monitoring.dbDriverClassname");
        } else { // we use the parameter in the properties file
            dbDriverClassnameProperty = props.getProperty("dbDriverClassname");
        }
        if (dbDriverClassnameProperty != null && dbDriverClassnameProperty.length() != 0) {
            dbDriverClassname = dbDriverClassnameProperty;
        } else {
            log.info("No dbDriverClassname parameter found"
                    + ". Using default value " + dbDriverClassname + ".");
        }

        // load property "dbConnectionAddress"
        String dbConnectionAddressProperty;
        if (considerSystemProperties && System.getProperty("kieker.monitoring.dbConnectionAddress") != null) { // we use the present virtual machine parameter value
            dbConnectionAddressProperty = System.getProperty("kieker.monitoring.dbConnectionAddress");
        } else { // we use the parameter in the properties file
            dbConnectionAddressProperty = props.getProperty("dbConnectionAddress");
        }
        if (dbConnectionAddressProperty != null && dbConnectionAddressProperty.length() != 0) {
            dbConnectionAddress = dbConnectionAddressProperty;
        } else {
            log.warn("No dbConnectionAddress parameter found"
                    + ". Using default value " + dbConnectionAddress + ".");
        }

// the filenamePrefix (folder where Kieker.Monitoring stores its data)
// for monitoring data depends on the properties kieker.monitoring.storeInJavaIoTmpdir
// and kieker.monitoring.customStoragePath
// these both parameters may be provided (with higher priority) as java command line parameters as well (example in the properties file)
        String storeInJavaIoTmpdirProperty;
        if (considerSystemProperties && System.getProperty("kieker.monitoring.storeInJavaIoTmpdir") != null) { // we use the present virtual machine parameter value
            storeInJavaIoTmpdirProperty = System.getProperty("kieker.monitoring.storeInJavaIoTmpdir");
        } else { // we use the parameter in the properties file
            storeInJavaIoTmpdirProperty = props.getProperty("kieker.monitoring.storeInJavaIoTmpdir");
        }

        if (storeInJavaIoTmpdirProperty != null && storeInJavaIoTmpdirProperty.length() != 0) {
            if (storeInJavaIoTmpdirProperty.toLowerCase().equals("true") || storeInJavaIoTmpdirProperty.toLowerCase().equals("false")) {
                storeInJavaIoTmpdir = storeInJavaIoTmpdirProperty.toLowerCase().equals("true");
            } else {
                log.warn("Bad value for kieker.monitoring.storeInJavaIoTmpdir (or provided via command line) parameter (" + storeInJavaIoTmpdirProperty + ")"
                        + ". Using default value " + storeInJavaIoTmpdir);
            }
        } else {
            log.warn("No kieker.monitoring.storeInJavaIoTmpdir parameter found"
                    + " (or provided via command line). Using default value '" + storeInJavaIoTmpdir + "'.");
        }

        if (storeInJavaIoTmpdir) {
            filenamePrefix = System.getProperty("java.io.tmpdir");
        } else { // only now we consider kieker.monitoring.customStoragePath
            String customStoragePathProperty;
            if (considerSystemProperties && System.getProperty("kieker.monitoring.customStoragePath") != null) { // we use the present virtual machine parameter value
                customStoragePathProperty = System.getProperty("kieker.monitoring.customStoragePath");
            } else { // we use the parameter in the properties file
                customStoragePathProperty = props.getProperty("kieker.monitoring.customStoragePath");
            }

            if (customStoragePathProperty != null && customStoragePathProperty.length() != 0) {
                filenamePrefix = customStoragePathProperty;
            } else {
                log.warn("No kieker.monitoring.customStoragePath parameter found"
                        + " (or provided via command line). Using default value '" + customStoragePath + "'.");
                filenamePrefix =
                        customStoragePath;
            }
        }

        // load property "dbTableNameProperty"
        String dbTableNameProperty;
        if (considerSystemProperties && System.getProperty("kieker.monitoring.dbTableName") != null) { // we use the present virtual machine parameter value
            dbTableNameProperty = System.getProperty("kieker.monitoring.dbTableName");
        } else { // we use the parameter in the properties file
            dbTableNameProperty = props.getProperty("dbTableName");
        }
        if (dbTableNameProperty != null && dbTableNameProperty.length() != 0) {
            dbTableName = dbTableNameProperty;
        } else {
            log.warn("No dbTableName  parameter found"
                    + ". Using default value " + dbTableName + ".");
        }

        // load property "debug"
        String debugProperty;
        if (considerSystemProperties && System.getProperty("kieker.monitoring.debug") != null) { // we use the present virtual machine parameter value
            debugProperty = System.getProperty("kieker.monitoring.debug");
        } else { // we use the parameter in the properties file
            debugProperty = props.getProperty("debug");
        }
        if (debugProperty != null && debugProperty.length() != 0) {
            if (debugProperty.toLowerCase().equals("true") || debugProperty.toLowerCase().equals("false")) {
                if (debugProperty.toLowerCase().equals("true")) {
                    log.info("Debug mode enabled");
                    this.debugMode = DebugMode.ENABLED;
                } else {
                    log.info("Debug mode disabled");
                    this.debugMode = DebugMode.DISABLED;
                }
            } else {
                log.warn("Bad value for debug parameter (" + debugProperty + ")"
                        + ". Using default value " + this.debugMode.isDebugEnabled());
            }
        } else {
            log.warn("Could not find debug parameter"
                    + ". Using default value " + this.debugMode.isDebugEnabled());
        }

        // load property "setInitialExperimentIdBasedOnLastId"
        String setInitialExperimentIdBasedOnLastIdProperty = props.getProperty("setInitialExperimentIdBasedOnLastId");
        if (setInitialExperimentIdBasedOnLastIdProperty != null && setInitialExperimentIdBasedOnLastIdProperty.length() != 0) {
            if (setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("true") || setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("false")) {
                setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("true");
            } else {
                log.warn("Bad value for setInitialExperimentIdBasedOnLastId parameter (" + setInitialExperimentIdBasedOnLastIdProperty + ")"
                        + ". Using default value " + setInitialExperimentIdBasedOnLastId);
            }
        } else {
            log.warn("Could not find setInitialExperimentIdBasedOnLastId parameter"
                    + ". Using default value " + setInitialExperimentIdBasedOnLastId);
        }

        // load property "asyncRecordQueueSize"
        String asyncRecordQueueSizeProperty = null;
        if (considerSystemProperties && System.getProperty("kieker.monitoring.asyncRecordQueueSize") != null) { // we use the present virtual machine parameter value
            asyncRecordQueueSizeProperty = System.getProperty("kieker.monitoring.asyncRecordQueueSize");
        } else { // we use the parameter in the properties file
            asyncRecordQueueSizeProperty = props.getProperty("asyncRecordQueueSize");
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
                log.warn("Bad value for asyncRecordQueueSize parameter (" + asyncRecordQueueSizeProperty + ")"
                        + ". Using default value " + asyncRecordQueueSize);
            }
        } else {
            log.warn("Could not find asyncRecordQueueSize parameter"
                    + ". Using default value " + asyncRecordQueueSize);
        }

        // load property "asyncBlockOnFullQueue"
        String asyncBlockOnFullQueueProperty = null;
        if (considerSystemProperties && System.getProperty("kieker.monitoring.asyncBlockOnFullQueue") != null) { // we use the present virtual machine parameter value
            asyncBlockOnFullQueueProperty = System.getProperty("kieker.monitoring.asyncBlockOnFullQueue");
        } else { // we use the parameter in the properties file
            asyncBlockOnFullQueueProperty = props.getProperty("asyncBlockOnFullQueue");
        }
        if (asyncBlockOnFullQueueProperty != null && asyncBlockOnFullQueueProperty.length() != 0) {
            asyncBlockOnFullQueue = Boolean.parseBoolean(asyncBlockOnFullQueueProperty);
            log.info("Using asyncBlockOnFullQueue value (" + asyncBlockOnFullQueueProperty + ")"
                    + ". Using default value " + asyncBlockOnFullQueue);
        } else {
            log.warn("Could not find asyncBlockOnFullQueue"
                    + ". Using default value " + asyncBlockOnFullQueue);
        }

        String monitoringEnabledProperty = props.getProperty("monitoringEnabled");
        if (monitoringEnabledProperty != null && monitoringEnabledProperty.length() != 0) {
            if (monitoringEnabledProperty.toLowerCase().equals("true") || monitoringEnabledProperty.toLowerCase().equals("false")) {
                if (monitoringEnabledProperty.toLowerCase().equals("true")) {
                    this.controllerState.set(ControllerState.ENABLED);
                } else {
                    this.controllerState.set(ControllerState.DISABLED);
                }
            } else {
                log.warn("Bad value for monitoringEnabled parameter (" + monitoringEnabledProperty + ")"
                        + ". Using default value " + this.controllerState.get().equals(ControllerState.ENABLED));
            }

        } else {
            log.warn("Could not find monitoringEnabled parameter"
                    + ". Using default value " + this.controllerState.get().equals(ControllerState.ENABLED));
        }

        if (!this.controllerState.get().equals(ControllerState.ENABLED)) {
            log.info("Monitoring is not enabled");
        }
    }

    /** Returns a human-readable information string about the controller configuration.
     *  @return the information string
     */
    public String getConnectorInfo() {
        StringBuilder strB = new StringBuilder();

        strB.append("monitoringDataWriter : " + this.monitoringLogWriter.getClass().getCanonicalName());
        strB.append(",");
        strB.append(" monitoringDataWriter config : (below), " + this.monitoringLogWriter.getInfoString());
        strB.append(",");
        strB.append(" version :" + this.getVersion() + ", debug :" + this.debugMode.isDebugEnabled() + ", enabled :" + isMonitoringEnabled() + ", terminated :" + isMonitoringPermanentlyTerminated() + ", experimentID :" + getExperimentId() + ", vmname :" + getVmName());

        return strB.toString();
    }

    /**
     * Returns a human-readable string with the current date and time.
     *
     * @return the date/time string.
     */
    public String getDateString() {
        return java.util.Calendar.getInstance().getTime().toString();
    }

    /**
     * Return the version name of this controller instance.
     * @return the version name
     */
    public String getVersion() {
        return Version.getVERSION();
    }

    /**
     * Sets the debug mode to the given parameter value.
     *
     * @param debug iff true, debug mode is enabled
     */
    public final void setDebug(boolean debug) {
        if (debug) {
            this.debugMode = DebugMode.ENABLED;
        } else {
            this.debugMode = DebugMode.DISABLED;
        }
    }
}
