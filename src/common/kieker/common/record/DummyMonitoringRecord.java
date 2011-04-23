package kieker.common.record;

/**
 * @author Andre van Hoorn
 */
public class DummyMonitoringRecord extends AbstractMonitoringRecord {

    private static final long serialVersionUID = 11767633L;

    public static AbstractMonitoringRecord getInstance() {
        return new DummyMonitoringRecord();
    }

    @Override
	public void initFromArray(final Object[] values) {
    }

    @Override
	public Object[] toArray() {
       return new Object[]{};
    }

    @Override
	public Class<?>[] getValueTypes() {
        return new Class[]{};
    }
}