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
import kieker.common.record.IMonitoringRecord;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.consumer.MonitoringRecordConsumerExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractMonitoringLogReader implements IMonitoringLogReader {

    private static final Log log = LogFactory.getLog(AbstractMonitoringLogReader.class);
    private final HashMap<String, String> map = new HashMap<String, String>();
    /** id x class object */
    protected Map<Integer, Class<? extends IMonitoringRecord>> recordTypeMap = Collections.synchronizedMap(new HashMap<Integer, Class<? extends IMonitoringRecord>>());
    /** Contains all consumers which consume records of any type */
    private final Collection<IMonitoringRecordConsumer> subscribedToAllList =
            new Vector<IMonitoringRecordConsumer>();
    /** Contains mapping of record types to subscribed consumers */
    private final HashMap<String, Collection<IMonitoringRecordConsumer>> subscribedToTypeMap =
            new HashMap<String, Collection<IMonitoringRecordConsumer>>();

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

    public final void addConsumer(final IMonitoringRecordConsumer consumer, final String[] recordTypeSubscriptionList) {
        if (recordTypeSubscriptionList == null) {
            this.subscribedToAllList.add(consumer);
        } else {
            for (String recordTypeName : recordTypeSubscriptionList) {
                Collection<IMonitoringRecordConsumer> cList = this.subscribedToTypeMap.get(recordTypeName);
                if (cList == null) {
                    cList = new Vector<IMonitoringRecordConsumer>(0);
                    this.subscribedToTypeMap.put(recordTypeName, cList);
                }
                cList.add(consumer);
            }
        }
    }

    protected final void deliverRecordToConsumers(final IMonitoringRecord r) throws LogReaderExecutionException {
        try {
            for (IMonitoringRecordConsumer c : this.subscribedToAllList) {
                c.consumeMonitoringRecord(r);
            }
            Collection<IMonitoringRecordConsumer> cList = this.subscribedToTypeMap.get(r.getClass().getName());
            if (cList != null) {
                for (IMonitoringRecordConsumer c : cList) {
                    c.consumeMonitoringRecord(r);
                }
            }
        } catch (MonitoringRecordConsumerExecutionException ex) {
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

            Class<? extends IMonitoringRecord> recordClass = Class.forName(classname).asSubclass(IMonitoringRecord.class);
            this.recordTypeMap.put(recordTypeId, recordClass);
            log.info("Registered record type mapping " + recordTypeId + "/" + classname);
        } catch (ClassNotFoundException ex) {
            log.error("Error loading record type class by name", ex);
            throw new LogReaderExecutionException("Error loading record type class by name", ex);
        }
    }

    /** Returns the class for record type with the given id. 
     *  If no such mapping exists, null is returned. */
    protected final Class<? extends IMonitoringRecord> fetchClassForRecordTypeId(int id) {
        return this.recordTypeMap.get(id);
    }

    public void terminate() {
        for (IMonitoringRecordConsumer c : this.subscribedToAllList) {
            c.terminate();
        }
        for (Collection<IMonitoringRecordConsumer> cList : this.subscribedToTypeMap.values()) {
            if (cList != null) {
                for (IMonitoringRecordConsumer c : cList) {
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
