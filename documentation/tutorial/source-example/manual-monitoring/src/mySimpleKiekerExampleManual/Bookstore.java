package mySimpleKiekerExampleManual;

import java.util.Vector;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

public class Bookstore extends Thread {

	static int numberOfRequests = 1;
	static int interRequestTime = 5;
	static final Vector<Bookstore> bookstoreScenarios = new Vector<Bookstore>();

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < numberOfRequests; i++) {
			System.out.println("Bookstore.main: Starting request " + i);
			Bookstore newBookstore = new Bookstore();
			bookstoreScenarios.add(newBookstore);
			newBookstore.start();
			Bookstore.waitabit(interRequestTime);
		}
		System.out
				.println("Bookstore.main: Finished with starting all requests.");
		System.out.println("Bookstore.main: Waiting for threads to terminate");
		synchronized (bookstoreScenarios) {
			while (!bookstoreScenarios.isEmpty()) {
				bookstoreScenarios.wait();
			}
		}
		System.exit(0);
	}

	@Override
	public void run() {
		/* Call searchBook() and remember the runtime of the call. */
		long tin = MonitoringController.getInstance().getTime();
		Bookstore.searchBook();
		long tout = MonitoringController.getInstance().getTime();

		/* Create a new record with the remembered values. */
		OperationExecutionRecord e = new OperationExecutionRecord(
				"mySimpleKiekerExampleManual.Bookstore",
				"searchBook()", "sessionID", 0, tin, tout, "vnName", 0, 0);
		/* Make sure that the record will somehow be persisted. */
		MonitoringController.getInstance().newMonitoringRecord(e);

		synchronized (bookstoreScenarios) {
			bookstoreScenarios.remove(this);
			bookstoreScenarios.notify();
		}
	}

	public static void searchBook() {
		for (int i = 0; i < 1; i++) {
			/* Call getBook() and remember the runtime of the call. */
			long tin = MonitoringController.getInstance().getTime();
			Catalog.getBook(false);
			long tout = MonitoringController.getInstance().getTime();

			/* Create a new record with the remembered values. */
			OperationExecutionRecord e = new OperationExecutionRecord(
					"mySimpleKiekerExampleManual.Catalog",
					"getBook(false)", "sessionID", 0, tin, tout, "vnName", 1, 1);
			/* Make sure that the record will somehow be persisted. */
			MonitoringController.getInstance().newMonitoringRecord(e);

			/* Call getOffers() and remember the runtime of the call. */
			tin = MonitoringController.getInstance().getTime();
			CRM.getOffers();
			tout = MonitoringController.getInstance().getTime();

			/* Create a new record with the remembered values. */
			e = new OperationExecutionRecord(
					"mySimpleKiekerExampleManual.CRM",
					"getOffers()", "sessionID", 0, tin, tout, "vnName", 2, 1);
			/* Make sure that the record will somehow be persisted. */
			MonitoringController.getInstance().newMonitoringRecord(e);
		}
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