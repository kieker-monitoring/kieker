package kieker.tests.compileTimeWeaving.bookstoreBenchmark;

import kieker.tpmon.annotation.TpmonExecutionMonitoringProbe;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * kieker.tests.compileTimeWeaving.bookstore.Bookstore.java
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
 *
 *
 * A simple test and demonstration scenario for Kieker's 
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

public class Bookstore extends Thread{
    static int numberOfRequests = 20000;
    static int interRequestTime = 1;

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
    @TpmonExecutionMonitoringProbe()
    public static void main(String[] args) {
	long startTime = System.nanoTime();
	Vector<Bookstore> bookstoreScenarios = new Vector<Bookstore>();
	
	for (int i = 0; i < numberOfRequests; i++) {
	        //if (i % 100 == 0) System.out.println("Bookstore.main: Starting request "+i);
		Bookstore newBookstore = new Bookstore();
		bookstoreScenarios.add(newBookstore);
		newBookstore.start();
		Bookstore.waitabit(interRequestTime);
	}
        
        long responseTime = System.nanoTime() - startTime;
        System.out.printf("Benchmark duration in millisecs:%3f\n",(double)(responseTime/(1000*1000)));
        System.out.printf("Throughput (request/sec):%.2f\n",(double)(numberOfRequests/(responseTime/(1000*1000*1000))));
        System.out.printf("Monitoring events / sec:%.2f\n",(double)(4*(numberOfRequests/(responseTime/(1000*1000*1000)))));
        
        
    	System.out.println("Bookstore.main: Finished with starting all requests.");
    	System.out.println("Bookstore.main: Waiting 3 secs before calling system.exit");
		waitabit(3000);
		System.exit(0);
    }
    
    public void doProcess(HttpServletRequest request, HttpServletResponse response){
        Bookstore.searchBook();        
    }
    
    public void run() {
        this.doProcess(null, null);
    }

    public static AtomicInteger reqs = new AtomicInteger(0);
    
    @TpmonExecutionMonitoringProbe()
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
