package kieker.tests.helloWorld.manualInstrumentation;
import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class HelloWorld {

    public static void main(String args[]) {
        System.out.println("Hello");
        
        /* recording of the start time of doSomething */
        long startTime = System.nanoTime();
        
        doSomething();
        
        long endTime = System.nanoTime();
        MonitoringController.getInstance().newMonitoringRecord(new OperationExecutionRecord("kieker.component", "method", 1, startTime, endTime));
                
        /* System.exit() called to initiate shutdown of 
         * the monitoring logic running in seperate threads */
        System.exit(0);
    }

    static void doSomething() {
        System.out.println("doing something");
        /* .. some application logic does something meaningful ..*/        
    }
}
