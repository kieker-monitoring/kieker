package bookstoreApplication;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class Bookstore {

    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(this.catalog);

    private final static MonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public void searchBook() {
        /* 1.) Call the Catalog component's getBook() method
         *     and log its entry and exit timestamp using Kieker. */
        final long tin = MonitoringController.currentTimeNanos();
        this.catalog.getBook(false);
        final long tout = MonitoringController.currentTimeNanos();
        final OperationExecutionRecord e = 
                new OperationExecutionRecord(
                Catalog.class.getName(), "getBook(..)",
                tin, tout);
        Bookstore.MONITORING_CONTROLLER.newMonitoringRecord(e);

        /* 2.) Call the CRM catalog's getOffers() method
         *     (without monitoring). */
        this.crm.getOffers();
    }
}
