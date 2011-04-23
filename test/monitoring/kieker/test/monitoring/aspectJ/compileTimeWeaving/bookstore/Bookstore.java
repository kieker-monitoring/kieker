package kieker.test.monitoring.aspectJ.compileTimeWeaving.bookstore;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;
import java.util.Vector;

/**
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component. 
 *
 * @author Matthias Rohr
 * History:
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */
public class Bookstore extends Thread{
    public static final Vector<Bookstore> bookstoreScenarios = new Vector<Bookstore>();

    static int numberOfRequests = 1000;
    static int interRequestTime = 25;

    /**
     *
     * main is the load driver for the Bookstore. It creates 
     * request which all request a search from the bookstore. 
     * A fixed time delay is between two request. Requests 
     * are likely to overlap, which leads to request processing
     * in more than one thread. 
     *
     * Both the number of requests and arrival rate are defined 
     * by the local variables above the method.
     * (default: 100 requests; interRequestTime 5 (millisecs))
     */ 
    public static void main(String[] args) throws InterruptedException {
	for (int i = 0; i < numberOfRequests; i++) {
    		System.out.println("Bookstore.main: Starting request "+i);
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

    public void run() {
    	Bookstore.searchBook();
        synchronized (bookstoreScenarios) {
            bookstoreScenarios.remove(this);
            bookstoreScenarios.notify();
        }
    }

    @OperationExecutionMonitoringProbe()
    public static void searchBook() {
	Catalog.getBook(false);	
	CRM.getOffers();
    }
   
    /**
     * Only encapsulates Thread.sleep()
     */
    public static void waitabit(long waittime) {
    	if (waittime > 0) {
		try{
		Thread.sleep(waittime);
		} catch(Exception e) {}
	}
    }
} 
