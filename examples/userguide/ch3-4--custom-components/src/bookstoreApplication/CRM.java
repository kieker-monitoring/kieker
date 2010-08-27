package bookstoreApplication;

import kieker.monitoring.core.MonitoringController;

public class CRM {

    private final Catalog catalog;
    private final static MonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public CRM(final Catalog catalog) {
        this.catalog = catalog;
    }

    public void getOffers() {
        /* Invoke catalog.getBook() and monitor response time */
        long tin = MONITORING_CONTROLLER.currentTimeNanos();
        catalog.getBook(false);
        long tout = MONITORING_CONTROLLER.currentTimeNanos();
        /* Create a new record and set values */
        MyResponseTimeRecord e = new MyResponseTimeRecord();
        e.className = "mySimpleKiekerExample.bookstoreTracing.Catalog";
        e.methodName = "getBook(..)";
        e.responseTimeNanos = tout - tin;
        /* Pass the record to the monitoring controller */
        MONITORING_CONTROLLER.newMonitoringRecord(e);
    }
}
