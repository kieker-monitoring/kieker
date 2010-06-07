package mySimpleKiekerExample.bookstoreTracing;

import kieker.tpmon.core.TpmonController;
import kieker.common.record.OperationExecutionRecord;

public class CRM {

    public static void getOffers(){
	    long tin = TpmonController.getInstance().getTime();
	    Catalog.getBook(false);
	    long tout = TpmonController.getInstance().getTime();

	    OperationExecutionRecord e = new OperationExecutionRecord("mySimpleKiekerExample.bookstoreTracing.Catalog", "getBook(false)", "sessionID", 0, tin, tout, "vnName", 3, 2);
	    TpmonController.getInstance().newMonitoringRecord(e);
    }
}
