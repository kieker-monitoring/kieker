package kieker.common.record;

import java.io.Serializable;

/**
 * @author Andre van Hoorn
 */
public interface IMonitoringRecord extends Serializable {
	public long getLoggingTimestamp();
	public void setLoggingTimestamp(long timestamp);
	public void initFromArray(Object[] values);
	public Object[] toArray();
	public Class<?>[] getValueTypes();
}
