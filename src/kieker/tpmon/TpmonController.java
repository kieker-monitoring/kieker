package kieker.tpmon;

/**
 * kieker.tpmon.FileSystemWriter
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
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kieker.tpmon.annotations.TpmonInternal;
import kieker.tpmon.asyncDbconnector.AsyncDbconnector;
import kieker.tpmon.asyncDbconnector.Worker;
import kieker.tpmon.asyncFsWriter.AsyncFsWriterProducer;

public class TpmonController {

    private static final Log log = LogFactory.getLog(TpmonController.class);
    private IMonitoringDataWriter monitoringDataWriter = null;
    private String vmname = "unknown";

    // the following configuration values are overwritten by tpmonLTW.properties in tpmonLTW.jar
    private String dbConnectionAddress = "jdbc:mysql://jupiter.informatik.uni-oldenburg.de/0610turbomon?user=root&password=xxxxxx";
    private String dbTableName = "turbomon10";
    //private String buildDate = "unknown (at least 2008-08-08)";
    private boolean debug = false;
    public boolean storeInDatabase = false;
    public String filenamePrefix = ""; // e.g. path "/tmp"   
    public boolean storeInJavaIoTmpdir = true;
    public String customStoragePath = "/tmp"; // only used as default if storeInJavaIoTmpdir == false
    // database only configuration configuration values that are overwritten by tpmon.properties included in the tpmon library
    private boolean setInitialExperimentIdBasedOnLastId = false;

    // only use the asyncDbconnector in server environments, that do not directly terminate after the executions, or some 
    // values might be not written to the database in case of an system.exit(0)!
    private boolean asyncDbconnector = false;
    private boolean asyncFsWriter = true;

    // Encoding method and component names stores just placeholders for the component and method names.
    // The place holders are usually much smaller and storage therefore much faster and requires less space.
    private boolean encodeMethodNames = false;
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
        //System.out.println("VMNAME:"+vmname);
        log.info(">Kieker-Tpmon: Virtual Machine start time " +
                ManagementFactory.getRuntimeMXBean().getStartTime());

        this.shutdownhook = new TpmonShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownhook);

        loadPropertiesFile();

        if (this.storeInDatabase) {
            if (asyncDbconnector) {
                AsyncDbconnector producer = new AsyncDbconnector(dbConnectionAddress, dbTableName,
                        setInitialExperimentIdBasedOnLastId);
                Vector<Worker> worker = producer.getWorkers();
                for (Worker w : worker) {
                    this.registerWorker(w);
                }
                this.monitoringDataWriter = producer;
            } else {
                this.monitoringDataWriter = new Dbconnector(dbConnectionAddress, dbTableName,
                        setInitialExperimentIdBasedOnLastId);
            }
            log.info(">Kieker-Tpmon: Initialization completed. Storing " +
                    "monitoring data in the database.");

        } else {
            String filenameBase = new String(filenamePrefix + "/tpmon-");
            if (asyncFsWriter) {
                AsyncFsWriterProducer producer = new AsyncFsWriterProducer(filenameBase);
                Vector<Worker> worker = producer.getWorkers();
                for (Worker w : worker) {
                    this.registerWorker(w);
                }
                this.monitoringDataWriter = producer;
            } else {
                this.monitoringDataWriter = new FileSystemWriter(filenameBase);
            }
            log.info(">Kieker-Tpmon: Initialization completed. Storing " +
                    "monitoring data in the folder " + filenamePrefix);
        }
    }

    @TpmonInternal()
    public String getFilenamePrefix() {
        return filenamePrefix;
    }

    /**
     * The vmname which defaults to the hostname, and may be set by tpmon-control-servlet.
     * The vmname will be part of the monitoring data and allows to assing observations
     * in cases where the software system is deployed on more than one host.
     * 
     * When you want to distinguish multiple Virtual Machines on one host,
     * you have to set the vmname manually (e.g., via the tpmon-control-servlet, 
     * or by directly implementing a call to TpmonController.setVmname(...).
     * 
     * @return
     */
    @TpmonInternal()
    public String getVmname() {
        return this.vmname;
    }

    /**
     * 
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
    public void registerWorker(Worker newWorker) {
        this.shutdownhook.registerWorker(newWorker);
    }
    private long lastUniqueIdTime = 0;
    private int secondaryCounter = 0;
    //TODO: why are these guys public?
    public long initializationTime = System.currentTimeMillis();
    public AtomicLong numberOfInserts = new AtomicLong(0);
    public Date startDate = new Date(initializationTime);
    private boolean monitoringEnabled = true;

    @TpmonInternal()
    public boolean isDebug() {
        return debug;
    }

    @TpmonInternal()
    public boolean isStoreInDatabase() {
        return storeInDatabase;
    }

    @TpmonInternal()
    public boolean isMonitoringEnabled() {
        return monitoringEnabled;
    }
    private final int STANDARDEXPERIMENTID = 0;
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
        this.monitoringEnabled = true;
    }

    /**
     * Disables to store monitoring data
     */
    @TpmonInternal()
    public void disableMonitoring() {
        log.info("Disabling monitoring");
        this.monitoringEnabled = false;
    }

    @TpmonInternal()
    public boolean insertMonitoringDataNow(String component, String methodSig, String requestID, long tin, long tout) {
        return this.insertMonitoringDataNow(component, methodSig, "nosession", requestID, tin, tout, -1, -1);
    }

    @TpmonInternal()
    public boolean insertMonitoringDataNow(String component, String methodSig, String requestID, long tin, long tout, int executionOrderIndex, int executionStackSize) {
        return this.insertMonitoringDataNow(component, methodSig, "nosession", requestID, tin, tout, executionOrderIndex, executionStackSize);
    }

    @TpmonInternal()
    public boolean insertMonitoringDataNow(String component, String methodSig, String sessionID, String requestID, long tin, long tout) {
        return this.insertMonitoringDataNow(component, methodSig, sessionID, requestID, tin, tout, -1, -1);
    }
    // only used if encodeMethodNames == true
    private HashMap<String, String> methodNameEncoder = new HashMap<String, String>();
    // lastEncodedMethodName provides some kind of distributed system unique offset, numbers are increased by 1 for
    // each monitoring point after that
    // (The following might produce in very very few cases a colision in a large DISTRIBUTED system with a large number
    // of instrumented methods. For save usage in a critical distributed system, where the monitoring data is extremely critical,
    // only file system storage should be used and component and methodnames should be decoded locally to avoid this problem (or disable encodeMethodNames).)    
    private int lastEncodedMethodName = Math.abs(getVmname().hashCode() % 10000);

    @TpmonInternal()
    public boolean insertMonitoringDataNow(String componentname, String methodSig, String sessionID, String requestID, long tin, long tout, int executionOrderIndex, int executionStackSize) {
        if (traceSampleing) { // approximately (!) every traceSampleingFrequency-th trace will be monitored
            if (!(requestID.hashCode() % traceSampleingFrequency == 0)) {
                return true;
            }
        }

        if (!this.monitoringEnabled) {
            return false;
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


//        if (!methodNamesCeWe) {

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

        //TODO: Encapsulation into an interface of all those writers and polymorphy might make the two evaluations of the boolean unneccesary
        numberOfInserts.incrementAndGet();
        String opname = componentname + "." + methodSig;
        if (!this.monitoringDataWriter.insertMonitoringDataNow(this.experimentId,
                this.vmname, opname, sessionID, requestID, tin, tout,
                executionOrderIndex, executionStackSize)) {
            log.fatal("Error writing the monitoring data. Will disable monitoring!");
            this.monitoringEnabled = false;
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
     * grep ",-5,-5,-5,-5$" will identify the lines that contain encoding information in monitoring files.
     *       
     * 
     */
    private void storeEncodedName(String component, String newMethodname, String encodedName) {
        // log.info("Kieker-Tpmon: Encoding "+component+""+newMethodname+" by "+encodedName);
        String opname = component + newMethodname;
        numberOfInserts.incrementAndGet();
        this.monitoringDataWriter.insertMonitoringDataNow(this.experimentId, this.vmname, opname, encodedName, "", -5, -5, -5, -5);
    }

    /**
     * Internal method to convert the method names into a proper format  
     * @param methodname
     * @return methodname without a double componentname
     */
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
            return new String("" + methodname.subSequence(indexBeginOfMethodname, methodname.length())).replaceAll(" ", "");
        }
    }
    private long seed = 0;
    private double d3 = 0.3d;
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

    /**
     * Returns a unique string based on the current system
     * time (in milli seconds), threadId, and a counter. 
     * The string follows the pattern "currentTime-threadId-counter". 
     * The counter indicates the nummer of former requests within the same milli
     * second (it is usually 0). 
     * 
     * The string will be only unique for this java virtual
     * machine instance! 
     *
     * The method is thread-save.
     *
     * */
    @TpmonInternal()
    public synchronized String getUniqueIdentifierForThread(long threadId) {
        long currentTime = System.currentTimeMillis();
        String uniqueIdentifier;
        if (currentTime != lastUniqueIdTime) {
            uniqueIdentifier = new String(currentTime + "-" + threadId + "-0");
            secondaryCounter = 0;
            lastUniqueIdTime = currentTime;
        } else {
            secondaryCounter++;
            uniqueIdentifier = new String(currentTime + "-" + threadId + "-" + secondaryCounter);
        }
        return uniqueIdentifier;
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
        Properties prop = new Properties();

        try {
            if (System.getProperty("tpmon.configuration") != null) { // we use the present virtual machine parameter value
                configurationFile = System.getProperty("tpmon.configuration");
                prop.load(new FileInputStream(configurationFile));
                log.info("Tpmon: Loading properties JVM-specified path '" + configurationFile + "'");
            } else {
                log.info("Tpmon: Loading properties from tpmon library jar/" + configurationFile);
                log.info("You can specify an alternative properties file using the property 'tpmon.configuration'");
                prop.load(TpmonController.class.getClassLoader().getResourceAsStream(configurationFile));
            }
        } catch (Exception ex) {
            log.error("Error loading tpmon.configuration", ex);
            formatAndOutputError("Could not open tpmon properties : " + configurationFile +
                    ". Using default value " + dbConnectionAddress + ". Message :" + ex.getMessage(), true, false);
        }


        // load property "dbConnectionAddress"
        String dbConnectionAddressProperty = prop.getProperty("dbConnectionAddress");
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
                filenamePrefix = customStoragePath;
            }
        }








        // load property "dbTableNameProperty"
        String dbTableNameProperty = prop.getProperty("dbTableName");
        if (dbTableNameProperty != null && dbTableNameProperty.length() != 0) {
            dbTableName = dbTableNameProperty;
        } else {
            formatAndOutputError("No dbTableName  parameter found in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + dbTableName + ".", true, false);
        }

        // ANDRE: buildDate obsolete due to "self-speaking" version name
        // load property "buildDate"
        //String buildDateProperty = prop.getProperty("buildDate");
        //if (buildDateProperty != null && buildDate.length() != 0) {
        //    buildDate = buildDateProperty;
        //} else {
        //    formatAndOutputError("No buildData parameter found in tpmonLTW.jar/" + configurationFile +
        //            ". Using default value " + buildDate + ".", true, false);
        //}

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

        // load property "storeInDatabase"
        String storeInDatabaseProperty = prop.getProperty("storeInDatabase");
        if (storeInDatabaseProperty != null && storeInDatabaseProperty.length() != 0) {
            if (storeInDatabaseProperty.toLowerCase().equals("true") || storeInDatabaseProperty.toLowerCase().equals("false")) {
                storeInDatabase = storeInDatabaseProperty.toLowerCase().equals("true");
            } else {
                formatAndOutputError("Bad value for debug parameter (" + debugProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + debug, true, false);
            }
        } else {
            formatAndOutputError("Could not find storeInDatabase parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + storeInDatabase, true, false);
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

        // load property "asyncDbconnector"
        String asyncDbconnectorProperty = prop.getProperty("useAsyncDbconnector");
        if (asyncDbconnectorProperty != null && asyncDbconnectorProperty.length() != 0) {
            if (asyncDbconnectorProperty.toLowerCase().equals("true") || asyncDbconnectorProperty.toLowerCase().equals("false")) {
                asyncDbconnector = asyncDbconnectorProperty.toLowerCase().equals("true");
            } else {
                formatAndOutputError("Bad value for useAsyncDbconnector parameter (" + asyncDbconnectorProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + asyncDbconnector, true, false);
            }
        } else {
            formatAndOutputError("Could not find useAsyncDbconnector parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + asyncDbconnector, true, false);
        }

        // load property "asyncFsWriter"
        String asyncFsWriterProperty = prop.getProperty("asyncFsWriter");
        if (asyncFsWriterProperty != null && asyncFsWriterProperty.length() != 0) {
            if (asyncFsWriterProperty.toLowerCase().equals("true") || asyncFsWriterProperty.toLowerCase().equals("false")) {
                asyncFsWriter = asyncFsWriterProperty.toLowerCase().equals("true");
            //  log.info("Async fs writer activated");
            } else {
                formatAndOutputError("Bad value for asyncFsWriter parameter (" + asyncFsWriterProperty + ") in tpmonLTW.jar/" + configurationFile +
                        ". Using default value " + asyncFsWriter, true, false);
            }
        } else {
            formatAndOutputError("Could not find asyncFsWriter parameter in tpmonLTW.jar/" + configurationFile +
                    ". Using default value " + asyncFsWriter, true, false);
        }
        // log.info("Async fs writer = "+asyncFsWriter);

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
        errorReport.append(" :" + errorMessage);
        log.error("" + errorReport);
    }

    @TpmonInternal()
    public String getConnectorInfo() {
        //only show the password if debug is on
        String dbConnectionAddress2 = dbConnectionAddress;
        if (!debug) {
            if (dbConnectionAddress.toLowerCase().contains("password")) {
                int posPassw = dbConnectionAddress.toLowerCase().lastIndexOf("password");
                dbConnectionAddress2 = new String(dbConnectionAddress.substring(0, posPassw) + "-PASSWORD-HIDDEN");
            }
        }

        if (storeInDatabase) {
            return new String("Storage mode : Tpmon stores in the database: dbConnectionAddress :" + dbConnectionAddress2 + ", version :" + this.getVersion() + ", dbTableName :" + dbTableName + ", debug :" + debug + ", enabled :" + isMonitoringEnabled() + ", experimentID :" + getExperimentId() + ", vmname :" + getVmname());
        } else {
            return new String("Storage mode : Tpmon stores in the filesystem, Version :" + this.getVersion() + ", debug :" + debug + ", enabled :" + isMonitoringEnabled() + ", experimentID :" + getExperimentId() + ", Monitoring data directory:" + filenamePrefix);
        }
    }

    @TpmonInternal()
    public String getDateString() {
        return java.util.Calendar.getInstance().getTime().toString();
    }
    // only used by the *Remote* aspect or the spring aspectj aspect. The other aspects have own 
    // more protected HashMaps.
    public Map<Long, String> sessionThreadMatcher = new ConcurrentHashMap<Long, String>();
    public Map<Long, String> requestThreadMatcher = new ConcurrentHashMap<Long, String>();

    /**
     * This method and setTraceId are only to support
     * traceing over remote connections (experimental prototype 
     * -- works but is dirty, since it is not encapsulated into aspects jet)...
     * @param threadid
     * @return
     */
    @TpmonInternal()
    public String getTraceId(Long threadid) {
        //log.info("TpmonController: getTraceId("+threadid+") ="+requestThreadMatcher.get(threadid));
        return requestThreadMatcher.get(threadid);
    }
//    /**
//     * This method and setTraceId are only used by to support
//     * traceing over remote connections (experimental prototype 
//     * -- works but is dirty, since it is not encapsulated into aspects jet)...
//     * @param threadid
//     * @return
//     */    
//    private static void setTraceId(Long threadid, String traceid) {
//        //log.info("TpmonController: setTraceId("+threadid+","+threadid+")");
//        requestThreadMatcher.put(threadid, traceid);
//    }
    /**
     * Only to enable remote tracing 
     * executionOrderIndexMatcher Contains for each traceid the last execution-order-identifier that was given to the execution
     * executionStackSizeMatcher contains for each traceid the next stack size to be logged
     */
    public Map<String, Integer> executionOrderIndexMatcher = Collections.synchronizedMap(new HashMap<String, Integer>());
    public Map<String, Integer> executionStackSizeMatcher = Collections.synchronizedMap(new HashMap<String, Integer>());

    /**
     * Use a RemoteCallMetaData object to transport tracing data together 
     * with a remote method call. This is required to allows  tracing in
     * distributed systems.
     * 
     * @param threadid
     * @return
     */
    @TpmonInternal()
    public RemoteCallMetaData getRemoteCallMetaData() {
        Long threadid = Thread.currentThread().getId();
        String traceid = requestThreadMatcher.get(threadid);
        if (traceid == null) {
            log.info("Tpmon: warning traceid was null");
            traceid = getUniqueIdentifierForThread(threadid);
            requestThreadMatcher.put(threadid, traceid);
        }

        Integer eoi = executionOrderIndexMatcher.get(traceid);
        if (eoi == null) {
            log.info("Tpmon: warning eoi == null");
            eoi = 0;
            executionOrderIndexMatcher.put(traceid, eoi);
        }
        Integer ess = executionStackSizeMatcher.get(traceid);
        if (ess == null) {
            log.info("Tpmon: warning ess == null");
            ess = 0;
            executionStackSizeMatcher.put(traceid, ess);
        }
        return new RemoteCallMetaData(traceid, eoi, ess);
    }

    /**
     * Used by the spring aspect to explicitly register a sessionid that is to be collected within
     * a servlet method (that knows the request object).
     */
    @TpmonInternal()
    public void registerSessionIdentifier(String sessionid, long threadid) {
        if (this.monitoringEnabled) {
            sessionThreadMatcher.put(threadid, sessionid);
        }
    }

    @TpmonInternal()
    public String getSessionIdentifier(long threadid) {
        String sessionid = sessionThreadMatcher.get(threadid);
        if (sessionid == null) {
            return "unknown";
        }
        return sessionid;
    }

    /**
     * This method has to be called to register a incomming remote call
     * @param rcmd
     * @param threadid
     */
    @TpmonInternal()
    public void registerRemoteCallMetaData(RemoteCallMetaData rcmd) {

        Long threadid = Thread.currentThread().getId();
        if (rcmd == null) {
            log.info("Tpmon: RCMD == null");
            String traceid = getUniqueIdentifierForThread(threadid);
            requestThreadMatcher.put(threadid, traceid);
            executionOrderIndexMatcher.put(traceid, 0);
            executionStackSizeMatcher.put(traceid, 0);
        } else {
            String traceid = rcmd.getTraceid();
            requestThreadMatcher.put(threadid, traceid);
            executionOrderIndexMatcher.put(traceid, rcmd.getEoi());
            executionStackSizeMatcher.put(traceid, rcmd.getEss());
        }

    }

    /**
     * After a remote call comes back via an return, the new executionOrderIndex
     * must be registered in the caller deplopyment context using this method.
     * @param eoi
     */
    @TpmonInternal()
    public void registerEoiReceivedFromRemoteContext(int eoi) {
        String traceid = requestThreadMatcher.get(Thread.currentThread().getId());
        executionOrderIndexMatcher.put(traceid, eoi);
    }

    @TpmonInternal()
    public int getEoi() {
        String traceid = requestThreadMatcher.get(Thread.currentThread().getId());
        return executionOrderIndexMatcher.get(traceid);
    }

    @TpmonInternal()
    public int getEoi(String traceid) {
        if (traceid == null) {
            return -2;
        }
        Integer eoi = executionOrderIndexMatcher.get(traceid);
        if (eoi == null) {
            return -3;
        }
        return eoi;
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
