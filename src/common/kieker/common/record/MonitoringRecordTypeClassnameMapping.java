package kieker.common.record;

import java.io.Serializable;

/**
 *
 * @author Andre van Hoorn
 */
public class MonitoringRecordTypeClassnameMapping implements Serializable {
    private static final long serialVersionUID = 5477L;
    public final int typeId;
    public final String classname;

    public MonitoringRecordTypeClassnameMapping(int id, String classname){
        this.typeId = id;
        this.classname = classname;
    }
}
