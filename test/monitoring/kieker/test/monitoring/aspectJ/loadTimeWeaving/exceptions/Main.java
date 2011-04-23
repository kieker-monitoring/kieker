package kieker.test.monitoring.aspectJ.loadTimeWeaving.exceptions;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * Experimental test for kieker in combination with exceptions.
 *
 * @author Matthias Rohr
 * History:
 * 2008/01/10: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-05-10: Initial version
 *
 */

public class Main {
    
    public static void main(String[] args) {
      
	for(int i = 0; i < 10; i++) {
		try{ 
	        helloTpmon(true);
		} catch(Exception e){}
	
		//try{ 
	        //helloTpmon(false);
		//} catch(Exception e){}
	}

	for(int i = 0; i < 10; i++) {
		try{ 
	        helloTpmon(true);
		} catch(Exception e){}
	
		try{ 
	        helloTpmon(false);
		} catch(Exception e){}
	}

    }
    
    @OperationExecutionMonitoringProbe()
    public static void helloTpmon(boolean throwException) throws Exception{
        System.out.println("Hello World (look at your monitoring log ...)."+Thread.currentThread().getId()+" ");        
	if (throwException) {
		System.out.println("For test purposes, I will throw an exception now");
		throw new Exception("");
		}
    }    
}
