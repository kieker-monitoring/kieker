package bookstoreApplication;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.IMonitoringRecordConsumerPlugin;
import kieker.analysis.reader.IMonitoringReader;

public class Starter {

    public static void main(final String[] args) throws Exception {
        /* Spawn a thread that performs asynchronous requests
         * to a bookstore. */
        new Thread(new Runnable() {

            @Override
			public void run() {
                final Bookstore bookstore = new Bookstore();
                for (int i = 0; i < 5; i++) {
                    System.out.println("Bookstore.main: Starting request " + i);
                    bookstore.searchBook();
                }
            }
        }).start();


        /* Start an analysis of the response times */
        final AnalysisController analyisController = new AnalysisController();
        final IMonitoringReader reader =
                new MyPipeReader("somePipe");
        final IMonitoringRecordConsumerPlugin consumer =
                new MyResponseTimeConsumer();
        analyisController.setReader(reader);
        analyisController.registerPlugin(consumer);
        analyisController.run();
    }
}
