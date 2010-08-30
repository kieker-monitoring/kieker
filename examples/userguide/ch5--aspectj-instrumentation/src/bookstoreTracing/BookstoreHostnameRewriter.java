package bookstoreTracing;

import bookstoreApplication.*;
import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.reader.filesystem.FSReader;

public class BookstoreHostnameRewriter {

    public static void main(String[] args)
            throws MonitoringLogReaderException, MonitoringRecordConsumerException {
       
		if (args.length == 0) {
		  return;
		}

        /* Create Kieker.Analysis instance */
        AnalysisController analysisInstance = new AnalysisController();
        /* Register our own consumer; set the max. response time to 1.9 ms */
        analysisInstance.registerPlugin(new Consumer(1900000));

        /* Set filesystem monitoring log input directory for our analysis */
        String inputDirs[] = {args[0]};
        analysisInstance.setLogReader(new FSReader(inputDirs));

        /* Start the analysis */
        analysisInstance.run();
    }
}
