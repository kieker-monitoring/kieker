package kieker.tests.loadTimeWeaving.executionOrderTest;

import kieker.tpmon.annotation.TpmonExecutionMonitoringProbe;

/**
 *  kieker.tests.loadTimeWeaving.executionOrderTest.ExecutionOrderTest.java
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
 * A simple test to test the execution ordering mechanism of the TpmonAnnotationRemoteAspect 
 * for monitoring in distributed systems 
 * 
 * @author Matthias Rohr
 * History:
 * 
 * 2007-03-30: Initial version
 *
 */

public class ExecutionOrderTest {
    @TpmonExecutionMonitoringProbe()
    private static void a(boolean b) {
        if (b) {   
            a2(b);            
        } else {
            a2(b);
            b();        
        }
    }

    @TpmonExecutionMonitoringProbe()
    private static void a2(boolean b) {
        if (b) b();
    }

    @TpmonExecutionMonitoringProbe()
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
        System.exit(0);
    }


    @TpmonExecutionMonitoringProbe()
	public static void d1() {
		d2();
	}

    @TpmonExecutionMonitoringProbe()
	public static void d2() {
		d3();
	}

    @TpmonExecutionMonitoringProbe()
	public static void d3() {
		System.out.println("d3()");
	}


        
} 
