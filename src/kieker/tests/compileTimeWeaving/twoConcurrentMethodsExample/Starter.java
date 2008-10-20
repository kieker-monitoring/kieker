package kieker.tests.compileTimeWeaving.twoConcurrentMethodsExample;

import kieker.tpmon.annotations.TpmonMonitoringProbe;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * kieker.tests.compileTimeWeaving.twoConcurrentMethodsExample.Starter
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
 * monitoring component tpmon.
 * 
 * 
 *
 * @author Matthias Rohr
 * History:
 * 2008/10/20: Initial version
 *
 */

public class Starter extends Thread{
    static int numberOfRequests = 1000;
    static int interRequestTime = 5;
    static int defaultSleeptimeMs = 10;

    /**
     *
     */
    @TpmonMonitoringProbe()
    public static void main(String[] args) {
	
	Vector<Starter> oneRun = new Vector<Starter>();
	
	for (int i = 0; i < numberOfRequests; i++) {
    		System.out.println("Bookstore.main: Starting request "+i);
		Starter newBookstore = new Starter();
		oneRun.add(newBookstore);
		newBookstore.start();
		Starter.waitabit(interRequestTime);
	}
    	System.out.println("Bookstore.main: Finished with starting all requests.");
    	System.out.println("Bookstore.main: Waiting 5 secs before calling system.exit");
		waitabit(5000);
		System.exit(0);
    }

    // just to ensure that the compiler cannot optimized workabit through removal
    static AtomicBoolean ab = new AtomicBoolean(true);
    
    @TpmonMonitoringProbe()
    private static void workabit() {
        int a = (int)(Math.random() * 5d);
        int b = 0;
        for (int i = 0; i < 1000; i++) {
            a = a + (b/4) + (i/1000);
        }
        if (a % 1000 == 0 ) { // extremely rare event ...  
            ab.set(true);
        }
        
    }

    public void run() {
    	Starter.a();
    }

    @TpmonMonitoringProbe()
    public static void a() {
        waitabit(defaultSleeptimeMs);
        workabit();
    }
   
    /**
     * Only encapsulates Thread.sleep()
     */
    @TpmonMonitoringProbe()
    public static void waitabit(long waittime) {
    	if (waittime > 0) {
		try{
		Thread.sleep(waittime);
		} catch(Exception e) {}
	}
    }
} 
