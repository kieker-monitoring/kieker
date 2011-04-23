package kieker.test.monitoring.aspectJ.compileTimeWeaving.bookstore.synchron;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component tpmon. See the kieker tutorial 
 * for more information 
 * (http://www.matthias-rohr.com/kieker/tutorial.html)
 * 
 * THIS VARIANT IS IDENTICAL TO kieker.tests.compileTimeWeaving.bookstore.Catalog,
 * but it uses a different Catalog that has a synchronized method. This allows to
 * test the (negative) performance influence of synchronized method invocation.
 *
 * @author Matthias Rohr
 * History:
 * 2008/20/10: Created this variant based on kieker.tests.compileTimeWeaving.bookstore.Catalog
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */

public class CRM {
 
    /**
     * This method will be monitored, since it has an annotation.
     */
    @OperationExecutionMonitoringProbe()
    public static void getOffers(){
	Catalog.getBook(true);
    }
}
