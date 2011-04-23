package kieker.common.record;

/**
 *
 * @author Andre van Hoorn
 */
public class MonitoringRecordReceiverException extends Exception {

    private static final long serialVersionUID = 76576L;

    public MonitoringRecordReceiverException(String messString) {
        super(messString);
    }

    public MonitoringRecordReceiverException(String messString, Throwable cause) {
        super(messString, cause);
    }
}
