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
        final long tin = MonitoringController.currentTimeNanos();
        this.catalog.getBook(false);
        final long tout = MonitoringController.currentTimeNanos();
        /* Create a new record and set values */
        final MyResponseTimeRecord e = new MyResponseTimeRecord();
        e.className = "mySimpleKiekerExample.bookstoreTracing.Catalog";
        e.methodName = "getBook(..)";
        e.responseTimeNanos = tout - tin;
        /* Pass the record to the monitoring controller */
        CRM.MONITORING_CONTROLLER.newMonitoringRecord(e);
    }
}
