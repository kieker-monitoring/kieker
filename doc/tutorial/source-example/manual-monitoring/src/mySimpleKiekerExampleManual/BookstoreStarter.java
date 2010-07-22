package mySimpleKiekerExampleManual;

import kieker.analysis.AnalysisInstance;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.monitoring.core.MonitoringController;

public class BookstoreStarter {

	public static void main(String[] args) throws MonitoringLogReaderException,
			MonitoringRecordConsumerException {
		/* Start the monitoring */
		Bookstore.startBookstoreRequests();

		/* Prepare the analysis */
		AnalysisInstance ai = new AnalysisInstance();
		/* Register our own consumer. */
		ai.registerPlugin(new Consumer(3));
		
		/* Get the output directory from the last monitoring... */
		String outputDirString = MonitoringController.getInstance().getConnectorInfo().split(", ")[3];
		String outputDir = outputDirString.split(" :")[1];
		/* ...and use it as input directory for our analysis */
		String inputDirs[] = {outputDir};
		ai.setLogReader(new FSReader(inputDirs));
		
		/* Now start the analysis */
		ai.run();
	}

}
