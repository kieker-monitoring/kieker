package mySimpleKiekerExample;

import kieker.monitoring.core.MonitoringController;

public class CRM {

	public static void getOffers() {
		Catalog.getBook(false);
		
		/* Create a new record */
		MyRecord e = new MyRecord();
		e.component = "mySimpleKiekerExample.bookstoreTracing.Catalog";
		e.service = "getBook(false)";
		/* Make sure that the record will somehow be persisted. */
		MonitoringController.getInstance().newMonitoringRecord(e);
	}
}
