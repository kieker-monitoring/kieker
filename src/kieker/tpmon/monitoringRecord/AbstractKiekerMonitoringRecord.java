/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.tpmon.monitoringRecord;

import java.io.Serializable;
import kieker.tpmon.annotation.TpmonInternal;


/**
 *
 * @author voorn
 */
public abstract class AbstractKiekerMonitoringRecord implements Serializable {

    @TpmonInternal()
    public abstract AbstractKiekerMonitoringRecord fromCSVRecord(String csvRecord);

    @TpmonInternal()
    public abstract String toCSVRecord();
}
