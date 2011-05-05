package bookstoreApplication;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.IMonitoringReader;
import kieker.analysis.reader.JMSReader;
import kieker.analysis.reader.MonitoringReaderException;

/**
 *
 * @author Andre van Hoorn
 */
public class JMSAnalysisStarter {

    private static final long MAX_RT_NANOS = 1700;

    public static void main(final String[] args) throws MonitoringReaderException, MonitoringRecordConsumerException {
        final AnalysisController analysisInstance = new AnalysisController();
        final IMonitoringReader logReader =
                new JMSReader("tcp://127.0.0.1:3035/", "queue1");
        analysisInstance.setReader(logReader);
        final IMonitoringRecordConsumerPlugin consumer =
                new Consumer(JMSAnalysisStarter.MAX_RT_NANOS);
        analysisInstance.registerPlugin(consumer);

        analysisInstance.run();
    }
}
