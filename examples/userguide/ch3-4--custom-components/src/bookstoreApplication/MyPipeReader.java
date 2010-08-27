package bookstoreApplication;

import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.common.util.PropertyMap;

public class MyPipeReader extends AbstractMonitoringLogReader {

    private static final String PROPERTY_PIPE_NAME = "pipeName";

    private volatile MyPipe pipe;

    public MyPipeReader () {}

    public MyPipeReader (final String pipeName) {
        this.init(PROPERTY_PIPE_NAME+"="+pipeName);
    }
    
    @Override
    public void init(String initString) throws IllegalArgumentException {
        PropertyMap propertyMap = new PropertyMap(initString, "|", "=");
        String pipeName = propertyMap.getProperty(PROPERTY_PIPE_NAME);
        this.pipe = MyNamedPipeManager.getInstance().acquirePipe(pipeName);
    }

    @Override
    public boolean read() throws MonitoringLogReaderException {
        try {
            Object[] objArray;
            /* Wait max. 4 seconds for the next data. */
            while ((objArray = pipe.poll(4)) != null) {
                /* Create new record, init from received array ... */
                MyResponseTimeRecord record = new MyResponseTimeRecord();
                record.initFromArray(objArray);
                /* ...and delegate the task of delivering to the super class. */
                deliverRecord(record);
            }
        } catch (InterruptedException e) {
            return false; // signal error
        }
        return true;
    }
}
