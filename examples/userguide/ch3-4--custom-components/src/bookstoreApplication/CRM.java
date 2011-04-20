package bookstoreApplication;

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
		/* Invoke catalog.getBook() and monitor response time */
		final long tin = CRM.MONITORING_CONTROLLER.getTimeSource().getTime();
		this.catalog.getBook(false);
		final long tout = CRM.MONITORING_CONTROLLER.getTimeSource().getTime();
		/* Create a new record and set values */
		final MyResponseTimeRecord e = new MyResponseTimeRecord();
		e.className = "mySimpleKiekerExample.bookstoreTracing.Catalog";
		e.methodName = "getBook(..)";
		e.responseTimeNanos = tout - tin;
		/* Pass the record to the monitoring controller */
		CRM.MONITORING_CONTROLLER.newMonitoringRecord(e);
	}
}
