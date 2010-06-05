package mySimpleKiekerExample.bookstoreTracing;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

public class CRM {

    public static void getOffers(){
	    long tin = TpmonController.getInstance().getTime();
	    Catalog.getBook(false);
	    long tout = TpmonController.getInstance().getTime();

	    KiekerExecutionRecord e = KiekerExecutionRecord.getInstance("mySimpleKiekerExample.bookstoreTracing.Catalog", "getBook(false)", "sessionID", 0, tin, tout, "vnName", 3, 2);
	    TpmonController.getInstance().logMonitoringRecord(e);
    }
}
