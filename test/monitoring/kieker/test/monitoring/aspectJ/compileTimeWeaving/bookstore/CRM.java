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

public class CRM {
 
    /**
     * This method will be monitored, since it has an annotation.
     */
    @OperationExecutionMonitoringProbe()
    public static void getOffers(){
	Catalog.getBook(true);
    }
}
