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

package kieker.common.record.internal;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public final class NullRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = -9106270301270791630L;

	private static final Class<?>[] TYPES = {};

	public NullRecord() {
		// nothing to do
	}

	public NullRecord(final Object[] values) { // NOPMD (UnusedFormalParameter)
		// nothing to do
	}

	@Override
	public Object[] toArray() {
		return new Object[] {};
	}

	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?>[] getValueTypes() {
		return NullRecord.TYPES.clone();
	}
}
