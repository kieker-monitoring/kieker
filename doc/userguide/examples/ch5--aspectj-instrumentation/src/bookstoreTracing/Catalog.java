package bookstoreTracing;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

public class Catalog {

    @OperationExecutionMonitoringProbe
    public void getBook(final boolean complexQuery) {
        try {
            if (complexQuery) {
                Thread.sleep(20);
            } else {
                Thread.sleep(2);
            }
        } catch (InterruptedException ex) {}
    }
}
