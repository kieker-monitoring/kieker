package bookstoreTracing;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class Bookstore {

    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(catalog);

    @OperationExecutionMonitoringProbe
    public void searchBook() {
        catalog.getBook(false);
        crm.getOffers();
    }
}
