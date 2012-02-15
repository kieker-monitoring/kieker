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

package kieker.common.record.misc;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author Jan Waller
 */
public final class HashRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = 4566332478835872121L;
	private static final Class<?>[] TYPES = new Class<?>[] {
		int.class, // id
		Object.class, // object
	};

	private final int id;
	private final Object object;

	public HashRecord(final int id, final Object object) {
		this.id = id;
		this.object = object;
	}

	public HashRecord(final Object[] values) {
		final Object[] myValues = values.clone(); // to protect object from tampering
		AbstractMonitoringRecord.checkArray(myValues, HashRecord.TYPES);
		try {
			this.id = (Integer) myValues[0];
			this.object = myValues[1];
		} catch (final Exception ex) { // NOCS (IllegalCatchCheck) // NOPMD
			throw new IllegalArgumentException("Failed to init record from array.", ex);
		}
	}

	@Override
	public Object[] toArray() {
		return new Object[] { this.id, this.object };
	}

	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?>[] getValueTypes() {
		return HashRecord.TYPES.clone();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return this.object;
	}
}
