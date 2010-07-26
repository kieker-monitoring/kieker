package mySimpleKiekerExampleManual;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class Bookstore {

    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(catalog);

    private final static MonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public void searchBook() {
        /* 1.) Call the Catalog component's getBook() method
         *     and log its entry and exit using Kieker. */
        long tin = MONITORING_CONTROLLER.getTime();
        catalog.getBook(false);
        long tout = MONITORING_CONTROLLER.getTime();
        OperationExecutionRecord e = 
                new OperationExecutionRecord(
                "mySimpleKiekerExampleManual.Catalog", "getBook()", 0,
                tin, tout);
        MONITORING_CONTROLLER.newMonitoringRecord(e);

        /* 2.) Call the CRM catalog's getOffers() method
         *     (without monitoring). */
        crm.getOffers();
    }
}
