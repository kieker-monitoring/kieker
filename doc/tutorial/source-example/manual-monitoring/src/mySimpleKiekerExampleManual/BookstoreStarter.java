package mySimpleKiekerExampleManual;

import kieker.analysis.AnalysisInstance;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.monitoring.core.MonitoringController;

public class BookstoreStarter {

    public static void main(String[] args)
            throws MonitoringLogReaderException, MonitoringRecordConsumerException {
        Bookstore bookstore = new Bookstore();
        /* Start the monitoring */
        for (int i = 0; i < 5; i++) {
            System.out.println("Bookstore.main: Starting request " + i);
		bookstore.searchBook();
        }

        /* Prepare the analysis */
        AnalysisInstance analysisInstance = new AnalysisInstance();
        /* Register our own consumer. */
        analysisInstance.registerPlugin(new Consumer(3));

        /* Get the output directory from the last monitoring... */
        String outputDirString = MonitoringController.getInstance().getConnectorInfo().split(", ")[3];
        String outputDir = outputDirString.split(" :")[1];
        /* ...and use it as input directory for our analysis */
        String inputDirs[] = {outputDir};
        analysisInstance.setLogReader(new FSReader(inputDirs));

        /* Now start the analysis */
        analysisInstance.run();
    }
}
