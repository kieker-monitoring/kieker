package kieker.test.monitoring.aspectJ.compileTimeWeaving.bookstore;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component. 
 *
 * @author Matthias Rohr
 * History:
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */


public class Catalog {
    
    @OperationExecutionMonitoringProbe()
    public static void getBook(boolean complexQuery){
    	if (complexQuery) {
		//System.out.println("  complex query");
		Bookstore.waitabit(20);
	}
	else 	{			
		//System.out.println("  simple query"); 
		Bookstore.waitabit(2);	
	}

    }
   
}
