package mySimpleKiekerExample.bookstoreTracing;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

public class Catalog {
    public static void getBook(boolean complexQuery) {
        if (complexQuery) {
            Bookstore.waitabit(20);
        } else {
	    long tin = TpmonController.getInstance().getTime();
	    Bookstore.waitabit(2);
	    long tout = TpmonController.getInstance().getTime();


	    KiekerExecutionRecord e = KiekerExecutionRecord.getInstance("mySimpleKiekerExample.bookstoreTracing.Bookstore", "waitabit(long)", 1, tin, tout);
	    TpmonController.getInstance().logMonitoringRecord(e);
        }
    }
}
