/**
 * 
 */
package kieker.common.record.control;

import kieker.common.record.IRecord;

/**
 * Entity record for string maps used in communication.
 * 
 * @author Reiner Jung
 * 
 */
public class StringMapRecord implements IRecord {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2620492456189136404L;

	private final int key;
	private final String string;

	/**
	 * Create one StringMapRecord.
	 * 
	 * @param key
	 *            key for lookup
	 * @param string
	 *            string value
	 */
	public StringMapRecord(final int key, final String string) {
		super();
		this.key = key;
		this.string = string;
	}

	public int getKey() {
		return this.key;
	}

	public String getString() {
		return this.string;
	}

}
