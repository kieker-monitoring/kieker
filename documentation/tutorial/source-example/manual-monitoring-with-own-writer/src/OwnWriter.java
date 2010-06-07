package mySimpleKiekerExample.bookstoreTracing;

import java.util.Vector;
import kieker.tpmon.writer.IMonitoringLogWriter;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;
import kieker.common.record.*;

public class OwnWriter implements IMonitoringLogWriter {

    /**
     * Initialize instance from passed initialization string which is typically
     * a list of separated parameter/values pairs.
     * The implementing class AbstractMonitoringLogWriter includes convenient
     * methods to extract configuration values from an initString.
     *
     * @param initString the initialization string
     * @return true iff the initialiation was successful
     */
    public boolean init(String initString) {
        return true;
    }

    /**
     * Called for each new record.
     * 
     * Notice, that this method should not throw an exception,
     * but indicate an error by the return value false.
     *
     * @param record the record.
     * @return true on success; false in case of an error.
     */
    public boolean newMonitoringRecord(IMonitoringRecord record) throws MonitoringRecordReceiverException {
        Object arr[] = record.toArray();
        int len = arr.length;
        if (len > 0) {
            for (int i = 0; i < len - 1; i++) {
                System.out.print(arr[i].toString() + ", ");
            }
            System.out.println(arr[len - 1]);
        }
        return true;
    }

    /**
     *
     * Returns a vector of workers, or null if none.
     */
    public Vector<AbstractWorkerThread> getWorkers() {
        return null;
    }

    /**
     * Returns a human-readable information string about the writer's
     * configuration and state.
     *
     * @return the information string.
     */
    public String getInfoString() {
        return null;
    }
} 
