/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.timeseries;

/**
 * 
 * @author Tom Frotscher
 * @since 1.10
 * 
 * @param <T>
 *            The type of the buffer.
 */
public interface ITimeSeriesPointsBuffer<T> {

	/**
	 * Add an Object to the tail of the Buffer.
	 * 
	 * @since 1.10
	 * @param o
	 *            The Value to add
	 * @return
	 *         Returns true if Object is added successfully
	 * 
	 * @since 1.9
	 */
	public boolean add(T o);

	/**
	 * Removes a Value from the buffer in FIFO order.
	 * 
	 * @since 1.10
	 * @return
	 *         Returns the removed Object
	 * 
	 * @since 1.9
	 */
	public T remove();

	/**
	 * Returns the current size of the buffer.
	 * 
	 * @since 1.10
	 * @return Returned Buffersize
	 * 
	 * @since 1.9
	 */
	public int getSize();

}
