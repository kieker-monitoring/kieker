package bookstoreTracing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class BookstoreHostnameRewriter {

    public static void main(String[] args)
            throws MonitoringLogReaderException, MonitoringRecordConsumerException {

        if (args.length == 0) {
            return;
        }

        /* Create Kieker.Analysis instance */
        AnalysisController analysisInstance = new AnalysisController();
        analysisInstance.registerPlugin(new HostNameRewriterPlugin());

        /* Set filesystem monitoring log input directory for our analysis */
        String inputDirs[] = {args[0]};
        analysisInstance.setLogReader(new FSReader(inputDirs));

        /* Start the analysis */
        analysisInstance.run();
    }
}

class HostNameRewriterPlugin implements IMonitoringRecordConsumerPlugin {

    private static final MonitoringController MONITORING_CTRL =
            MonitoringController.getInstance();
    private static final Collection<Class<? extends IMonitoringRecord>> RECORDTYPE_SUBSCR_LIST =
            new ArrayList<Class<? extends IMonitoringRecord>>();

    static {
        RECORDTYPE_SUBSCR_LIST.add(OperationExecutionRecord.class);
    }

    @Override
    public Collection<Class<? extends IMonitoringRecord>> getRecordTypeSubscriptionList() {
        return RECORDTYPE_SUBSCR_LIST;
    }
    private static final String BOOKSTORE_HOSTNAME = "SRV0";
    private static final Random rnd = new Random();
    private static final int RND_PERCENTILE_HOST_IDX_1 = 34;
    private static final String[] CATALOG_HOSTNAMES = {"SRV0", "SRV1"};
    private static final String CRM_HOSTNAME = "SRV0";

    @Override
    public boolean newMonitoringRecord(IMonitoringRecord record) {
        if (!(record instanceof OperationExecutionRecord)) {
            return true;
        }

        OperationExecutionRecord execution =
                (OperationExecutionRecord) record;

        if (execution.className.equals(Bookstore.class.getName())) {
            execution.hostName = BOOKSTORE_HOSTNAME;
        } else if (execution.className.equals(CRM.class.getName())) {
            execution.hostName = CRM_HOSTNAME;
        } else if (execution.className.equals(Catalog.class.getName())) {
            if (rnd.nextInt(99) < RND_PERCENTILE_HOST_IDX_1) {
                execution.hostName = CATALOG_HOSTNAMES[0];
            } else {
                execution.hostName = CATALOG_HOSTNAMES[1];
            }
        }
        MONITORING_CTRL.newMonitoringRecord(record);

        return true;
    }

    @Override
    public boolean execute() {
        return true; // do nothing
    }

    @Override
    public void terminate(boolean error) {
        return; // do nothing
    }
}
