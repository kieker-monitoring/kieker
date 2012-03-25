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

package kieker.examples.userguide.ch3and4bookstore;

import kieker.common.record.AbstractMonitoringRecord;

public class MyResponseTimeRecord extends AbstractMonitoringRecord {

	private static final long serialVersionUID = 1775L;
	private final static String NA_VAL = "N/A";

	/* Attributes storing the actual monitoring data: */
	public volatile String className = MyResponseTimeRecord.NA_VAL;
	public volatile String methodName = MyResponseTimeRecord.NA_VAL;
	public volatile long responseTimeNanos = -1;

	public final void initFromArray(final Object[] values) { // NOPMD (store values)
		this.className = (String) values[0];
		this.methodName = (String) values[1];
		this.responseTimeNanos = (Long) values[2];
	}

	public final Object[] toArray() {
		return new Object[] { this.className, this.methodName, this.responseTimeNanos };
	}

	public Class<?>[] getValueTypes() {
		return new Class[] { String.class, String.class, long.class };
	}
}
