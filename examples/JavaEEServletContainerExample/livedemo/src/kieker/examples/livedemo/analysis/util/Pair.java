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

package kieker.examples.livedemo.analysis.util;

/**
 * 
 * @author Bjoern Weissenfels
 * 
 * @since 1.10
 * 
 * @param <V>
 *            The first type.
 * @param <W>
 *            The second type.
 */
public class Pair<V, W> {

	private V first;
	private W last;

	public Pair(final V first, final W last) {
		this.first = first;
		this.last = last;
	}

	public V getFirst() {
		return this.first;
	}

	public void setFirst(final V first) {
		this.first = first;
	}

	public W getLast() {
		return this.last;
	}

	public void setLast(final W last) {
		this.last = last;
	}
}
