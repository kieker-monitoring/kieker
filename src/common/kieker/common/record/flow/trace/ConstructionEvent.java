/***************************************************************************
 * Copyright 2012 by
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

package kieker.common.record.flow.trace;

import kieker.common.record.flow.IObjectRecord;

/**
 * @author Jan Waller
 */
public class ConstructionEvent extends AbstractTraceEvent implements IObjectRecord {
	private static final long serialVersionUID = -7484030624827825815L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		long.class, // TraceEvent.traceId
		int.class, // TraceEvent.orderIndex
		String.class, // className
		int.class, // objectId
	};

	/**
	 * This field should not be exported, because it makes little sense to have no associated class
	 */
	private static final String NO_CLASSNAME = "<no-classname>";

	private final String classSignature;
	private final int objectId;

	public ConstructionEvent(final long timestamp, final long traceId, final int orderIndex, final String className, final int objectId) {
		super(timestamp, traceId, orderIndex);
		this.classSignature = (className == null) ? NO_CLASSNAME : className; // NOCS
		this.objectId = objectId;
	}

	public ConstructionEvent(final Object[] values) { // NOPMD (values stored directly)
		super(values, TYPES); // values[0..2]
		this.classSignature = (String) values[3];
		this.objectId = (Integer) values[4];
	}

	protected ConstructionEvent(final Object[] values, final Class<?>[] types) { // NOPMD (values stored directly)
		super(values, types); // values[0..2]
		this.classSignature = (String) values[3];
		this.objectId = (Integer) values[4];
	}

	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getTraceId(), this.getOrderIndex(), this.classSignature, this.objectId, };
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	public final String getClassSignature() {
		return this.classSignature;
	}

	public final int getObjectId() {
		return this.objectId;
	}
}
