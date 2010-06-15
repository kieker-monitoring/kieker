package kieker.tests.loadTimeWeaving.bookstoreDifferentRecordTypes;

import kieker.monitoring.annotation.TpmonExecutionMonitoringProbe;
import java.util.Vector;

/*
 *
 * ==================LICENCE=========================
 * Copyright 2006-2010 the Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
 
/** A simple test and demonstration scenario for Kieker's
 * monitoring component tpmon. See the kieker tutorial 
 * for more information 
 * (http://www.matthias-rohr.com/kieker/tutorial.html)
 *
 * @author Matthias Rohr
 * History:
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */
public class Bookstore extends Thread {

    static int numberOfRequests = 3;
    static int interRequestTime = 1000;

    public static final Vector<Bookstore> bookstoreScenarios = new Vector<Bookstore>();

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
     * 
     * This will be monitored by Tpmon, since it has the
     * TpmonExecutionMonitoringProbe() annotation.
     */
    //@TpmonExecutionMonitoringProbe()
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

    @TpmonExecutionMonitoringProbe()
    public static void searchBook() {
        for (int i = 0; i < 1; i++) {
            Catalog.getBook(false);
            CRM.getOffers();
        }
    }

    /**
     * Only encapsulates Thread.sleep()
     */
    public static void waitabit(long waittime) {
        if (waittime > 0) {
            try {
                Thread.sleep(waittime);
            } catch (Exception e) {
            }
        }
    }
} 
