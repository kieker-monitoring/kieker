package kieker.test.monitoring.aspectJ.loadTimeWeaving.exceptions;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * Experimental test for Kieker in combination with exceptions.
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
	        helloKieker(true);
		} catch(Exception e){}
	
		//try{ 
	        //helloKieker(false);
		//} catch(Exception e){}
	}

	for(int i = 0; i < 10; i++) {
		try{ 
	        helloKieker(true);
		} catch(Exception e){}
	
		try{ 
	        helloKieker(false);
		} catch(Exception e){}
	}

    }
    
    @OperationExecutionMonitoringProbe()
    public static void helloKieker(boolean throwException) throws Exception{
        System.out.println("Hello World (look at your monitoring log ...)."+Thread.currentThread().getId()+" ");        
	if (throwException) {
		System.out.println("For test purposes, I will throw an exception now");
		throw new Exception("");
		}
    }    
}
