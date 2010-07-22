package mySimpleKiekerExampleManual;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class Bookstore {

	static int numberOfRequests = 5;

	public static void startBookstoreRequests() {
		for (int i = 0; i < numberOfRequests; i++) {
			System.out.println("Bookstore.main: Starting request " + i);
			Bookstore newBookstore = new Bookstore();
			newBookstore.startBookstore();
		}
	}

	public void startBookstore() {
		/* Call searchBook() and remember the runtime of the call. */
		long tin = System.currentTimeMillis();
		Bookstore.searchBook();
		long tout = System.currentTimeMillis();

		/* Create a new record with the remembered values. */
		OperationExecutionRecord e = new OperationExecutionRecord(
				"mySimpleKiekerExampleManual.Bookstore", "searchBook()", 0,
				tin, tout);
		/* Make sure that the record will somehow be persisted. */
		MonitoringController.getInstance().newMonitoringRecord(e);
	}

	public static void searchBook() {
		/* Call getBook() and remember the runtime of the call. */
		long tin = System.currentTimeMillis();
		Catalog.getBook(false);
		long tout = System.currentTimeMillis();

		/* Create a new record with the remembered values. */
		OperationExecutionRecord e = new OperationExecutionRecord(
				"mySimpleKiekerExampleManual.Catalog", "getBook()", 0,
				tin, tout);
		/* Make sure that the record will somehow be persisted. */
		MonitoringController.getInstance().newMonitoringRecord(e);

		/* Call getOffers() and remember the runtime of the call. */
		tin = System.currentTimeMillis();
		CRM.getOffers();
		tout = System.currentTimeMillis();

		/* Create a new record with the remembered values. */
		e = new OperationExecutionRecord(
				"mySimpleKiekerExampleManual.CRM", "getOffers()", 0,
				tin, tout);
		/* Make sure that the record will somehow be persisted. */
		MonitoringController.getInstance().newMonitoringRecord(e);
	}

	public static void waitabit(long waittime) {
		if (waittime > 0) {
			try {
				Thread.sleep(waittime);
			} catch (Exception e) {
			}
		}
	}
}