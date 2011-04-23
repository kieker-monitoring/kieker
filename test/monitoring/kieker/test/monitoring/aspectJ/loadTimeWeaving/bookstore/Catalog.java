package kieker.test.monitoring.aspectJ.loadTimeWeaving.bookstore;


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
    
    public static void getBook(boolean complexQuery){
    	if (complexQuery) {
//		System.out.println("  complex query "+Thread.currentThread().getName());
		Bookstore.waitabit(20);
	}
	else 	{			
//		System.out.println("  simple query "+Thread.currentThread().getName());
		Bookstore.waitabit(2);	
	}

    }
   
}
