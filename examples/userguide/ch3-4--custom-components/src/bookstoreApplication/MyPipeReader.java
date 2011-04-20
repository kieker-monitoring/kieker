package bookstoreApplication;

import kieker.analysis.reader.AbstractMonitoringReader;
import kieker.analysis.util.PropertyMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyPipeReader extends AbstractMonitoringReader {

	private static final Log log = LogFactory.getLog(MyPipeReader.class);
	
    private static final String PROPERTY_PIPE_NAME = "pipeName";

    private volatile MyPipe pipe;

    public MyPipeReader () {}

    public MyPipeReader (final String pipeName) {
        this.init(MyPipeReader.PROPERTY_PIPE_NAME+"="+pipeName);
    }
    
    @Override
    public boolean init(final String initString) throws IllegalArgumentException {
    	try {
        final PropertyMap propertyMap = new PropertyMap(initString, "|", "=");
        final String pipeName = propertyMap.getProperty(MyPipeReader.PROPERTY_PIPE_NAME);
        this.pipe = MyNamedPipeManager.getInstance().acquirePipe(pipeName);
		} catch (final Exception exc) {
			MyPipeReader.log.error("Failed to parse initString '" + initString
					+ "': " + exc.getMessage());
			return false;
		}
		return true;
    }

    @Override
    public boolean read() {
        try {
            Object[] objArray;
            /* Wait max. 4 seconds for the next data. */
            while ((objArray = this.pipe.poll(4)) != null) {
                /* Create new record, init from received array ... */
                final MyResponseTimeRecord record = new MyResponseTimeRecord();
                record.initFromArray(objArray);
                /* ...and delegate the task of delivering to the super class. */
                this.deliverRecord(record);
            }
        } catch (final InterruptedException e) {
            return false; // signal error
        }
        return true;
    }
}
