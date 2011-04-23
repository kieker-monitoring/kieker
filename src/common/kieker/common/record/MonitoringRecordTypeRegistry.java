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

    private static final Log log = LogFactory.getLog(MonitoringRecordTypeRegistry.class);
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
    public final void registerRecordTypeIdMapping(int recordTypeId, String classname) throws ClassNotFoundException {
        try {
            if (this.recordTypeMap.get(recordTypeId) != null) {
                log.warn("Record type with id " + recordTypeId + " already registered.");
                return;
            }

            /**
             * If the compatibility mode for the old execution record name is
             * enables, map old name to new name. */
            if (this.oldKiekerExecutionRecordCompatibilityMode && classname.equals(OLD_KIEKEREXECUTIONRECORD_CLASSNAME)){
                classname = OperationExecutionRecord.class.getName();
            }

            Class<? extends IMonitoringRecord> recordClass = Class.forName(classname).asSubclass(IMonitoringRecord.class);
            this.recordTypeMap.put(recordTypeId, recordClass);
            log.info("Registered record type mapping " + recordTypeId + "/" + classname);
        } catch (ClassNotFoundException ex) {
            log.error("Error loading record type class by name", ex);
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
    public final Class<? extends IMonitoringRecord> fetchClassForRecordTypeId(int recordTypeId) {
        return this.recordTypeMap.get(recordTypeId);
    }
}
