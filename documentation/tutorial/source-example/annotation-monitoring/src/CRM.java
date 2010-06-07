package mySimpleKiekerExample.bookstoreTracing;

import kieker.tpmon.annotation.TpmonExecutionMonitoringProbe;

public class CRM {

    @TpmonExecutionMonitoringProbe()
    public static void getOffers() {
      Catalog.getBook(false);
    }
}
