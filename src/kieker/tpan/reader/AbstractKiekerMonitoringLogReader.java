/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.tpan.reader;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.RecordConsumerExecutionException;

import kieker.common.record.AbstractMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractKiekerMonitoringLogReader implements IKiekerMonitoringLogReader {

    private static final Log log = LogFactory.getLog(AbstractKiekerMonitoringLogReader.class);
    private final HashMap<String, String> map = new HashMap<String, String>();
    /** class name x class object */
    protected Map<String, Class<? extends AbstractMonitoringRecord>> recordTypeMap = Collections.synchronizedMap(new HashMap<String, Class<? extends AbstractMonitoringRecord>>());
    /** Contains all consumers which consume records of any type */
    private final Collection<IKiekerRecordConsumer> subscribedToAllList =
            new Vector<IKiekerRecordConsumer>();
    /** Contains mapping of record types to subscribed consumers */
    private final HashMap<String, Collection<IKiekerRecordConsumer>> subscribedToTypeMap =
            new HashMap<String, Collection<IKiekerRecordConsumer>>();

    /** Returns the value for the initialization property @a propName or the
     *  the passed default value @a default if no value for this property
     *  exists. */
    protected final String getInitProperty(String propName, String defaultVal) {
        if (!this.initStringProcessed) {
            log.error("InitString not yet processed. " +
                    " Call method initVarsFromInitString(..) first.");
            return null;
        }

        String retVal = this.map.get(propName);
        if (retVal == null) {
            retVal = defaultVal;
        }

        return retVal;
    }

    /** Returns the value for the initialization property @a propName or null
     *  if no value for this property exists. */
    protected final String getInitProperty(String propName) {
        return this.getInitProperty(propName, null);
    }
    private boolean initStringProcessed = false;

    /**
     * Parses the initialization string @a initString for this component.
     * The initilization string consists of key/value pairs.
     * After this method is executed, the parameter values can be retrieved
     * using the method getInitProperty(..).
     */
    protected final void initVarsFromInitString(String initString) throws IllegalArgumentException {
        if (initString == null || initString.length() == 0) {
            initStringProcessed = true;
            return;
        }

        try {
            StringTokenizer keyValListTokens = new StringTokenizer(initString, "|");
            while (keyValListTokens.hasMoreTokens()) {
                String curKeyValToken = keyValListTokens.nextToken().trim();
                StringTokenizer keyValTokens = new StringTokenizer(curKeyValToken, "=");
                if (keyValTokens.countTokens() != 2) {
                    throw new IllegalArgumentException("Expected key=value pair, found " + curKeyValToken);
                }
                String key = keyValTokens.nextToken().trim();
                String val = keyValTokens.nextToken().trim();
                log.info("Found key/value pair: " + key + "=" + val);
                map.put(key, val);
            }
        } catch (Exception exc) {
            throw new IllegalArgumentException("Error parsing init string '" + initString + "'", exc);
        }

        initStringProcessed = true;
    }

    public final void addConsumer(final IKiekerRecordConsumer consumer, final String[] recordTypeSubscriptionList) {
        if (recordTypeSubscriptionList == null) {
            this.subscribedToAllList.add(consumer);
        } else {
            for (String recordTypeName : recordTypeSubscriptionList) {
                Collection<IKiekerRecordConsumer> cList = this.subscribedToTypeMap.get(recordTypeName);
                if (cList == null) {
                    cList = new Vector<IKiekerRecordConsumer>(0);
                    this.subscribedToTypeMap.put(recordTypeName, cList);
                }
                cList.add(consumer);
            }
        }
    }

    protected final void deliverRecordToConsumers(final AbstractMonitoringRecord r) throws LogReaderExecutionException {
        try {
            for (IKiekerRecordConsumer c : this.subscribedToAllList) {
                c.consumeMonitoringRecord(r);
            }
            Collection<IKiekerRecordConsumer> cList = this.subscribedToTypeMap.get(r.getClass().getName());
            if (cList != null) {
                for (IKiekerRecordConsumer c : cList) {
                    c.consumeMonitoringRecord(r);
                }
            }
        } catch (RecordConsumerExecutionException ex) {
            log.fatal("RecordConsumerExecutionException", ex);
            throw new LogReaderExecutionException("A RecordConsumerExecutionException " +
                    "was caught -- now being rethrown as LogReaderExecutionException", ex);
        }
    }

    protected final void registerRecordTypeIdMapping(int recordTypeId, String classname) throws LogReaderExecutionException {
        try {
            if (this.recordTypeMap.get(recordTypeId) != null) {
                log.warn("Record type with id " + recordTypeId + " already registered.");
                return;
            }

            Class<? extends AbstractMonitoringRecord> recordClass = Class.forName(classname).asSubclass(AbstractMonitoringRecord.class);
            this.recordTypeMap.put(recordClass.getClass().getName(), recordClass);
            log.info("Registered record type mapping " + recordTypeId + "/" + classname);
        } catch (ClassNotFoundException ex) {
            log.error("Error loading record type class by name", ex);
            throw new LogReaderExecutionException("Error loading record type class by name", ex);
        }
    }

    /** Returns the class for record type with the given id. 
     *  If no such mapping exists, null is returned. */
    protected final Class<? extends AbstractMonitoringRecord> fetchClassForRecordTypeId(String id) {
        return this.recordTypeMap.get(id);
    }

    public void terminate() {
        for (IKiekerRecordConsumer c : this.subscribedToAllList) {
            c.terminate();
        }
        for (Collection<IKiekerRecordConsumer> cList : this.subscribedToTypeMap.values()) {
            if (cList != null) {
                for (IKiekerRecordConsumer c : cList) {
                    c.terminate();
                }
            }
        }
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
     *
     * True is returned if its finished?
     *
     * @return
     * @throws LogReaderExecutionException
     */
    public abstract boolean execute() throws LogReaderExecutionException;
}
