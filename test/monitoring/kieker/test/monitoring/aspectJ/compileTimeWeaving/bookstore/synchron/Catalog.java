package kieker.test.monitoring.aspectJ.compileTimeWeaving.bookstore.synchron;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component.
 * 
 * THIS VARIANT IS nearly identical TO kieker.tests.compileTimeWeaving.bookstore.Catalog.
 * Method getBook is synchronized here. This allows to test the (negative) performance 
 * influence of synchronized method invocation.
 *
 * @author Matthias Rohr
 * History:
 * 2008/20/10: Created this variant based on kieker.tests.compileTimeWeaving.bookstore.Catalog
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */
public class Catalog {
    
    @OperationExecutionMonitoringProbe()
    public static synchronized void getBook(boolean complexQuery){
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
