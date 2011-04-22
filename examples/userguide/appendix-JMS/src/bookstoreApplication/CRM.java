package bookstoreApplication;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

public class CRM {    
    private final Catalog catalog;

    private final static IMonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public CRM(final Catalog catalog) {
        this.catalog = catalog;
    }

    public void getOffers() {
        /* Call the Catalog component's getBook() method
         * and log its entry and exit timestamp using Kieker. */
        final long tin = CRM.MONITORING_CONTROLLER.getTimeSource().getTime();
        this.catalog.getBook(false);
        final long tout = CRM.MONITORING_CONTROLLER.getTimeSource().getTime();
        final OperationExecutionRecord e =
                new OperationExecutionRecord(
                Catalog.class.getName(), "getBook()",
                tin, tout);
        CRM.MONITORING_CONTROLLER.newMonitoringRecord(e);
    }
}
