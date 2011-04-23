package kieker.test.monitoring.aspectJ.loadTimeWeaving.bookstoreWithoutAnnotation;

/**
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component tpmon.
 *
 * @author Matthias Rohr
 * History:
 * 2008/08/30: Created based on Catalog.java without Annotations
 *
 */


public class CatalogWA {
    
    public static void getBook(boolean complexQuery){
    	if (complexQuery) {
		//System.out.println("  complex query");
		BookstoreWA.waitabit(20);
	}
	else 	{			
		//System.out.println("  simple query"); 
		BookstoreWA.waitabit(2);	
	}

    }
   
}
