package kieker.analysis.plugin;

import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn
 */
public class MonitoringRecordTypeLogger implements IMonitoringRecordConsumerPlugin {

    private static final Log log = LogFactory.getLog(MonitoringRecordTypeLogger.class);

    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return null; // receive records of any type
    }

    public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
        log.info("Consumed record:" + monitoringRecord.getClass().getName());
        log.info(monitoringRecord.toString());
        return true;
    }

    public boolean execute() {
        /* We consume synchronously */
        return true;
    }

    public void terminate(final boolean error) {
        /* We consume synchronously */
    }
}
