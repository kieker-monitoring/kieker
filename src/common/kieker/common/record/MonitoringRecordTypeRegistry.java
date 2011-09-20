/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.common.record;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class MonitoringRecordTypeRegistry {

    private static final Log LOG = LogFactory.getLog(MonitoringRecordTypeRegistry.class);
    /** recordTypeId x class object */
    private final Map<Integer, Class<? extends IMonitoringRecord>> recordTypeMap = new ConcurrentHashMap<Integer, Class<? extends IMonitoringRecord>>();

    public final static String OLD_KIEKEREXECUTIONRECORD_CLASSNAME =
            "kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord";
    /**
     * If true, the classname ${@value #OLD_KIEKEREXECUTIONRECORD_CLASSNAME}
     * is automatically mapped to the new classname of ${@link OperationExecutionRecord}.
     */
    private final boolean oldKiekerExecutionRecordCompatibilityMode;

    public MonitoringRecordTypeRegistry (){
        oldKiekerExecutionRecordCompatibilityMode = false;
    }

    /**
     * @see #oldKiekerExecutionRecordCompatibilityMode
     *
     * @param oldKiekerExecutionRecordCompatibilityMode
     */
    public MonitoringRecordTypeRegistry (final boolean oldKiekerExecutionRecordCompatibilityMode){
        this.oldKiekerExecutionRecordCompatibilityMode =
                oldKiekerExecutionRecordCompatibilityMode;
    }

    /**
     * Registers a mapping of the given record type recordTypeId to the corresponding classname.
     *
     * This method should be used by implementing classes.
     *
     * @param recordTypeId
     * @param classname
     * @throws ClassNotFoundException
     */
    public final void registerRecordTypeIdMapping(final int recordTypeId, final String classname) throws ClassNotFoundException {
        try {
            if (this.recordTypeMap.get(recordTypeId) != null) {
                LOG.warn("Record type with id " + recordTypeId + " already registered.");
                return;
            }
            String myClassname;
            /**
             * If the compatibility mode for the old execution record name is
             * enables, map old name to new name. */
            if (this.oldKiekerExecutionRecordCompatibilityMode && classname.equals(OLD_KIEKEREXECUTIONRECORD_CLASSNAME)){
            	myClassname = OperationExecutionRecord.class.getName();
            } else {
            	myClassname = classname;
            }

            this.recordTypeMap.put(recordTypeId, Class.forName(myClassname).asSubclass(IMonitoringRecord.class));
            LOG.info("Registered record type mapping " + recordTypeId + "/" + myClassname);
        } catch (ClassNotFoundException ex) {
            LOG.error("Error loading record type class by name", ex);
            throw ex;
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
    public final Class<? extends IMonitoringRecord> fetchClassForRecordTypeId(final int recordTypeId) {
        return this.recordTypeMap.get(recordTypeId);
    }
}
