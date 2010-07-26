package mySimpleKiekerExample.bookstoreTracing;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class CRM {
	@OperationExecutionMonitoringProbe
	public static void getOffers() {
		Catalog.getBook(false);
	}
}
