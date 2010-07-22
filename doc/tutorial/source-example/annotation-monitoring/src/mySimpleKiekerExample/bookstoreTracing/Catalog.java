package mySimpleKiekerExample.bookstoreTracing;

import kieker.monitoring.annotation.TpmonExecutionMonitoringProbe;

public class Catalog {
	@TpmonExecutionMonitoringProbe
	public static void getBook(boolean complexQuery) {
		if (complexQuery) {
			Bookstore.waitabit(20);
		} else {
			Bookstore.waitabit(2);
		}
	}
}
