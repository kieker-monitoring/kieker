package kieker.tests.compileTimeWeaving.twoConcurrentMethodsExample;

import kieker.monitoring.annotation.TpmonExecutionMonitoringProbe;

/**
 * kieker.tests.compileTimeWeaving.twoConcurrentMethodsExample.Starter
 *
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component tpmon.
 * 
 * @author Matthias Rohr
 * History:
 * 2009/02/20: Reduced text length
 * 2008/10/20: Initial version
 *
 */

public class Starter extends Thread{
   public static void main(String[] args) throws InterruptedException {
	for (int i = 0; i < 10000; i++) {
		new Starter().start();		
                Thread.sleep((int)(Math.max(0,Math.random() * 115d - (i/142d) )+1)); // wait between requests
	}
	System.exit(0);
    }

    public void run() {
		double ranVal = Math.random();
		if (ranVal < 0.5 ) {
			if (ranVal < 0.25) {
				// do nothing
			} else {
				waitP(300);
			}
		}else {
			if (ranVal > 0.75) {
				work();
				waitP(300);
			} else {
				work();
			}
		}
    }

    @TpmonExecutionMonitoringProbe()
    public void waitP(long sleeptime) {
	try{Thread.sleep(sleeptime);} catch (Exception e){}
    }

    static boolean boolvar = true;
    @TpmonExecutionMonitoringProbe()
    private void work() {
        int a = (int)(Math.random() * 5d);
        for (int i=0; i<2000000; i++) { a += i/1000;}
        if (a % 10000 == 0 ) boolvar = false;
    }
} 
