/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon.monitoringRecord;

import kieker.tpmon.annotation.TpmonInternal;

/**
 *
 * @author voorn
 */
public class KiekerDummyMonitoringRecord extends AbstractKiekerMonitoringRecord {

    private static final long serialVersionUID = 1133L;

    @TpmonInternal()
    public AbstractKiekerMonitoringRecord fromCSVRecord(String csvRecord) {
        return new KiekerDummyMonitoringRecord();
    }

    @TpmonInternal()
    public String toCSVRecord() {
        return new String("");
    }
}