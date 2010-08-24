package bookstoreTracing;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class CRM {    
    private final Catalog catalog;

    public CRM(final Catalog catalog) {
        this.catalog = catalog;
    }

    @OperationExecutionMonitoringProbe
    public void getOffers() {
        catalog.getBook(false);
    }
}
