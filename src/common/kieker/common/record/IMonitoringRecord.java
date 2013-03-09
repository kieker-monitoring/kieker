/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.common.record;

import java.io.Serializable;

/**
 * All Kieker monitoring records have to implement this minimal interface.
 * 
 * <p>
 * <b>Warning:</b> Objects within records should not contain ';', '\n', or '\r' in their respective toString() representation.
 * </p>
 * 
 * @see Factory
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public interface IMonitoringRecord extends Serializable, Comparable<IMonitoringRecord> {

	/**
	 * Delivers the current timestamp of the record.
	 * 
	 * @return The timestamp.
	 */
	public long getLoggingTimestamp();

	/**
	 * Sets the logging timestamp to a new value.
	 * 
	 * @param timestamp
	 *            The new timestamp for the record.
	 */
	public void setLoggingTimestamp(long timestamp);

	/**
	 * This method should deliver an array containing the content of the record. It should be possible to convert this array later into a record again.
	 * 
	 * @return An array with the values of the record.
	 */
	public Object[] toArray();

	/**
	 * Creates a string representation of this record.<br/>
	 * <br/>
	 * 
	 * This method should not be used for serialization purposes since this is not the purpose of Object's toString method.
	 * 
	 * @return A (human readable) string of this record.
	 */
	public String toString();

	/**
	 * This method should initialize the record based on the given values. The array should be one of those resulting from a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public void initFromArray(Object[] values);

	/**
	 * This method should deliver an array with the classes of the single values for the record.
	 * 
	 * @return The types of the values.
	 * 
	 * @see #toArray()
	 */
	public Class<?>[] getValueTypes();

	/**
	 * Any record that implements this interface has to conform to certain specifications.
	 * 
	 * <p>
	 * These records can use final fields and thus provide better performance.
	 * </p>
	 * 
	 * <ul>
	 * <li>a constructor accepting a single Object[] as argument.
	 * <li>a <code>private static final Class&lt;?&gt;[] TYPES</code> specifying the types of the records, usually returned via
	 * {@link #IMonitoringRecord.getValueTypes()}.
	 * <li>the {@link #IMonitoringRecord.initFromArray(Object[] values)} method does not have to be implemented
	 * </ul>
	 */
	public static interface Factory { // NOCS (name)
		// empty marker interface
	}
}
