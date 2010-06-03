package mySimpleKiekerExample.bookstoreTracing;

import java.util.Vector;

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
    }

    @Override
    public void run() {
        Bookstore.searchBook();
        synchronized (bookstoreScenarios) {
            bookstoreScenarios.remove(this);
            bookstoreScenarios.notify();
        }
    }

    public static void searchBook() {
        for (int i = 0; i < 1; i++) {
            Catalog.getBook(false);
            CRM.getOffers();
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
