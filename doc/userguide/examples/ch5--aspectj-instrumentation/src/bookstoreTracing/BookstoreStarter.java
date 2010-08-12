package bookstoreTracing;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class BookstoreStarter {

	@OperationExecutionMonitoringProbe
    public static void main(String[] args) {
        Bookstore bookstore = new Bookstore();
        for (int i = 0; i < 1; i++) {
            System.out.println("Bookstore.main: Starting request " + i);
            bookstore.searchBook();
        }
    }
}
