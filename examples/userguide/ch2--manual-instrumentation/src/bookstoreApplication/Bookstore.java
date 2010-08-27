package bookstoreApplication;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class Bookstore {

    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(catalog);

    private final static MonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public void searchBook() {
        /* 1.) Call the Catalog component's getBook() method
         *     and log its entry and exit timestamp using Kieker. */
        long tin = MONITORING_CONTROLLER.currentTimeNanos();
        catalog.getBook(false);
        long tout = MONITORING_CONTROLLER.currentTimeNanos();
        OperationExecutionRecord e = 
                new OperationExecutionRecord(
                Catalog.class.getName(), "getBook(..)",
                tin, tout);
        MONITORING_CONTROLLER.newMonitoringRecord(e);

        /* 2.) Call the CRM catalog's getOffers() method
         *     (without monitoring). */
        crm.getOffers();
    }
}
