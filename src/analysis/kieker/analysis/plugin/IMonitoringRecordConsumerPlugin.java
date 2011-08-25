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
     * @return list of specific record types to subscribe to; null to subsribe to any type
     */
    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList();
}
