package kieker.tests.helloWorld.manualInstrumentation;
import kieker.tpmon.monitoringRecord.KiekerExecutionRecord;
import kieker.tpmon.core.TpmonController;

public class HelloWorld {

    public static void main(String args[]) {
        System.out.println("Hello");
        
        /* recording of the start time of doSomething */
        long startTime = System.nanoTime();
        
        doSomething();
        
        long endTime = System.nanoTime();
        TpmonController.getInstance().insertMonitoringDataNow(KiekerExecutionRecord.getInstance("kieker.component", "method", 1, startTime, endTime));
                
        /* System.exit() called to initiate shutdown of 
         * the monitoring logic running in seperate threads */
        System.exit(0);
    }

    static void doSomething() {
        System.out.println("doing something");
        /* .. some application logic does something meaningful ..*/        
    }
}
