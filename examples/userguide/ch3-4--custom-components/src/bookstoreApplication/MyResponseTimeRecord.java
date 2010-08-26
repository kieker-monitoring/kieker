package bookstoreTracing;

import kieker.common.record.AbstractMonitoringRecord;

public class MyResponseTimeRecord extends AbstractMonitoringRecord {

    private static final long serialVersionUID = 1775L;
    private final static String NULL_VAL = "N/A";

    /* Attributes storing the actual monitoring data: */
    public String className = NULL_VAL;
    public String methodName = NULL_VAL;
    public long responseTimeNanos = -1;

    @Override
    public final void initFromArray(Object[] values) {
            this.className = (String) values[0];
            this.methodName = (String) values[1];
            this.responseTimeNanos = (Long) values[2];
    }

    @Override
    public final Object[] toArray() {
        return new Object[]{this.className, this.methodName, this.responseTimeNanos};
    }

    @Override
    public Class[] getValueTypes() {
        return new Class[]{String.class, String.class, long.class};
    }
}
