package kieker.analysis.reader;

import kieker.common.record.MonitoringRecordReceiverException;

/**
 *
 * @author Andre van Hoorn
 */
public class MonitoringReaderException extends MonitoringRecordReceiverException {

    private static final long serialVersionUID = 14537L;

    public MonitoringReaderException(String messString) {
        super(messString);
    }

    public MonitoringReaderException(String messString, Throwable cause) {
        super(messString, cause);
    }
}
