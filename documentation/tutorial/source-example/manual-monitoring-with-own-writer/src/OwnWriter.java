package mySimpleKiekerExample.bookstoreTracing;

import kieker.tpmon.writer.AbstractKiekerMonitoringLogWriter;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;
import java.util.Vector;

public class OwnWriter extends AbstractKiekerMonitoringLogWriter {

    @TpmonInternal()
    public void registerMonitoringRecordType(int id, String className) {
    }

    @TpmonInternal()
    public boolean writeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) {
        String arr[] = monitoringRecord.toStringArray();
        int len = arr.length;
        if (len > 0) {
            for (int i = 0; i < len - 1; i++) {
                System.out.print(arr[i] + ", ");
            }
            System.out.println(arr[len - 1]);
        }
        return true;
    }

    @TpmonInternal()
    public boolean init(String initString) {
        return true;
    }

    /**
     * Returns a vector of workers, or null if none.
     */
    @TpmonInternal()
    public Vector<AbstractWorkerThread> getWorkers() {
        return null;
    }

    @TpmonInternal()
    public String getInfoString() {
        return null;
    }
} 
