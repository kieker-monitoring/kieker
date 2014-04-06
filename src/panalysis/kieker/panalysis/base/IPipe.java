/***************************************************************************
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
public interface IPipe {

	/**
	 * @since 1.10
	 */
	void put(Object token);

	/**
	 * @since 1.10
	 */
	void putMultiple(List<?> items);

	// Since the concurrent execution does not base on locks, it is difficult to define a blocking take. Since there is no definition of blocking in the sequential
	// execution, we do not define a blocking take in this interface
	/**
	 * @since 1.10
	 */
	@Deprecated
	Object take();

	/**
	 * @return and removes the next token if the pipe is not empty, otherwise <code>null</code>
	 * 
	 * @since 1.10
	 */
	Object tryTake();

	/**
	 * @since 1.10
	 * @return but does not removes the next token if the pipe is not empty, otherwise <code>null</code>
	 */
	Object read();

	/**
	 * @since 1.10
	 */
	List<?> tryTakeMultiple(int numItemsToTake);

	// Let this uncommented for documentation purpose:<br>
	// Do not provide a method to check for emptiness, since the state from EMPTY to NON-EMPTY can change between check and access (stale state)

	// boolean isEmpty();

	/**
	 * @since 1.10
	 */
	<O extends Enum<O>, I extends Enum<I>> void connect(ISource<O> sourceStage, O sourcePort, ISink<I> targetStage, I targetPort);

	void fireSignalClosing();

}
