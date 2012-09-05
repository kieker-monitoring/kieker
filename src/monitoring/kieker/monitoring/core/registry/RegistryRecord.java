/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.registry;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * Internal record type used exclusively by the monitoring subsystem.
 * 
 * @author Jan Waller
 */
public final class RegistryRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = 4566332478835872121L;
	private static final Class<?>[] TYPES = new Class<?>[] {
		int.class, // id
		Object.class, // object
	};

	private final int id;
	private final Object object;

	public RegistryRecord(final int id, final Object object) {
		this.id = id;
		this.object = object;
	}

	public RegistryRecord(final Object[] values) {
		final Object[] myValues = values.clone(); // to protect object from tampering
		AbstractMonitoringRecord.checkArray(myValues, TYPES);
		try {
			this.id = (Integer) myValues[0];
			this.object = myValues[1];
		} catch (final Exception ex) { // NOPMD NOCS (IllegalCatchCheck)
			throw new IllegalArgumentException("Failed to init record from array.", ex);
		}
	}

	public Object[] toArray() {
		return new Object[] { this.id, this.object };
	}

	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
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
