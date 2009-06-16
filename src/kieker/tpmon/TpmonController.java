package kieker.tpmon;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;
import kieker.tpmon.annotations.TpmonInternal;
import kieker.tpmon.writer.asyncDatabase.AsyncDbconnector;
import kieker.tpmon.writer.asyncFsWriter.AsyncFsWriterProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.TpmonController
 * 
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project 
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
 *
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
 * @author Matthias Rohr, Andre van Hoorn, Nils Sommer
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
public class TpmonController {

    private static final Log log = LogFactory.getLog(TpmonController.class);
    public final static String WRITER_SYNCDB = "SyncDB";
    public final static String WRITER_ASYNCDB = "AsyncDB";
    public final static String WRITER_SYNCFS = "SyncFS";
    public final static String WRITER_ASYNCFS = "AsyncFS";
    private String monitoringDataWriterClassname = null;
    private String monitoringDataWriterInitString = null;
    private IMonitoringDataWriter monitoringDataWriter = null;
    private String vmname = "unknown";    // the following configuration values are overwritten by tpmonLTW.properties in tpmonLTW.jar
    private String dbDriverClassname = "com.mysql.jdbc.Driver";
    private String dbConnectionAddress = "jdbc:mysql://HOSTNAME/DATABASENAME?user=DBUSER&password=DBPASS";
    private String dbTableName = "turbomon10";
    //private String buildDate = "unknown (at least 2008-08-08)";
    private boolean debug = false;
    private String filenamePrefix = ""; // e.g. path "/tmp"   
    private boolean storeInJavaIoTmpdir = true;
    private String customStoragePath = "/tmp"; // only used as default if storeInJavaIoTmpdir == false
    // database only configuration configuration values that are overwritten by tpmon.properties included in the tpmon library
    private boolean setInitialExperimentIdBasedOnLastId = false;    // only use the asyncDbconnector in server environments, that do not directly terminate after the executions, or some 
    // values might be not written to the database in case of an system.exit(0)!
    // The place holders are usually much smaller and storage therefore much faster and requires less space.
    //private boolean encodeMethodNames = false;
    // trace sampling:
    // if activated, approximately every n-th (traceSampleingFrequency) trace will be made persistend 
    // this allows to save the overhead and space for storing data.
    // WARNING: Trace sampling should not be used if a session-based evaluation is targeted!
    //          For this, a sessionid based sampleing is required (not implemented yet)
    private boolean traceSampleing = false;
    private int traceSampleingFrequency = 2;
    private TpmonShutdownHook shutdownhook = null;
    //TODO: to be removed and reengineered
    //private static final boolean methodNamesCeWe = true;
    private static TpmonController ctrlInst = null;

    //marks the end of monitoring to the writer threads
    public static final KiekerExecutionRecord END_OF_MONITORING_MARKER = KiekerExecutionRecord.getInstance();

    @TpmonInternal()
    public synchronized static TpmonController getInstance() {
        if (ctrlInst == null) {
            ctrlInst = new TpmonController();
        }
        return TpmonController.ctrlInst;
    }

    public TpmonController() {
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
                String filenameBase = filenamePrefix + "/tpmon-";
                this.monitoringDataWriter = new FileSystemWriter(filenameBase);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_ASYNCFS)) {
                String filenameBase = filenamePrefix + "/tpmon-";
                this.monitoringDataWriter = new AsyncFsWriterProducer(filenameBase);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_SYNCDB)) {
                this.monitoringDataWriter = new Dbconnector(
                        dbDriverClassname, dbConnectionAddress,
                        dbTableName,
                        setInitialExperimentIdBasedOnLastId);
            } else if (this.monitoringDataWriterClassname.equals(WRITER_ASYNCDB)) {
                this.monitoringDataWriter = new AsyncDbconnector(
                        dbDriverClassname, dbConnectionAddress,
                        dbTableName,
                        setInitialExperimentIdBasedOnLastId);
            } else {
                /* try to load the class by name */
                this.monitoringDataWriter = (IMonitoringDataWriter) Class.forName(this.monitoringDataWriterClassname).newInstance();
                this.monitoringDataWriter.init(monitoringDataWriterInitString);
            }
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
    public String getVmname() {
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
    public void setVmname(String newVmname) {
        log.info(">Kieker-Tpmon: The VM has the NEW name " + newVmname +
                " Thread:" + Thread.currentThread().getId());
        this.vmname = newVmname;
    }

    /**
     * See TpmonShutdownHook.registerWorker
     * @param newWorker
     */
    @TpmonInternal()
    public void registerWorker(AbstractWorkerThread newWorker) {
        this.shutdownhook.registerWorker(newWorker);
    }
    //private long lastUniqueIdTime = 0;
    //private int secondaryCounter = 0;
    //TODO: why are these guys public?  -- made private by Nina, and then commented out
    // private long initializationTime = System.currentTimeMillis();
    private AtomicLong numberOfInserts = new AtomicLong(0);
    // private Date startDate = new Date(initializationTime);
    private boolean monitoringEnabled = true;
    // if monitoring terminated, it is not allowed to enable monitoring afterwards
    private boolean monitoringPermanentlyTerminated = false;

    @TpmonInternal()
    public boolean isDebug() {
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
    public boolean isMonitoringEnabled() {
        return monitoringEnabled;
    }
    
    @TpmonInternal()
    public boolean isMonitoringPermanentlyTerminated() {
        return monitoringPermanentlyTerminated;
    }


    private static final int STANDARDEXPERIMENTID = 0;
    // we do not use AtomicInteger since we only rarely 
    // set the value (common case -- getting -- faster now).
    // instead, we decided to provide an "expensive" increment method.
    private int experimentId = STANDARDEXPERIMENTID;

    @TpmonInternal()
    public int getExperimentId() {
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
    public void enableMonitoring() {
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
    public void disableMonitoring() {
        log.info("Disabling monitoring");
        this.monitoringEnabled = false;
    }

    /**
     * Permanently terminates monitoring (e.g., due to a failure).
     * Subsequent tries to enable monitoring will be refused.
     */
    @TpmonInternal()
    public void terminateMonitoring() {
        log.info("Permanently terminating monitoring");
        this.monitoringDataWriter.insertMonitoringDataNow(END_OF_MONITORING_MARKER);
        this.disableMonitoring();
        this.monitoringPermanentlyTerminated = true;
    }


// only used if encodeMethodNames == true
//    private HashMap<String, String> methodNameEncoder = new HashMap<String, String>();
    // lastEncodedMethodName provides some kind of distributed system unique offset, numbers are increased by 1 for
    // each monitoring point after that
    // (The following might produce in very very few cases a colision in a large DISTRIBUTED system with a large number
    // of instrumented methods. For save usage in a critical distributed system, where the monitoring data is extremely critical,
    // only file system storage should be used and component and methodnames should be decoded locally to avoid this problem (or disable encodeMethodNames).)    
//    private int lastEncodedMethodName = Math.abs(getVmname().hashCode() % 10000);
    @TpmonInternal()
    public boolean insertMonitoringDataNow(KiekerExecutionRecord execData) {
        execData.experimentId = this.experimentId;
        execData.vmName = this.vmname;

        if (!this.monitoringEnabled) {
            return false;
        }

        if (traceSampleing) { // approximately (!) every traceSampleingFrequency-th trace will be monitored
            if (!(execData.traceId % traceSampleingFrequency == 0)) {
                return true;
            }
        }
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
        numberOfInserts.incrementAndGet();
        // now it fails fast, it disables monitoring when a queue once is full
        if (!this.monitoringDataWriter.insertMonitoringDataNow(execData)) {
            log.fatal("Error writing the monitoring data. Will terminate monitoring!");
            this.terminateMonitoring();
            return false;
        }

        return true;
    }

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
    @TpmonInternal()
    private void storeEncodedName(String component, String newMethodname, String encodedName) {
        // log.info("Kieker-Tpmon: Encoding "+component+""+newMethodname+" by "+encodedName);
        String opname = component + newMethodname;
        numberOfInserts.incrementAndGet();
        KiekerExecutionRecord execData = KiekerExecutionRecord.getInstance();
        execData.componentName = opname;
        execData.opname = encodedName;
        execData.traceId = -5;
        execData.tin = -5;
        execData.tout = -5;
        execData.eoi = -5;
        execData.ess = -5;
        // NOTE: experimentId and vmname will be set inside insertMonitoringDataNow(.)
        this.monitoringDataWriter.insertMonitoringDataNow(execData);
    }

    /**
     * Internal method to convert the method names into a proper format  
     * @param methodname
     * @return methodname without a double componentname
     */
    @TpmonInternal()
    private String formatMethodName(String methodname) {
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

        int indexOfOpenBrace = methodname.lastIndexOf("(");
        int indexBeginOfMethodname = methodname.lastIndexOf(".", indexOfOpenBrace);
        if (indexBeginOfMethodname == -1) {
            return methodname;
        } else {
            return methodname.substring(indexBeginOfMethodname, methodname.length()).replaceAll(" ", "");
        }

    }
    //private long seed = 0;
    //private double d3 = 0.3d;
    /**
     * This method is used by the aspects to get the time stamps. It uses nano seconds as precision.    
     * The method is synchronized in order to reduce the risk of identical time stamps. 
     * 
     * In contrast to System.nanoTime(), it gives the nano seconds between the current time and midnight, January 1, 1970 UTC.
     * (The value returned by System.nanoTime() only represents nanoseconds since *some* fixed but arbitrary time.)
     */
    private long offsetA = System.currentTimeMillis() * 1000000 - System.nanoTime();

    @TpmonInternal()
    public long getTime() {
        return System.nanoTime() + offsetA;
    }
    private AtomicLong lastThreadId = new AtomicLong(0);
    private ThreadLocal<Long> threadLocalTraceId = new ThreadLocal<Long>();

    /**
     * This method returns a thread-local traceid which is globally
     * unique and stored it local for the thread.
     * The thread is responsible for invalidating the stored curTraceId using 
     * the method unsetThreadLocalTraceId()!
     */
    @TpmonInternal()
    public long getAndStoreUniqueThreadLocalTraceId() {
        long id = lastThreadId.incrementAndGet();
        this.threadLocalTraceId.set(id);
        return id;
    }

    /**
     * This method stores a thread-local curTraceId.
     * The thread is responsible for invalidating the stored curTraceId using 
     * the method unsetThreadLocalTraceId()!
     */
    @TpmonInternal()
    public void storeThreadLocalTraceId(long traceId) {
        this.threadLocalTraceId.set(traceId);
    }

    /**
     * This method returns the thread-local traceid previously
     * registered using the method registerTraceId(curTraceId).
     * 
     * @return the traceid. -1 if no curTraceId has been registered
     *         for this thread.
     */
    @TpmonInternal()
    public long recallThreadLocalTraceId() {
        //log.info("Recalling curTraceId");
        Long traceIdObj = this.threadLocalTraceId.get();
        if (traceIdObj == null) {
            //log.info("curTraceId == null");
            return -1;
        }
//log.info("curTraceId =" + traceIdObj);
        return traceIdObj;
    }

    /**
     * This method unsets a previously registered traceid. 
     */
    @TpmonInternal()
    public void unsetThreadLocalTraceId() {
        this.threadLocalTraceId.remove();
    }
    private ThreadLocal<String> threadLocalSessionId = new ThreadLocal<String>();

    /**
     * Used by the spring aspect to explicitly register a sessionid that is to be collected within
     * a servlet method (that knows the request object).
     * The thread is responsible for invalidating the stored curTraceId using 
     * the method unsetThreadLocalSessionId()!
     */
    @TpmonInternal()
    public void storeThreadLocalSessionId(String sessionId) {
        this.threadLocalSessionId.set(sessionId);
    }

    /**
     * This method returns the thread-local traceid previously
     * registered using the method registerTraceId(curTraceId).
     * 
     * @return the sessionid. null if no session registered.
     */
    @TpmonInternal()
    public String recallThreadLocalSessionId() {
        return this.threadLocalSessionId.get();
    }

    /**
     * This method unsets a previously registered sessionid. 
     */
    @TpmonInternal()
    public void unsetThreadLocalSessionId() {
        this.threadLocalSessionId.remove();
    }
    private ThreadLocal<Integer> threadLocalEoi = new ThreadLocal<Integer>();

    /**
     * Used by the spring aspect to explicitly register an curEoi.
     * The thread is responsible for invalidating the stored curTraceId using 
     * the method unsetThreadLocalEOI()!
     */
    @TpmonInternal()
    public void storeThreadLocalEOI(int eoi) {
        this.threadLocalEoi.set(eoi);
    }

    /** 
     * Since this method accesses a ThreadLocal variable,
     * it is not (necessary to be) thread-safe.
     */
    @TpmonInternal()
    public int incrementAndRecallThreadLocalEOI() {
        Integer curEoi = this.threadLocalEoi.get();
        if (curEoi == null) {
            log.fatal("eoi has not been registered before");
            return -1;
        }
        int newEoi = curEoi + 1;
        this.threadLocalEoi.set(newEoi);
        return newEoi;
    }

    /**
     * This method returns the thread-local curEoi previously
     * registered using the method registerTraceId(curTraceId).
     * 
     * @return the sessionid. -1 if no curEoi registered.
     */
    @TpmonInternal()
    public int recallThreadLocalEOI() {
        Integer curEoi = this.threadLocalEoi.get();
        if (curEoi == null) {
            log.fatal("eoi has not been registered before");
            return -1;
        }
        return curEoi;
    }

    /**
     * This method unsets a previously registered traceid. 
     */
    @TpmonInternal()
    public void unsetThreadLocalEOI() {
        this.threadLocalEoi.remove();
    }
    private ThreadLocal<Integer> threadLocalEss = new ThreadLocal<Integer>();

    /**
     * Used by the spring aspect to explicitly register a sessionid that is to be collected within
     * a servlet method (that knows the request object).
     * The thread is responsible for invalidating the stored curTraceId using 
     * the method unsetThreadLocalSessionId()!
     */
    @TpmonInternal()
    public void storeThreadLocalESS(int ess) {
        this.threadLocalEss.set(ess);
    }

    /** 
     * Since this method accesses a ThreadLocal variable,
     *  it is not (necessary to be) thread-safe.
     */
    @TpmonInternal()
    public int recallAndIncrementThreadLocalESS() {
        Integer curEss = this.threadLocalEss.get();
        if (curEss == null) {
            log.fatal("ess has not been registered before");
            return -1;
        }
        this.threadLocalEss.set(curEss + 1);
        return curEss;
    }

    /**
     * This method returns the thread-local curEss previously
     * registered using the method registerTraceId(curTraceId).
     * 
     * @return the sessionid. -1 if no curEss registered.
     */
    @TpmonInternal()
    public int recallThreadLocalESS() {
        Integer ess = this.threadLocalEss.get();
        if (ess == null) {
            log.fatal("ess has not been registered before");
            return -1;
        }
        return ess;
    }

    /**
     * This method unsets a previously registered curEss. 
     */
    @TpmonInternal()
    public void unsetThreadLocalESS() {
        this.threadLocalEss.remove();
    }

    @TpmonInternal()
    public void shutdown() {
        log.info("Tpmon: shutting down");
    }

    /**    
     * Loads configuration values from the file
     * tpmonLTW.jar/META-INF/dbconnector.properties.
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
            prop.load(is);
        } catch (Exception ex) {
            log.error("Error loading tpmon.configuration", ex);
            formatAndOutputError("Could not open tpmon properties : " + configurationFile +
                    ". Using default value " + dbConnectionAddress + ". Message :" + ex.getMessage(), true, false);
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
            formatAndOutputError("No dbConnectionAddress parameter found in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + dbConnectionAddress + ".", true, false);
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
                formatAndOutputError("Bad value for tpmon.storeInJavaIoTmpdir (or provided via command line) parameter (" + storeInJavaIoTmpdirProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + storeInJavaIoTmpdir, true, false);
            }

        } else {
            formatAndOutputError("No tpmon.storeInJavaIoTmpdir parameter found in tpmonLTW.jar/" + configurationFile +
                    " (or provided via command line). Using default value '" + storeInJavaIoTmpdir + "'.", true, false);
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
                formatAndOutputError("No tpmon.customStoragePath parameter found in tpmonLTW.jar/" + configurationFile +
                        " (or provided via command line). Using default value '" + customStoragePath + "'.", true, false);
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
            formatAndOutputError("No dbTableName  parameter found in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + dbTableName + ".", true, false);
        }

        // load property "debug"
        String debugProperty = prop.getProperty("debug");
        if (debugProperty != null && debugProperty.length() != 0) {
            if (debugProperty.toLowerCase().equals("true") || debugProperty.toLowerCase().equals("false")) {
                debug = debugProperty.toLowerCase().equals("true");
            } else {
                formatAndOutputError("Bad value for debug parameter (" + debugProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + debug, true, false);
            }

        } else {
            formatAndOutputError("Could not find debug parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + debug, true, false);
        }

        // load property "setInitialExperimentIdBasedOnLastId"
        String setInitialExperimentIdBasedOnLastIdProperty = prop.getProperty("setInitialExperimentIdBasedOnLastId");
        if (setInitialExperimentIdBasedOnLastIdProperty != null && setInitialExperimentIdBasedOnLastIdProperty.length() != 0) {
            if (setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("true") || setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("false")) {
                setInitialExperimentIdBasedOnLastId = setInitialExperimentIdBasedOnLastIdProperty.toLowerCase().equals("true");
            } else {
                formatAndOutputError("Bad value for setInitialExperimentIdBasedOnLastId parameter (" + setInitialExperimentIdBasedOnLastIdProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + setInitialExperimentIdBasedOnLastId, true, false);
            }

        } else {
            formatAndOutputError("Could not find setInitialExperimentIdBasedOnLastId parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + setInitialExperimentIdBasedOnLastId, true, false);
        }

        String monitoringEnabledProperty = prop.getProperty("monitoringEnabled");
        if (monitoringEnabledProperty != null && monitoringEnabledProperty.length() != 0) {
            if (monitoringEnabledProperty.toLowerCase().equals("true") || monitoringEnabledProperty.toLowerCase().equals("false")) {
                monitoringEnabled = monitoringEnabledProperty.toLowerCase().equals("true");
            //  log.info("monitoringEnabled true");
            } else {
                formatAndOutputError("Bad value for monitoringEnabled parameter (" + monitoringEnabledProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + monitoringEnabled, true, false);
            //    log.info("monitoringEnabled bad value");
            }

        } else {
            formatAndOutputError("Could not find monitoringEnabled parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + monitoringEnabled, true, false);
        //  log.info("monitoringEnabled missing param");
        }

        //log.info("monitoringEnabled "+monitoringEnabled);
        if (monitoringEnabled == false) {
            log.info(">Kieker-Tpmon: Notice, monitoring is deactived (monitoringEnables=false in dbconnector.properties within tpmonLTW.jar)");
        }

        if (debug) {
            log.info(getConnectorInfo());
        }
    }

    /**
     * TODO: this strange method should be removed (my (Andre) opinion)!
     * 
     * @param errorMessage
     * @param onlyWarning
     * @param reportTime
     */
    @TpmonInternal()
    public void formatAndOutputError(String errorMessage, boolean onlyWarning, boolean reportTime) {
        StringBuffer errorReport = new StringBuffer(">Kieker-Tpmon:  ");
        if (onlyWarning) {
            errorReport.append("Warning ");
        } else {
            errorReport.append("Error   ");
        }
        if (reportTime) {
            errorReport.append(getDateString());
        }
        errorReport.append(" : " + errorMessage);
        log.error(errorReport.toString());
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

    /**
     * Use a RemoteCallMetaData object to transport tracing data together 
     * with a remote method call. This is required to allows  tracing in
     * distributed systems.
     * 
     * @param threadid
     */
    @TpmonInternal()
    public RemoteCallMetaData getRemoteCallMetaData() {
        long curTraceId = this.recallThreadLocalTraceId();
        if (curTraceId == -1) { // no curTraceId was registered
            log.info("Tpmon: warning traceid was null");
            curTraceId = this.getAndStoreUniqueThreadLocalTraceId();
        }

        int curEoi = this.recallThreadLocalEOI();
        if (curEoi == -1) {
            log.info("Tpmon: warning eoi == -1");
            curEoi = 0;
            this.storeThreadLocalEOI(0);
        }

        int curEss = this.recallThreadLocalESS();
        if (curEss == -1) {
            log.info("Tpmon: warning ess == -1");
            curEss = 0;
            this.storeThreadLocalESS(0);
        }

        return new RemoteCallMetaData(curTraceId, curEoi, curEss);
    }

    /**
     * This method has to be called to register an incomming remote call
     * @param rcmd
     * @param threadid
     */
    @TpmonInternal()
    public void registerRemoteCallMetaData(RemoteCallMetaData rcmd) {
        if (rcmd == null) {
            log.info("Tpmon: RCMD == null");
            this.getAndStoreUniqueThreadLocalTraceId();
            this.storeThreadLocalEOI(0);
            this.storeThreadLocalESS(0);
        } else {
            this.storeThreadLocalTraceId(rcmd.traceid);
            this.storeThreadLocalEOI(rcmd.eoi);
            this.storeThreadLocalESS(rcmd.ess);
        }
    }

    @TpmonInternal()
    public String getVersion() {
        return TpmonVersion.getVERSION();
    }

    @TpmonInternal()
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
