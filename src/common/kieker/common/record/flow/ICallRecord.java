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
 * Interface for all flow records that describe operation calls.
 * 
 * All call records have a <code>calleeClassSignature</code> and an <code>calleeOperationSignature</code> field of type <code>String</code>.
 * 
 * @author Jan Waller
 * 
 * @since 1.6
 */
public interface ICallRecord extends IOperationRecord {

	/**
	 * @see #getClassSignature()
	 * 
	 * @since 1.6
	 */
	public abstract String getCallerClassSignature();

	/**
	 * @see #getOperationSignature()
	 * 
	 * @since 1.6
	 */
	public abstract String getCallerOperationSignature();

	/**
	 * @since 1.6
	 */
	public abstract String getCalleeClassSignature();

	/**
	 * @since 1.6
	 */
	public abstract String getCalleeOperationSignature();

	/**
	 * @since 1.6
	 */
	public abstract boolean callsReferencedOperationOf(final IOperationRecord record);
}
