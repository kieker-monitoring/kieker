package bookstoreApplication;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

public class Bookstore {

    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(this.catalog);

    private final static IMonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public void searchBook() {
        /* 1.) Call the Catalog component's getBook() method
         *     and log its entry and exit timestamp using Kieker. */
        final long tin = Bookstore.MONITORING_CONTROLLER.getTimeSource().getTime();
        this.catalog.getBook(false);
        final long tout = Bookstore.MONITORING_CONTROLLER.getTimeSource().getTime();
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
