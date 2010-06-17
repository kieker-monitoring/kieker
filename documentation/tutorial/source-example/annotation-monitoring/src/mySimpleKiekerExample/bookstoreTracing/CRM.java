package mySimpleKiekerExample.bookstoreTracing;

import kieker.monitoring.annotation.TpmonExecutionMonitoringProbe;

public class CRM {
	@TpmonExecutionMonitoringProbe
	public static void getOffers() {
		Catalog.getBook(false);
	}
}
