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

package kieker.common.record.flow;

/**
 * @author Jan Waller
 */
public class ConstructionEvent extends AbstractEvent {
	private static final long serialVersionUID = 4260562921517437040L;
	private static final Class<?>[] TYPES = {
		long.class, // Event.timestamp
		String.class, // className
		int.class, // objectId
	};

	/**
	 * This field should not be exported, because it makes little sense to have no associated class
	 */
	private static final String NO_CLASSNAME = "<no-classname>";

	private final String className;
	private final int objectId;

	public ConstructionEvent(final long timestamp, final String className, final int objectId) {
		super(timestamp);
		this.className = (className == null) ? NO_CLASSNAME : className; // NOCS
		this.objectId = objectId;
	}

	public ConstructionEvent(final Object[] values) { // NOPMD (values stored directly)
		super(values, TYPES); // values[0]
		this.className = (String) values[1];
		this.objectId = (Integer) values[2];
	}

	protected ConstructionEvent(final Object[] values, final Class<?>[] types) { // NOPMD (values stored directly)
		super(values, types); // values[0]
		this.className = (String) values[1];
		this.objectId = (Integer) values[2];
	}

	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.className, this.objectId, };
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	public final String getClassName() {
		return this.className;
	}

	public final int getObjectId() {
		return this.objectId;
	}
}
