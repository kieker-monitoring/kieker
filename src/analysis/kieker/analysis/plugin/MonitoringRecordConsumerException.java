package kieker.analysis.plugin;

/**
 *
 * @author Andre van Hoorn
 */
public class MonitoringRecordConsumerException extends Exception {

    private static final long serialVersionUID = 1L;

    public MonitoringRecordConsumerException(String messString) {
        super(messString);
    }

    public MonitoringRecordConsumerException(String messString, Throwable cause) {
        super(messString, cause);
    }
}
