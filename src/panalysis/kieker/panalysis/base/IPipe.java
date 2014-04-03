/***********************************import java.util.List;
 ****************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis.base;

import java.util.List;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public interface IPipe<T> {

	/**
	 * @since 1.10
	 */
	void put(T record);

	/**
	 * @since 1.10
	 */
	T take();

	/**
	 * @return and removes the next record if the pipe is not empty, otherwise <code>null</code>
	 * 
	 * @since 1.10
	 */
	T tryTake();

	/**
	 * @return <code>true</code> if the pipe contains no element, otherwise <code>false</code>.<br>
	 *         <i>This method is used to find the next non-empty port of a stage with multiple ports.<i>
	 * 
	 * @since 1.10
	 */
	boolean isEmpty();

	/**
	 * @since 1.10
	 */
	List<T> tryTakeMultiple(int numItemsToTake);

	/**
	 * @since 1.10
	 */
	void putMultiple(List<T> items);

}
