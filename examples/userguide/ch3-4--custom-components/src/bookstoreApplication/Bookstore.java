package bookstoreApplication;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

public class Bookstore {

	private final Catalog catalog = new Catalog();
	private final CRM crm = new CRM(this.catalog);
	private final static IMonitoringController MONITORING_CONTROLLER =
			MonitoringController.getInstance();

	public void searchBook() {
		{ /* 1. Invoke catalog.getBook() and monitor response time */
			final long tin =
					Bookstore.MONITORING_CONTROLLER.getTimeSource().getTime();
			this.catalog.getBook(false);
			final long tout =
					Bookstore.MONITORING_CONTROLLER.getTimeSource().getTime();
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
