package bookstoreTracing;

import kieker.monitoring.core.MonitoringController;

public class CRM {
    private final Catalog catalog;

    public CRM(final Catalog catalog) {
        this.catalog = catalog;
    }


    public void getOffers() {
        catalog.getBook(false);
		
	/* Create a new record */
	MyResponseTimeRecord e = new MyResponseTimeRecord();
	e.className = "mySimpleKiekerExample.bookstoreTracing.Catalog";
	e.methodName = "getBook(false)";
	/* Make sure that the record will somehow be persisted. */
	MonitoringController.getInstance().newMonitoringRecord(e);
    }
}
