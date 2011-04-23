package kieker.test.monitoring.aspectJ.loadTimeWeaving.executionOrderTest;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test to test the execution ordering mechanism monitoring in distributed systems 
 * 
 * @author Matthias Rohr
 * History:
 * 
 * 2007-03-30: Initial version
 *
 */

public class ExecutionOrderTest {
    @OperationExecutionMonitoringProbe()
    private static void a(boolean b) {
        if (b) {   
            a2(b);            
        } else {
            a2(b);
            b();        
        }
    }

    @OperationExecutionMonitoringProbe()
    private static void a2(boolean b) {
        if (b) b();
    }

    @OperationExecutionMonitoringProbe()
    private static void b() {
        double d = 12 + ((double)System.currentTimeMillis()/1000d);
        for (int i=0; i < 10000; i++) {
            d = d + 5 + i;
        }
    }
    
    
    public static void main(String args[]) {       
        a(true);
        a(false);        
		d1();
    }


    @OperationExecutionMonitoringProbe()
	public static void d1() {
		d2();
	}

    @OperationExecutionMonitoringProbe()
	public static void d2() {
		d3();
	}

    @OperationExecutionMonitoringProbe()
	public static void d3() {
		System.out.println("d3()");
	}


        
} 
