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
	MyRecord e = new MyRecord();
	e.component = "mySimpleKiekerExample.bookstoreTracing.Catalog";
	e.service = "getBook(false)";
	/* Make sure that the record will somehow be persisted. */
	MonitoringController.getInstance().newMonitoringRecord(e);
    }
}
