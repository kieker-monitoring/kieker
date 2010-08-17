package bookstoreApplication;

import kieker.analysis.AnalysisInstance;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.monitoring.core.MonitoringController;

public class BookstoreAnalysisStarter {

    public static void main(String[] args)
            throws MonitoringLogReaderException, MonitoringRecordConsumerException {
       
		if (args.length == 0) {
		  return;
		}

        /* Create Kieker.Analysis instance */
        AnalysisInstance analysisInstance = new AnalysisInstance();
        /* Register our own consumer. */
        analysisInstance.registerPlugin(new Consumer(3));

        /* Set filesystem monitoring log input directory for our analysis */
        String inputDirs[] = {args[0]};
        analysisInstance.setLogReader(new FSReader(inputDirs));

        /* Start the analysis */
        analysisInstance.run();
    }
}
