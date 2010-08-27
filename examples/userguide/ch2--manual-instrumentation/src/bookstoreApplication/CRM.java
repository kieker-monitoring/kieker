package bookstoreApplication;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class CRM {    
    private final Catalog catalog;

    private final static MonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public CRM(final Catalog catalog) {
        this.catalog = catalog;
    }

    public void getOffers() {
        /* Call the Catalog component's getBook() method
         * and log its entry and exit timestamp using Kieker. */
        long tin = MONITORING_CONTROLLER.currentTimeNanos();
        catalog.getBook(false);
        long tout = MONITORING_CONTROLLER.currentTimeNanos();
        OperationExecutionRecord e =
                new OperationExecutionRecord(
                Catalog.class.getName(), "getBook()",
                tin, tout);
        MONITORING_CONTROLLER.newMonitoringRecord(e);
    }
}
