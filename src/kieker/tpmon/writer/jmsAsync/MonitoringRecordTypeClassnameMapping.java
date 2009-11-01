/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon.writer.jmsAsync;

import java.io.Serializable;

/**
 *
 * @author Andre van Hoorn
 */
public class MonitoringRecordTypeClassnameMapping implements Serializable {
    private static final long serialVersionUID = 5477L;
    public final int id;
    public final String classname;

    private MonitoringRecordTypeClassnameMapping(){
        this.id = -1;
        this.classname = null;
    }

    public MonitoringRecordTypeClassnameMapping(int id, String classname){
        this.id = id;
        this.classname = classname;
    }
}
