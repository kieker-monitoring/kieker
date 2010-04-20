package kieker.tpan.reader;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2010 Kieker Project
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
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
    /** recordTypeId x class object */
    private Map<Integer, Class<? extends IMonitoringRecord>> recordTypeMap = new ConcurrentHashMap<Integer, Class<? extends IMonitoringRecord>>();
    /** Contains all consumers which consume records of any type */
    private final Collection<IMonitoringRecordConsumer> anyTypeConsumers =
            new Vector<IMonitoringRecordConsumer>();
    /** Contains mapping of record types to subscribed consumers */
    private final HashMap<String, Collection<IMonitoringRecordConsumer>> specificTypeConsumers =
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
            this.anyTypeConsumers.add(consumer);
        } else {
            for (String recordTypeName : recordTypeSubscriptionList) {
                Collection<IMonitoringRecordConsumer> cList = this.specificTypeConsumers.get(recordTypeName);
                if (cList == null) {
                    cList = new Vector<IMonitoringRecordConsumer>(0);
                    this.specificTypeConsumers.put(recordTypeName, cList);
                }
                cList.add(consumer);
            }
        }
    }

    /**
     * Delivers the given record to the consumers that are registered for this
     * type of records.
     *
     * This method should be used by implementing classes.
     *
     * @param monitoringRecord the record
     * @throws LogReaderExecutionException if an error occurs
     */
    protected final void deliverRecordToConsumers(final IMonitoringRecord monitoringRecord) throws LogReaderExecutionException {
        try {
            for (IMonitoringRecordConsumer c : this.anyTypeConsumers) {
                c.consumeMonitoringRecord(monitoringRecord);
            }
            Collection<IMonitoringRecordConsumer> cList = this.specificTypeConsumers.get(monitoringRecord.getClass().getName());
            if (cList != null) {
                for (IMonitoringRecordConsumer c : cList) {
                    c.consumeMonitoringRecord(monitoringRecord);
                }
            }
        } catch (MonitoringRecordConsumerExecutionException ex) {
            log.fatal("RecordConsumerExecutionException", ex);
            throw new LogReaderExecutionException("A RecordConsumerExecutionException " +
                    "was caught -- now being rethrown as LogReaderExecutionException", ex);
        }
    }

    /**
     * Registers a mapping if the given record type recordTypeId to the corresponding classname.
     *
     * This method should be used by implementing classes.
     *
     * @param recordTypeId
     * @param classname
     * @throws LogReaderExecutionException
     */
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

    /**
     * Returns the class object for the given record type ID record type recordTypeId,
     * which has been registered before by calling the registerRecordTypeIdMapping
     * method.
     *
     * This method should be used by implementing classes.
     *
     * @param recordTypeId the record type ID
     * @return the class object
     */
    protected final Class<? extends IMonitoringRecord> fetchClassForRecordTypeId(int recordTypeId) {
        return this.recordTypeMap.get(recordTypeId);
    }
}
