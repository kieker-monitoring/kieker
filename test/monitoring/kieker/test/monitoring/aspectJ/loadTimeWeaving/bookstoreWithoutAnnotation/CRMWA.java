package kieker.test.monitoring.aspectJ.loadTimeWeaving.bookstoreWithoutAnnotation;


/**
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component. 
 *
 * @author Matthias Rohr
 * History:
 * 2008/08/30: Created based on CRM.java without Annotations
 *
 */

public class CRMWA {
 
    public static void getOffers(){
	CatalogWA.getBook(true);
    }
}
