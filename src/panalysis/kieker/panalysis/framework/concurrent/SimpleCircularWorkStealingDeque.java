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
package kieker.panalysis.framework.concurrent;

import java.util.concurrent.ConcurrentLinkedDeque;

import de.chw.concurrent.CircularWorkStealingDeque;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class SimpleCircularWorkStealingDeque<T> extends CircularWorkStealingDeque<T> {

	ConcurrentLinkedDeque<T> deque = new ConcurrentLinkedDeque<T>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.chw.concurrent.CircularWorkStealingDeque#pushBottom(java.lang.Object)
	 */
	@Override
	public void pushBottom(final T o) {
		this.deque.addLast(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.chw.concurrent.CircularWorkStealingDeque#popBottom()
	 */
	@Override
	public T popBottom() {
		return this.deque.pollLast();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.chw.concurrent.CircularWorkStealingDeque#steal()
	 */
	@Override
	public T steal() {
		return this.deque.pollFirst();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.chw.concurrent.CircularWorkStealingDeque#readBottom()
	 */
	@Override
	public T readBottom() {
		return this.deque.peekLast();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.chw.concurrent.CircularWorkStealingDeque#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.deque.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.chw.concurrent.CircularWorkStealingDeque#toString()
	 */
	@Override
	public String toString() {
		return this.deque.toString();
	}
}
