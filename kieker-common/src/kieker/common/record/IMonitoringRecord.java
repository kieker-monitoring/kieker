/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.BufferOverflowException;

import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

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
 *
 * @since 1.2
 */
public interface IMonitoringRecord extends Serializable, Comparable<IMonitoringRecord> {

	/**
	 * Delivers the current timestamp of the record.
	 *
	 * @return The timestamp.
	 *
	 * @since 1.2
	 */
	public long getLoggingTimestamp();

	/**
	 * Sets the logging timestamp to a new value.
	 *
	 * @param timestamp
	 *            The new timestamp for the record.
	 *
	 * @since 1.2
	 */
	public void setLoggingTimestamp(long timestamp);

	/**
	 * Creates a string representation of this record.<br/>
	 * <br/>
	 *
	 * This method should not be used for serialization purposes since this is not the purpose of Object's toString method.
	 *
	 * @return A (human readable) string of this record.
	 *
	 * @since 1.5
	 */
	@Override
	public String toString();

	/**
	 * This method should deliver an array containing the content of the record. It should be possible to convert this array later into a record again.
	 *
	 * @return An array with the values of the record.
	 *
	 * @since 1.2
	 */
	public Object[] toArray();

	/**
	 * Registers the string attributes of the record at the given <code>stringRegistry</code>.
	 *
	 * @param stringRegistry
	 *            string registry
	 *
	 * @since 1.11
	 * @deprecated since 1.13 (to be removed in 1.14): Is unnecessary when using the new, serializer-based output.
	 */
	@Deprecated
	public void registerStrings(final IRegistry<String> stringRegistry);

	/**
	 * This method serializes this record using the given serializer.
	 *
	 * @param serializer
	 *            The serializer to serialize the record with. *
	 * @throws BufferOverflowException
	 *             If the underlying buffer has insufficient capacity to store this record
	 * @since 1.13
	 */
	public void serialize(IValueSerializer serializer) throws BufferOverflowException;

	/**
	 * This method should initialize the record based on the given values. The array should be one of those resulting from a call to {@link #toArray()}.
	 *
	 * @param values
	 *            The values for the record.
	 *
	 * @since 1.2
	 */
	public void initFromArray(Object[] values);

	/**
	 * This method delivers an array with the classes of the single values for the record.
	 *
	 * @return The types of the values. This returned array should be treated readonly.
	 *
	 * @see #toArray()
	 *
	 * @since 1.2
	 */
	public Class<?>[] getValueTypes();

	/**
	 * This method delivers an array containing the value names of the record.
	 *
	 * @return The types of the values. This returned array should be treated readonly.
	 *
	 * @see #toArray()
	 *
	 * @since 1.2
	 */
	public String[] getValueNames();

	/**
	 * This method should deliver the size of a binary representation of this record.
	 *
	 * @return The size.
	 *
	 * @since 1.8
	 * @deprecated since 1.13 (to be removed in 1.14) With the introduction of value serializers, this method has become obsolete.
	 */
	@Deprecated
	public int getSize();

	/**
	 * Any record that implements this interface has to conform to certain specifications.
	 *
	 * <p>
	 * These records can use final fields and thus provide better performance.
	 * </p>
	 *
	 * <ul>
	 * <li>a constructor accepting a single Object[] as argument.
	 * <li>a <code>public static final Class&lt;?&gt;[] TYPES</code> specifying the types of the records, usually returned via {@link #getValueTypes()}.
	 * <li>the {@link #initFromArray(Object[])} method does not have to be implemented
	 * </ul>
	 *
	 * @since 1.5
	 */
	public static interface Factory { // NOCS (name)
		// empty marker interface
	}

	/**
	 * Any record that implements this interface has to conform to certain specifications.
	 *
	 * <p>
	 * These records can use final fields and thus provide better performance.
	 * </p>
	 *
	 * <ul>
	 * <li>a constructor accepting a Deserializer as arguments possibly throwing BufferUnderflowException.
	 * <li>a <code>public static final int SIZE</code> specifying the binary size of the record, usually returned via {@link #getSize()}.
	 * </ul>
	 *
	 * @since 1.8
	 */
	public static interface BinaryFactory { // NOCS (name)
		// empty marker interface
	}
}
