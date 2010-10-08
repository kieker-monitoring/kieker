package kieker.monitoring.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.DummyMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.common.util.Version;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.database.AsyncDbWriter;
import kieker.monitoring.writer.database.SyncDbWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;
import kieker.monitoring.writer.filesystem.SyncFsWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;
import kieker.monitoring.writer.util.async.TpmonShutdownHook;

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
    private final TpmonShutdownHook shutdownhook;
    private String monitoringDataWriterClassname = null;
    private String monitoringDataWriterInitString = null;
    private String dbDriverClassname = "com.mysql.jdbc.Driver";
    private String dbConnectionAddress = "jdbc:mysql://HOSTNAME/DATABASENAME?user=DBUSER&password=DBPASS";
    private String dbTableName = "turbomon10";

    private final String instanceName;

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
    public static MonitoringController createInstance (final String instanceName, final Properties props){
        return new MonitoringController(instanceName, props,
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
    public static MonitoringController createInstance (final String instanceName, final String configurationFn) throws FileNotFoundException, IOException{
        return new MonitoringController(
                instanceName,
                MonitoringController.loadPropertiesFromFile(configurationFn),
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
        final InputStream is = new FileInputStream(propertiesFn);
        final Properties prop = new Properties();
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
        final InputStream is = MonitoringController.class.getClassLoader().getResourceAsStream(name);
        if (is == null) {
            return null;
        }
        final Properties prop = new Properties();

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
            if (System.getProperty(MonitoringController.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME) != null) {
                configurationFile = System.getProperty(MonitoringController.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME);
                MonitoringController.log.info("Loading properties from JVM-specified location '" + configurationFile + "'");
                prop = MonitoringController.loadPropertiesFromFile(configurationFile);
            } else {
                /* 2. No JVM property; 
                 *    Trying to find configuration file in classpath */
                configurationFile = MonitoringController.KIEKER_CUSTOM_PROPERTIES_LOCATION_CLASSPATH;
                prop = MonitoringController.loadPropertiesFromResource(configurationFile);
                if (prop != null) { // success
                    MonitoringController.log.info("Loading properties from properties file in classpath: " + configurationFile);
                    MonitoringController.log.info("You can specify an alternative properties file using the property '" + MonitoringController.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME + "'");
                } else {
                    /* 3. No  */
                    configurationFile = MonitoringController.KIEKER_CUSTOM_PROPERTIES_LOCATION_DEFAULT;
                    MonitoringController.log.info("Loading properties from Kieker.Monitoring library jar!" + configurationFile);
                    MonitoringController.log.info("You can specify an alternative properties file using the property '" + MonitoringController.KIEKER_CUSTOM_CONFIGURATION_JVM_PROP_NAME + "'");
                    prop = MonitoringController.loadPropertiesFromResource(configurationFile);
                }
            }
        } catch (final Exception ex) {
            MonitoringController.log.error("Error loading kieker configuration file '" + configurationFile + "'", ex);
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
    private MonitoringController(final String instanceName, final Properties props, final boolean considerSystemProperties) {
        this.instanceName = instanceName;
        try {
            this.vmName = java.net.InetAddress.getLocalHost().getHostName();
        } catch (final Exception ex) {
            MonitoringController.log.warn("Failed to get hostname", ex);
        } // nothing to do -- vmName will be "unknown"

        MonitoringController.log.info("The VM has the name " + this.vmName + " Thread:"
                + Thread.currentThread().getId());
        MonitoringController.log.info("Virtual Machine start time "
                + ManagementFactory.getRuntimeMXBean().getStartTime());

        this.shutdownhook = new TpmonShutdownHook();
        
        try {
        Runtime.getRuntime().addShutdownHook(this.shutdownhook);
        } catch (final Exception e){
        	MonitoringController.log.warn("Failed to add shutdownHook", e);
        }

        this.initFromProperties(props, considerSystemProperties);

        /* We will now determine and load the monitoring Writer to use */
        try {
            if ((this.monitoringDataWriterClassname == null) || (this.monitoringDataWriterClassname.length() == 0)) {
                throw new Exception("Property monitoringDataWriter not set");
            } else if (this.monitoringDataWriterClassname.equals(MonitoringController.WRITER_SYNCFS)) {
                final String filenameBase = this.filenamePrefix;
                this.monitoringLogWriter = new SyncFsWriter(filenameBase, this.instanceName);
            } else if (this.monitoringDataWriterClassname.equals(MonitoringController.WRITER_ASYNCFS)) {
                final String filenameBase = this.filenamePrefix;
                this.monitoringLogWriter = new AsyncFsWriter(filenameBase, this.instanceName, this.asyncRecordQueueSize, this.asyncBlockOnFullQueue);
            } else if (this.monitoringDataWriterClassname.equals(MonitoringController.WRITER_SYNCDB)) {
                this.monitoringLogWriter = new SyncDbWriter(
                        this.dbDriverClassname, this.dbConnectionAddress,
                        this.dbTableName,
                        this.setInitialExperimentIdBasedOnLastId);
            } else if (this.monitoringDataWriterClassname.equals(MonitoringController.WRITER_ASYNCDB)) {
                this.monitoringLogWriter = new AsyncDbWriter(
                        this.dbDriverClassname, this.dbConnectionAddress,
                        this.dbTableName,
                        this.setInitialExperimentIdBasedOnLastId, this.asyncRecordQueueSize, this.asyncBlockOnFullQueue);
            } else {
                /* try to load the class by name */
                this.monitoringLogWriter = (IMonitoringLogWriter) Class.forName(this.monitoringDataWriterClassname).newInstance();
                //add asyncRecordQueueSize
                this.monitoringDataWriterInitString += " | asyncRecordQueueSize=" + this.asyncRecordQueueSize;
                if (!this.monitoringLogWriter.init(this.monitoringDataWriterInitString)) {
                    this.monitoringLogWriter = null;
                    throw new Exception("Initialization of writer failed!");
                }

            }
            final Vector<AbstractWorkerThread> worker = this.monitoringLogWriter.getWorkers(); // may be null
            if (worker != null) {
                for (final AbstractWorkerThread w : worker) {
                    this.registerWorker(w);
                }
            }
            // TODO: we should add a getter to all writers like isInitialized.
            //       right now, the following even appears in case init failed.
            //       Or can we simply throw an exception from within the constructors
            MonitoringController.log.info("Initialization completed.\n Writer Info: " + this.getConnectorInfo());
        } catch (final Exception exc) {
            MonitoringController.log.error("Disabling monitoring", exc);
            this.terminate();
        }
    }

    private static final String SINGLETON_INSTANCE_NAME = "SINGLETON";

    /**
     * Constructor used for singleton instance. 
     */
    private MonitoringController() {
        this(   MonitoringController.SINGLETON_INSTANCE_NAME,
                MonitoringController.loadSingletonProperties(),
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
    public final void setVmname(final String newVmname) {
        MonitoringController.log.info("The VM has the NEW name " + newVmname
                + " Thread:" + Thread.currentThread().getId());
        this.vmName = newVmname;
    }

    /**
     * See TpmonShutdownHook.registerWorker
     * @param newWorker
     */
    private void registerWorker(final AbstractWorkerThread newWorker) {
        this.shutdownhook.registerWorker(newWorker);
    }
    private final AtomicLong numberOfInserts = new AtomicLong(0);

    public final boolean isDebug() {
        return this.debugMode.equals(DebugMode.ENABLED);
    }

    /**
     * Shows how many inserts have been performed since last restart of the execution
     * environment.
     */
    public long getNumberOfInserts() {
        return this.numberOfInserts.longValue();
    }

    public final boolean isMonitoringEnabled() {
        return this.controllerState.get().equals(ControllerState.ENABLED);
    }

    public final boolean isMonitoringPermanentlyTerminated() {
        return this.controllerState.get().equals(ControllerState.TERMINATED);
    }
    private static final int DEFAULT_EXPERIMENTID = 0;
    private final AtomicInteger experimentId = new AtomicInteger(MonitoringController.DEFAULT_EXPERIMENTID);

    public final int getExperimentId() {
        return this.experimentId.intValue();
    }

    /** Increments the experiment ID by 1 and returns the new value. */
    public synchronized int incExperimentId() {
        return this.experimentId.incrementAndGet();
    }

    public void setExperimentId(final int newExperimentID) {
        this.experimentId.set(newExperimentID);
    }

    /**
     * Enables monitoring.
     *
     * @throws IllegalStateException if controller has been terminated prior to call
     */
    public final void enable() {
        MonitoringController.log.info("Enabling monitoring");
        synchronized (this.controllerState) {
            if (this.controllerState.get().equals(ControllerState.TERMINATED)) {
                final IllegalStateException ex = new IllegalStateException("Refused to enable monitoring because monitoring has been permanently terminated before");
                MonitoringController.log.error("Monitoring cannot be enabled", ex);
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
        MonitoringController.log.info("Disabling monitoring");
        synchronized (this.controllerState) {
            if (this.controllerState.get().equals(ControllerState.TERMINATED)) {
                final IllegalStateException ex = new IllegalStateException("Refused to enable monitoring because monitoring has been permanently terminated before");
                MonitoringController.log.error("Monitoring cannot be enabled", ex);
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
        MonitoringController.log.info("Permanently terminating monitoring");
        synchronized (this.controllerState) {
            if (this.monitoringLogWriter != null) {
                /* if the initialization of the writer failed, it is set to null*/
                if (!this.monitoringLogWriter.newMonitoringRecord(MonitoringController.END_OF_MONITORING_MARKER)) {
                    MonitoringController.log.error("Failed to terminate writer");
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
    @Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
        try {
            if (!this.controllerState.get().equals(ControllerState.ENABLED)) {
                return false;
            }
            this.numberOfInserts.incrementAndGet();
            if (this.controllerMode.equals(ControllerMode.REALTIME)) {
                record.setLoggingTimestamp(this.currentTimeNanos());
            }
            if (!this.monitoringLogWriter.newMonitoringRecord(record)) {
                MonitoringController.log.fatal("Error writing the monitoring data. Will terminate monitoring!");
                this.terminate();
                return false;
            }
            return true;
        } catch (final Exception ex) {
            MonitoringController.log.error("Caught an Exception. Will terminate monitoring", ex);
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
        return System.nanoTime() + MonitoringController.offsetA;
    }

    /**    
     *  Loads properties from configuration file. 
     */
    private void initFromProperties(final Properties props, final boolean considerSystemProperties) {
        // load property monitoringLogWriter
        this.monitoringDataWriterClassname = props.getProperty("monitoringDataWriter");
        this.monitoringDataWriterInitString = props.getProperty("monitoringDataWriterInitString");

        String dbDriverClassnameProperty;
        if (considerSystemProperties && (System.getProperty("kieker.monitoring.dbConnectionAddress") != null)) { // we use the present virtual machine parameter value
            dbDriverClassnameProperty = System.getProperty("kieker.monitoring.dbDriverClassname");
        } else { // we use the parameter in the properties file
            dbDriverClassnameProperty = props.getProperty("dbDriverClassname");
        }
        if ((dbDriverClassnameProperty != null) && (dbDriverClassnameProperty.length() != 0)) {
            this.dbDriverClassname = dbDriverClassnameProperty;
        } else {
            MonitoringController.log.info("No dbDriverClassname parameter found"
                    + ". Using default value " + this.dbDriverClassname + ".");
        }

        // load property "dbConnectionAddress"
        String dbConnectionAddressProperty;
        if (considerSystemProperties && (System.getProperty("kieker.monitoring.dbConnectionAddress") != null)) { // we use the present virtual machine parameter value
            dbConnectionAddressProperty = System.getProperty("kieker.monitoring.dbConnectionAddress");
        } else { // we use the parameter in the properties file
            dbConnectionAddressProperty = props.getProperty("dbConnectionAddress");
        }
        if ((dbConnectionAddressProperty != null) && (dbConnectionAddressProperty.length() != 0)) {
            this.dbConnectionAddress = dbConnectionAddressProperty;
        } else {
            MonitoringController.log.warn("No dbConnectionAddress parameter found"
                    + ". Using default value " + this.dbConnectionAddress + ".");
        }

// the filenamePrefix (folder where Kieker.Monitoring stores its data)
// for monitoring data depends on the properties kieker.monitoring.storeInJavaIoTmpdir
// and kieker.monitoring.customStoragePath
// these both parameters may be provided (with higher priority) as java command line parameters as well (example in the properties file)
        String storeInJavaIoTmpdirProperty;
        if (considerSystemProperties && (System.getProperty("kieker.monitoring.storeInJavaIoTmpdir") != null)) { // we use the present virtual machine parameter value
            storeInJavaIoTmpdirProperty = System.getProperty("kieker.monitoring.storeInJavaIoTmpdir");
        } else { // we use the parameter in the properties file
            storeInJavaIoTmpdirProperty = props.getProperty("kieker.monitoring.storeInJavaIoTmpdir");
        }

        if ((storeInJavaIoTmpdirProperty != null) && (storeInJavaIoTmpdirProperty.length() != 0)) {
            if (storeInJavaIoTmpdirProperty.toLowerCase().equals("true") || storeInJavaIoTmpdirProperty.toLowerCase().equals("false")) {
                this.storeInJavaIoTmpdir = storeInJavaIoTmpdirProperty.toLowerCase().equals("true");
            } else {
                MonitoringController.log.warn("Bad value for kieker.monitoring.storeInJavaIoTmpdir (or provided via command line) parameter (" + storeInJavaIoTmpdirProperty + ")"
                        + ". Using default value " + this.storeInJavaIoTmpdir);
            }
        } else {
            MonitoringController.log.warn("No kieker.monitoring.storeInJavaIoTmpdir parameter found"
                    + " (or provided via command line). Using default value '" + this.storeInJavaIoTmpdir + "'.");
        }

        if (this.storeInJavaIoTmpdir) {
            this.filenamePrefix = System.getProperty("java.io.tmpdir");
        } else { // only now we consider kieker.monitoring.customStoragePath
            String customStoragePathProperty;
            if (considerSystemProperties && (System.getProperty("kieker.monitoring.customStoragePath") != null)) { // we use the present virtual machine parameter value
                customStoragePathProperty = System.getProperty("kieker.monitoring.customStoragePath");
            } else { // we use the parameter in the properties file
                customStoragePathProperty = props.getProperty("kieker.monitoring.customStoragePath");
            }

            if ((customStoragePathProperty != null) && (customStoragePathProperty.length() != 0)) {
                this.filenamePrefix = customStoragePathProperty;
            } else {
                MonitoringController.log.warn("No kieker.monitoring.customStoragePath parameter found"
                        + " (or provided via command line). Using default value '" + this.customStoragePath + "'.");
                this.filenamePrefix =
                        this.customStoragePath;
            }
        }

        // load property "dbTableNameProperty"
        String dbTableNameProperty;
        if (considerSystemProperties && (System.getProperty("kieker.monitoring.dbTableName") != null)) { // we use the present virtual machine parameter value
            dbTableNameProperty = System.getProperty("kieker.monitoring.dbTableName");
        } else { // we use the parameter in the properties file
            dbTableNameProperty = props.getProperty("dbTableName");
        }
        if ((dbTableNameProperty != null) && (dbTableNameProperty.length() != 0)) {
            this.dbTableName = dbTableNameProperty;
        } else {
            MonitoringController.log.warn("No dbTableName  parameter found"
                    + ". Using default value " + this.dbTableName + ".");
        }

        // load property "debug"
        String debugProperty;
        if (considerSystemProperties && (System.getProperty("kieker.monitoring.debug") != null)) { // we use the present virtual machine parameter value
            debugProperty = System.getProperty("kieker.monitoring.debug");
        } else { // we use the parameter in the properties file
            debugProperty = props.getProperty("debug");
        }
        if ((debugProperty != null) && (debugProperty.length() != 0)) {
            if (debugProperty.toLowerCase().equals("true") || debugProperty.toLowerCase().equals("false")) {
                if (debugProperty.toLowerCase().equals("true")) {
                    MonitoringController.log.info("Debug mode enabled");
                    this.debugMode = DebugMode.ENABLED;
                } else {
                    MonitoringController.log.info("Debug mode disabled");
                    this.debugMode = DebugMode.DISABLED;
                }
            } else {
                MonitoringController.log.warn("Bad value for debug parameter (" + debugProperty + ")"
                        + ". Using default value " + this.debugMode.isDebugEnabled());
            }
        } else {
            MonitoringController.log.warn("Could not find debug parameter"
                    + ". Using default value " + this.debugMode.isDebugEnabled());
        }

        // load property "setInitialExperimentIdBasedOnLastId"
        final String setInitialExperimentIdBasedOnLastIdProperty = props.getProperty("setInitialExperimentIdBasedOnLastId");
        if ((setInitialExperimentIdBasedOnLastIdProperty != null) && (setInitialExperimentIdBasedOnLastIdProperty.length() != 0)) {
            if (setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("true") || setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("false")) {
                this.setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("true");
            } else {
                MonitoringController.log.warn("Bad value for setInitialExperimentIdBasedOnLastId parameter (" + setInitialExperimentIdBasedOnLastIdProperty + ")"
                        + ". Using default value " + this.setInitialExperimentIdBasedOnLastId);
            }
        } else {
            MonitoringController.log.warn("Could not find setInitialExperimentIdBasedOnLastId parameter"
                    + ". Using default value " + this.setInitialExperimentIdBasedOnLastId);
        }

        // load property "asyncRecordQueueSize"
        String asyncRecordQueueSizeProperty = null;
        if (considerSystemProperties && (System.getProperty("kieker.monitoring.asyncRecordQueueSize") != null)) { // we use the present virtual machine parameter value
            asyncRecordQueueSizeProperty = System.getProperty("kieker.monitoring.asyncRecordQueueSize");
        } else { // we use the parameter in the properties file
            asyncRecordQueueSizeProperty = props.getProperty("asyncRecordQueueSize");
        }
        if ((asyncRecordQueueSizeProperty != null) && (asyncRecordQueueSizeProperty.length() != 0)) {
            int asyncRecordQueueSizeValue = -1;
            try {
                asyncRecordQueueSizeValue = Integer.parseInt(asyncRecordQueueSizeProperty);
            } catch (final NumberFormatException ex) {
            }
            if (asyncRecordQueueSizeValue >= 0) {
                this.asyncRecordQueueSize = asyncRecordQueueSizeValue;
            } else {
                MonitoringController.log.warn("Bad value for asyncRecordQueueSize parameter (" + asyncRecordQueueSizeProperty + ")"
                        + ". Using default value " + this.asyncRecordQueueSize);
            }
        } else {
            MonitoringController.log.warn("Could not find asyncRecordQueueSize parameter"
                    + ". Using default value " + this.asyncRecordQueueSize);
        }

        // load property "asyncBlockOnFullQueue"
        String asyncBlockOnFullQueueProperty = null;
        if (considerSystemProperties && (System.getProperty("kieker.monitoring.asyncBlockOnFullQueue") != null)) { // we use the present virtual machine parameter value
            asyncBlockOnFullQueueProperty = System.getProperty("kieker.monitoring.asyncBlockOnFullQueue");
        } else { // we use the parameter in the properties file
            asyncBlockOnFullQueueProperty = props.getProperty("asyncBlockOnFullQueue");
        }
        if ((asyncBlockOnFullQueueProperty != null) && (asyncBlockOnFullQueueProperty.length() != 0)) {
            this.asyncBlockOnFullQueue = Boolean.parseBoolean(asyncBlockOnFullQueueProperty);
            MonitoringController.log.info("Using asyncBlockOnFullQueue value (" + asyncBlockOnFullQueueProperty + ")"
                    + ". Using default value " + this.asyncBlockOnFullQueue);
        } else {
            MonitoringController.log.warn("Could not find asyncBlockOnFullQueue"
                    + ". Using default value " + this.asyncBlockOnFullQueue);
        }

        final String monitoringEnabledProperty = props.getProperty("monitoringEnabled");
        if ((monitoringEnabledProperty != null) && (monitoringEnabledProperty.length() != 0)) {
            if (monitoringEnabledProperty.toLowerCase().equals("true") || monitoringEnabledProperty.toLowerCase().equals("false")) {
                if (monitoringEnabledProperty.toLowerCase().equals("true")) {
                    this.controllerState.set(ControllerState.ENABLED);
                } else {
                    this.controllerState.set(ControllerState.DISABLED);
                }
            } else {
                MonitoringController.log.warn("Bad value for monitoringEnabled parameter (" + monitoringEnabledProperty + ")"
                        + ". Using default value " + this.controllerState.get().equals(ControllerState.ENABLED));
            }

        } else {
            MonitoringController.log.warn("Could not find monitoringEnabled parameter"
                    + ". Using default value " + this.controllerState.get().equals(ControllerState.ENABLED));
        }

        if (!this.controllerState.get().equals(ControllerState.ENABLED)) {
            MonitoringController.log.info("Monitoring is not enabled");
        }
    }

    /** Returns a human-readable information string about the controller configuration.
     *  @return the information string
     */
    public String getConnectorInfo() {
        final StringBuilder strB = new StringBuilder();

        strB.append("monitoringDataWriter : " + this.monitoringLogWriter.getClass().getCanonicalName());
        strB.append(",");
        strB.append(" monitoringDataWriter config : (below), " + this.monitoringLogWriter.getInfoString());
        strB.append(",");
        strB.append(" version :" + this.getVersion() + ", debug :" + this.debugMode.isDebugEnabled() + ", enabled :" + this.isMonitoringEnabled() + ", terminated :" + this.isMonitoringPermanentlyTerminated() + ", experimentID :" + this.getExperimentId() + ", vmname :" + this.getVmName());

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
    public final void setDebug(final boolean debug) {
        if (debug) {
            this.debugMode = DebugMode.ENABLED;
        } else {
            this.debugMode = DebugMode.DISABLED;
        }
    }
}
