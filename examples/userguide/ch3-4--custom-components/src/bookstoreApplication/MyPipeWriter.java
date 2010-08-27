package bookstoreApplication;

import java.util.Vector;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.PropertyMap;
import kieker.monitoring.writer.IMonitoringLogWriter;
import kieker.monitoring.writer.util.async.AbstractWorkerThread;

public class MyPipeWriter implements IMonitoringLogWriter {

    private static final String PROPERTY_PIPE_NAME = "pipeName";
    private volatile MyPipe pipe;

    @Override
    public boolean init(String initString) throws IllegalArgumentException {
        try {
            PropertyMap propertyMap = new PropertyMap(initString, "|", "=");
            String pipeName = propertyMap.getProperty(PROPERTY_PIPE_NAME);
            this.pipe = MyNamedPipeManager.getInstance().acquirePipe(pipeName);
        } catch (IllegalArgumentException ex) {
            return false; // signal error
        }
        return true;
    }

    @Override
    public boolean newMonitoringRecord(IMonitoringRecord record) {
        try {
            /* Just write the content of the record into the pipe. */
            this.pipe.put(record.toArray());
        } catch (InterruptedException e) {
            return false; // signal error
        }
        return true;
    }

    @Override
    public String getInfoString() {
        return MyPipeWriter.class.getName();
    }

    @Override
    public Vector<AbstractWorkerThread> getWorkers() {
        /* We don't have any worker threads, so we return just null. */
        return null;
    }
}
