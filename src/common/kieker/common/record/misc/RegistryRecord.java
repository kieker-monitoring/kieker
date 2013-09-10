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

package kieker.common.record.misc;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Bits;

/**
 * Record used to associate Objects (typically Strings) with unique ids.
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class RegistryRecord<E> extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = 4566332478835872121L;
	private static final Class<?>[] TYPES = new Class<?>[] {
		int.class, // id
		Object.class, // object
	};

	private final int id;
	private final E object;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param id
	 *            The ID.
	 * @param object
	 *            The object.
	 */
	public RegistryRecord(final int id, final E object) {
		this.id = id;
		this.object = object;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	@SuppressWarnings("unchecked")
	public RegistryRecord(final Object[] values) { // NOPMD (direct store of E (usually String))
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.id = (Integer) values[0];
		this.object = (E) (values[1]);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.getId(), this.getObject() };
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] toByteArray() {
		final byte[] arr = new byte[4 + 8];
		Bits.putInt(arr, 0, this.getId());
		Bits.putString(arr, 8, this.getObject().toString()); // FIXME
		return arr;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromByteArray(final byte[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return this.id;
	}

	/**
	 * @return the object
	 */
	public final E getObject() {
		return this.object;
	}
}
