package kieker.analysis.plugin;

import java.util.Collection;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

/**
 * @author Andre van Hoorn
 */
public interface IMonitoringRecordConsumerPlugin extends IMonitoringRecordReceiver, IAnalysisPlugin {

    /**
     * Simply return null to get records of all types.
     * 
     * @return
     */
    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList();
}
