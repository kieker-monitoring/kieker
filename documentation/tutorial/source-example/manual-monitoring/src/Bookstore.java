package mySimpleKiekerExample.bookstoreTracing;

import java.util.Vector;
import kieker.tpmon.core.TpmonController;
import kieker.common.record.OperationExecutionRecord;

public class Bookstore extends Thread {

    static int numberOfRequests = 1;
    static int interRequestTime = 5;
    static final Vector<Bookstore> bookstoreScenarios = new Vector<Bookstore>();

    public static void main(String[] args) throws InterruptedException {
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
        System.exit(0);
    }

    @Override
    public void run() {
        long tin = TpmonController.getInstance().getTime();
        Bookstore.searchBook();
        long tout = TpmonController.getInstance().getTime();

        OperationExecutionRecord e = new OperationExecutionRecord("mySimpleKiekerExample.bookstoreTracing.Bookstore", "searchBook()", "sessionID", 0, tin, tout, "vnName", 0, 0);
        TpmonController.getInstance().newMonitoringRecord(e);

        synchronized (bookstoreScenarios) {
            bookstoreScenarios.remove(this);
            bookstoreScenarios.notify();
        }
    }

    public static void searchBook() {
        for (int i = 0; i < 1; i++) {

            long tin = TpmonController.getInstance().getTime();
            Catalog.getBook(false);
            long tout = TpmonController.getInstance().getTime();

            OperationExecutionRecord e = new OperationExecutionRecord("mySimpleKiekerExample.bookstoreTracing.Catalog", "getBook(false)", "sessionID", 0, tin, tout, "vnName", 1, 1);
            TpmonController.getInstance().newMonitoringRecord(e);

            tin = TpmonController.getInstance().getTime();
            CRM.getOffers();
            tout = TpmonController.getInstance().getTime();

            e = new OperationExecutionRecord("mySimpleKiekerExample.bookstoreTracing.CRM", "getOffers()", "sessionID", 0, tin, tout, "vnName", 2, 1);
            TpmonController.getInstance().newMonitoringRecord(e);
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
