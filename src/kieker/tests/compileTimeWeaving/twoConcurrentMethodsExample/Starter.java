package kieker.tests.compileTimeWeaving.twoConcurrentMethodsExample;

import kieker.tpmon.annotations.TpmonMonitoringProbe;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * kieker.tests.compileTimeWeaving.twoConcurrentMethodsExample.Starter
 *
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component tpmon.
 * 
 * @author Matthias Rohr
 * History:
 * 2008/10/20: Initial version
 *
 */

public class Starter extends Thread{
    static int numberOfRequests = 1500;
    static int interRequestTime = 25;    
    static int defaultSleeptimeMs = 500; 

    public static void main(String[] args) throws InterruptedException {
	Vector<Starter> runInstances = new Vector<Starter>();
	for (int i = 0; i < numberOfRequests; i++) {
		Starter newBookstore = new Starter();
		runInstances.add(newBookstore);
		newBookstore.start();		
                Thread.sleep(interRequestTime);
	}
    	Thread.sleep(5000);
	System.exit(0);
    }
    
    @Override
    public void run() {
        try {
            waitabit(defaultSleeptimeMs);
            workabit();
        } catch (InterruptedException ex) { }
    }        
       
    @TpmonMonitoringProbe()
    public void waitabit(long waittime) throws InterruptedException {    			
		Thread.sleep(waittime);		
    }
    
        static AtomicBoolean boolvar = new AtomicBoolean(true);
    @TpmonMonitoringProbe()
    private void workabit() {
        int a = (int)(Math.random() * 5d);        
        for (int i=0; i<2500000; i++) { a += i/1000;}        
        // extremely rare event
        if (a % 10000 == 0 ) boolvar.set(true);        
    }
} 
