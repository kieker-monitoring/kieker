package mySimpleKiekerExample.bookstoreTracing;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class Catalog {
	@OperationExecutionMonitoringProbe
	public static void getBook(boolean complexQuery) {
		if (complexQuery) {
			Bookstore.waitabit(20);
		} else {
			Bookstore.waitabit(2);
		}
	}
}
