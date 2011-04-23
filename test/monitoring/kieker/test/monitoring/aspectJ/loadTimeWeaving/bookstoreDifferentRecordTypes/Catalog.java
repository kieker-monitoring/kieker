package kieker.test.monitoring.aspectJ.loadTimeWeaving.bookstoreDifferentRecordTypes;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;
import kieker.monitoring.probe.manual.BranchingProbe;


/**
 * A simple test and demonstration scenario for Kieker's 
 * monitoring component.
 *
 * @author Matthias Rohr, Andre van Hoorn
 * History:
 * 2009/06/23: Adapted for "different record type test"
 * 2008/01/09: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007-04-18: Initial version
 *
 */
public class Catalog {
    @OperationExecutionMonitoringProbe()
    public static void getBook(boolean complexQuery) {
        if (complexQuery) {
            BranchingProbe.monitorBranch(1,0);
            Bookstore.waitabit(20);
        } else {
            BranchingProbe.monitorBranch(1,1);
            Bookstore.waitabit(2000); //
        }
    }
}
