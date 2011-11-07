/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

/**
 * 
 * @author Jan Waller
 */
public final class HashRecord extends AbstractMonitoringRecord {
	private static final long serialVersionUID = 1L;
	private static final Class<?>[] TYPES = new Class<?>[] {
		int.class, // id
		Object.class, // object
	};

	private int id;
	private Object object;

	public HashRecord() {
		// do nothing
	}

	public HashRecord(final int id, final Object object) {
		this.id = id;
		this.object = object;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return this.object;
	}

	/**
	 * @param object
	 *            the object to set
	 */
	public void setObject(final Object object) {
		this.object = object;
	}

	@Override
	public void initFromArray(final Object[] values) {
		try {
			if (values.length != HashRecord.TYPES.length) {
				throw new IllegalArgumentException("Expecting array with " + HashRecord.TYPES.length + " elements but found " + values.length + ".");
			}
			this.setId((Integer) values[0]);
			this.setObject(values[1]);
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			throw new IllegalArgumentException("Failed to init record from array.", ex);
		}
	}

	@Override
	public Object[] toArray() {
		return new Object[] { this.getId(), this.getObject() };
	}

	@Override
	public final Class<?>[] getValueTypes() {
		return HashRecord.TYPES.clone();
	}
}
