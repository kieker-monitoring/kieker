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

        /* Prepare the analysis */
        AnalysisInstance analysisInstance = new AnalysisInstance();
        /* Register our own consumer. */
        analysisInstance.registerPlugin(new Consumer(3));

        /* Get the output directory from the given argument. */
        String outputDir = args[0];
        /* ...and use it as input directory for our analysis */
        String inputDirs[] = {outputDir};
        analysisInstance.setLogReader(new FSReader(inputDirs));

        /* Now start the analysis */
        analysisInstance.run();
    }
}
