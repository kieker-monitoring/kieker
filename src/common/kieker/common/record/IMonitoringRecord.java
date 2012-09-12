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

package kieker.common.record;

import java.io.Serializable;

/**
 * @author Andre van Hoorn, Jan Waller
 */
public interface IMonitoringRecord extends Serializable, Comparable<IMonitoringRecord> {

	public long getLoggingTimestamp();

	public void setLoggingTimestamp(long timestamp);

	public Object[] toArray();

	/**
	 * Creates a string representation of this record.
	 * 
	 * This method should not be used for serialization purposes since this is not the purpose of Object's toString method.
	 */
	public String toString();

	public void initFromArray(Object[] values);

	public Class<?>[] getValueTypes();

	/**
	 * Any record that implements this interface has to conform to certain specifications.
	 * 
	 * <ul>
	 * <li>a constructor accepting a single Object[] as argument.
	 * <li>a <code>private static final Class&lt;?&gt;[] TYPES</code> specifying the types of the records, usually returned via {@link #getValueTypes()}.
	 * </ul>
	 */
	public static interface Factory { // NOCS (name)
		// empty marker interface
	}
}
