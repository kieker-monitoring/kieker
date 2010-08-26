package bookstoreApplication;

import java.util.Vector;

import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.monitoring.core.MonitoringController;

public class Bookstore extends Thread {

    private final static MonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();
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
        MyPipeReader reader = new MyPipeReader();
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
        MyResponseTimeRecord e = new MyResponseTimeRecord();
        e.className = "mySimpleKiekerExample.bookstoreTracing.Bookstore";
        e.methodName = "searchBook()";
        /* Make sure that the record will somehow be persisted. */
        MonitoringController.getInstance().newMonitoringRecord(e);

        synchronized (bookstoreScenarios) {
            bookstoreScenarios.remove(this);
            bookstoreScenarios.notify();
        }
    }

    public void searchBook() {
        for (int i = 0; i < 1; i++) {

            /* Invoke method and measure response time */
            long tin = MONITORING_CONTROLLER.currentTimeNanos();
            catalog.getBook(false);
            long tout = MONITORING_CONTROLLER.currentTimeNanos();
            /* Create a new record and set values */
            MyResponseTimeRecord e = new MyResponseTimeRecord();
            e.className = "mySimpleKiekerExample.bookstoreTracing.Catalog";
            e.methodName = "getBook(false)";
            e.responseTimeNanos = tout - tin;
            /* Pass the record to the monitoring controller */
            MONITORING_CONTROLLER.newMonitoringRecord(e);

            crm.getOffers();
            /* Create a new record */
            e = new MyResponseTimeRecord();
            e.className = "mySimpleKiekerExample.bookstoreTracing.CRM";
            e.methodName = "getOffers()";
            /* Pass the record to the monitoring controller */
            MONITORING_CONTROLLER.newMonitoringRecord(e);
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
