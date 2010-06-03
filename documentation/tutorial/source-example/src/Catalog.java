package mySimpleKiekerExample.bookstoreTracing;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

public class Catalog {
    public static void getBook(boolean complexQuery) {
        if (complexQuery) {
            Bookstore.waitabit(20);
        } else {
	    long tin = System.currentTimeMillis();

            Bookstore.waitabit(2);

	    long tout = System.currentTimeMillis();

	    KiekerExecutionRecord e = KiekerExecutionRecord.getInstance("mySimpleKiekerExample.bookstoreTracing.Bookstore", "waitabit(2)", 0, tin, tout);
	    TpmonController.getInstance().logMonitoringRecord(e);
        }
    }
}
