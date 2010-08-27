package bookstoreApplication;

import kieker.monitoring.core.MonitoringController;

public class Bookstore {

    private final Catalog catalog = new Catalog();
    private final CRM crm = new CRM(catalog);
    private final static MonitoringController MONITORING_CONTROLLER =
            MonitoringController.getInstance();

    public void searchBook() {
        {   /* 1. Invoke catalog.getBook() and monitor response time */
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

        { /* 2. Invoke crm.getOffers() (without monitoring) */
            crm.getOffers();
        }
    }
}
