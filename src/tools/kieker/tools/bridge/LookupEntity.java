/***************************************************************************
 * Copyright 2013 by
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
package kieker.tools.bridge;

import java.lang.reflect.Constructor;

import kieker.common.record.IMonitoringRecord;

/**
 * @author rju
 *
 */
public final class LookupEntity {
	// CHECKSTYLE:OFF for speedup reasons these properties are public
	/**
	 * List of parameter types for a given IMonitoringRecord.
	 */
	public final Class<?>[] parameterTypes;
	/**
	 * Constructor for an IMonitoringRecord class.
	 */
	public final Constructor<? extends IMonitoringRecord> constructor;
	// CHECKSTYLE:ON
	
	/**
	 * Construct one new LookupEntry.
	 * 
	 * @param constructor constructor for a IMonitoringRecord class
	 * @param parameterTypes monitoring record property type list
	 */
	public LookupEntity(final Constructor<? extends IMonitoringRecord> constructor, final Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
		this.constructor = constructor;
	}
}
