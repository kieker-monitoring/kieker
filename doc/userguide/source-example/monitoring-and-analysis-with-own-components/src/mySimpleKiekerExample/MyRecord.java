package mySimpleKiekerExample;

import kieker.common.record.AbstractMonitoringRecord;

public class MyRecord extends AbstractMonitoringRecord {
	private static final long serialVersionUID = 1775L;
	/**
	 * Used to identify the type of CSV records This record type has a fixed
	 * value of 0
	 */
	private static int numRecordFields = 3;
	public long rt = -1;
	public String component = null;
	public String service = null;

	public final void initFromArray(Object[] values)
			throws IllegalArgumentException {
		try {
			if (values.length != MyRecord.numRecordFields) {
				throw new IllegalArgumentException("Expecting vector with "
						+ MyRecord.numRecordFields
						+ " elements but found:" + values.length);
			}

			this.component = (String) values[0];
			this.service = (String) values[1];
			this.rt = (Long) values[2];

		} catch (Exception exc) {
			throw new IllegalArgumentException("Failed to init", exc);
		}

		return;
	}

	public final Object[] toArray() {
		return new Object[] {
				(this.component == null) ? "NULLCOMPONENT" : this.component,
				(this.service == null) ? "NULLSERVICE" : this.service, this.rt };
	}

	public Class[] getValueTypes() {
		return new Class[] { String.class, // component
				String.class, // service
				long.class // rt
		};
	}
}
