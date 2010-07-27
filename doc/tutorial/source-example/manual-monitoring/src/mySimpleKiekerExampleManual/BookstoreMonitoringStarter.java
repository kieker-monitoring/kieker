package mySimpleKiekerExampleManual;

import kieker.analysis.AnalysisInstance;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.monitoring.core.MonitoringController;

public class BookstoreMonitoringStarter {

    public static void main(String[] args)
            throws MonitoringLogReaderException, MonitoringRecordConsumerException {
        Bookstore bookstore = new Bookstore();
        for (int i = 0; i < 5; i++) {
            System.out.println("Bookstore.main: Starting request " + i);
            bookstore.searchBook();
        }
    }
}
