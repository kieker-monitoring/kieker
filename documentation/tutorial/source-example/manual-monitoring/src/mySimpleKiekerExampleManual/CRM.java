package mySimpleKiekerExample.bookstoreTracing;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class CRM {

	public static void getOffers() {
		/* Call getTime() and remember the runtime of the call. */
		long tin = MonitoringController.getInstance().getTime();
		Catalog.getBook(false);
		long tout = MonitoringController.getInstance().getTime();

		/* Create a new record with the remembered values. */
		OperationExecutionRecord e = new OperationExecutionRecord(
				"mySimpleKiekerExample.bookstoreTracing.Catalog",
				"getBook(false)", "sessionID", 0, tin, tout, "vnName", 3, 2);
		/* Make sure that the record will somehow be persisted. */
		MonitoringController.getInstance().newMonitoringRecord(e);
	}
}
