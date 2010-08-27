package bookstoreApplication;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.reader.IMonitoringLogReader;

public class Starter {

    public static void main(String[] args) throws Exception {
        Bookstore bookstore = new Bookstore();
        for (int i = 0; i < 5; i++) {
            System.out.println("Bookstore.main: Starting request " + i);
            bookstore.searchBook();
        }

        AnalysisController analyisController = new AnalysisController();
        IMonitoringLogReader reader =
                new MyPipeReader("somePipe");
        IMonitoringRecordConsumerPlugin consumer =
                new MyResponseTimeConsumer();
        analyisController.setLogReader(reader);
        analyisController.registerPlugin(consumer);
        analyisController.run();
    }
}
