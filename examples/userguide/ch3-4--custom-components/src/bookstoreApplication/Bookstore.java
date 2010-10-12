package bookstoreApplication;

import kieker.monitoring.core.MonitoringController;

public class Bookstore {

    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(this.catalog);
    private final static MonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public void searchBook() {
        {   /* 1. Invoke catalog.getBook() and monitor response time */
            final long tin = MonitoringController.currentTimeNanos();
            this.catalog.getBook(false);
            final long tout = MonitoringController.currentTimeNanos();
            /* Create a new record and set values */
            final MyResponseTimeRecord e = new MyResponseTimeRecord();
            e.className = "mySimpleKiekerExample.bookstoreTracing.Catalog";
            e.methodName = "getBook(..)";
            e.responseTimeNanos = tout - tin;
            /* Pass the record to the monitoring controller */
            Bookstore.MONITORING_CONTROLLER.newMonitoringRecord(e);
        }

        { /* 2. Invoke crm.getOffers() (without monitoring) */
            this.crm.getOffers();
        }
    }
}
