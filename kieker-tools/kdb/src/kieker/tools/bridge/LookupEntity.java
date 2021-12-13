/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.bridge;

import java.lang.reflect.Constructor;

import kieker.common.record.IMonitoringRecord;

/**
 * Lookup entity for a record id to a monitoring record. To avoid too many lookups and queries,
 * this map record contains a constructor reference and an array containing the field descriptions.
 *
 * @author Reiner Jung
 * @since 1.8
 *
 * @deprecated since 1.15 removed in 1.16 replaced by collector
 */
@Deprecated
public final class LookupEntity {
	/**
	 * List of parameter types for a given IMonitoringRecord.
	 */
	private final Class<?>[] parameterTypes;

	/**
	 * Constructor for an IMonitoringRecord class.
	 */
	private final Constructor<? extends IMonitoringRecord> constructor;

	/**
	 * Construct one new LookupEntry.
	 *
	 * @param constructor
	 *            constructor for a IMonitoringRecord class
	 * @param parameterTypes
	 *            monitoring record property type list
	 */
	public LookupEntity(final Constructor<? extends IMonitoringRecord> constructor, final Class<?>[] parameterTypes) { // NOPMD
		this.parameterTypes = parameterTypes;
		this.constructor = constructor;
	}

	public Class<?>[] getParameterTypes() {
		return this.parameterTypes; // NOPMD
	}

	public Constructor<? extends IMonitoringRecord> getConstructor() {
		return this.constructor;
	}

}
