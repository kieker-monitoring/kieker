package bookstoreApplication;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringReaderException;
import kieker.analysis.reader.filesystem.FSReader;

public class BookstoreAnalysisStarter {

    public static void main(final String[] args)
            throws MonitoringReaderException, MonitoringRecordConsumerException {
       
        if (args.length == 0) {
            return;
        }

        /* Create Kieker.Analysis instance */
        final AnalysisController analysisInstance = new AnalysisController();
        /* Register our own consumer; set the max. response time to 1.9 ms */
        analysisInstance.registerPlugin(new Consumer(1900000));

        /* Set filesystem monitoring log input directory for our analysis */
        final String inputDirs[] = {args[0]};
        analysisInstance.setLogReader(new FSReader(inputDirs));

        /* Start the analysis */
        analysisInstance.run();
    }
}
