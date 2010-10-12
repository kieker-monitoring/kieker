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
        final long tin = MonitoringController.currentTimeNanos();
        this.catalog.getBook(false);
        final long tout = MonitoringController.currentTimeNanos();
        final OperationExecutionRecord e =
                new OperationExecutionRecord(
                Catalog.class.getName(), "getBook()",
                tin, tout);
        CRM.MONITORING_CONTROLLER.newMonitoringRecord(e);
    }
}
