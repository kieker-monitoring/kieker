package bookstoreTracing;

import java.util.Vector;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.monitoring.core.MonitoringController;

public class Bookstore extends Thread {

    static int numberOfRequests = 1;
    static int interRequestTime = 5;
    static final Vector<Bookstore> bookstoreScenarios = new Vector<Bookstore>();
    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(catalog);

    public static void main(String[] args) throws InterruptedException,
            MonitoringLogReaderException, MonitoringRecordConsumerException {
        for (int i = 0; i < numberOfRequests; i++) {
            System.out.println("Bookstore.main: Starting request " + i);
            Bookstore newBookstore = new Bookstore();
            bookstoreScenarios.add(newBookstore);
            newBookstore.start();
            Bookstore.waitabit(interRequestTime);
        }
        System.out.println("Bookstore.main: Finished with starting all requests.");
        System.out.println("Bookstore.main: Waiting for threads to terminate");
        synchronized (bookstoreScenarios) {
            while (!bookstoreScenarios.isEmpty()) {
                bookstoreScenarios.wait();
            }
        }

        AnalysisController ai = new AnalysisController();
        MyReader reader = new MyReader();
        reader.init("somePipe");
        ai.setLogReader(reader);
        ai.registerPlugin(new MyConsumer());
        ai.run();

        System.exit(0);
    }

    @Override
    public void run() {
        this.searchBook();
        /* Create a new record */
        MyRecord e = new MyRecord();
        e.component = "mySimpleKiekerExample.bookstoreTracing.Bookstore";
        e.service = "searchBook()";
        /* Make sure that the record will somehow be persisted. */
        MonitoringController.getInstance().newMonitoringRecord(e);

        synchronized (bookstoreScenarios) {
            bookstoreScenarios.remove(this);
            bookstoreScenarios.notify();
        }
    }

    public void searchBook() {
        for (int i = 0; i < 1; i++) {
            catalog.getBook(false);

            /* Create a new record */
            MyRecord e = new MyRecord();
            e.component = "mySimpleKiekerExample.bookstoreTracing.Catalog";
            e.service = "getBook(false)";
            /* Make sure that the record will somehow be persisted. */
            MonitoringController.getInstance().newMonitoringRecord(e);

            crm.getOffers();
            /* Create a new record */
            e = new MyRecord();
            e.component = "mySimpleKiekerExample.bookstoreTracing.CRM";
            e.service = "getOffers()";
            /* Make sure that the record will somehow be persisted. */
            MonitoringController.getInstance().newMonitoringRecord(e);
        }
    }

    public static void waitabit(long waittime) {
        if (waittime > 0) {
            try {
                Thread.sleep(waittime);
            } catch (Exception e) {
            }
        }
    }
}
