/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
 * Interface for all flow records that describe operation calls on objects.
 * 
 * All call records have a <code>calleeObjectId</code> field of type <code>int</code>.
 * 
 * @author Jan Waller
 * 
 * @since 1.6
 */
public interface ICallObjectRecord extends ICallRecord, IObjectRecord {

	/**
	 * @return The ID of the caller object.
	 * 
	 * @see #getObjectId()
	 * 
	 * @since 1.6
	 */
	public abstract int getCallerObjectId();

	/**
	 * @return The ID of the callee object.
	 * 
	 * @since 1.6
	 */
	public abstract int getCalleeObjectId();

}
