/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.util;

/**
 * Plain Java tuple class. Usable as key value in maps which are composed of multiple entries.
 *
 * @param <F>
 *            first tuple element type
 * @param <S>
 *            second tuple element type
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class Tuple<F, S> {

	private final F first;
	private final S second;

	public Tuple(final F first, final S second) {
		this.first = first;
		this.second = second;
	}

	public F getFirst() {
		return this.first;
	}

	public S getSecond() {
		return this.second;
	}

	@Override
	public boolean equals(final Object value) {
		if (value != null) {
			if (value instanceof Tuple) { // NOPMD CollapsibleIfStatements (no they cannot)
				final Tuple<?, ?> key = (Tuple<?, ?>) value;
				if ((this.first == null) && (key.getFirst() == null)) {
					if ((this.second == null) && (key.getSecond() == null)) {
						return true;
					} else if ((this.second != null) && (key.getSecond() != null)) {
						return this.second.equals(key.getSecond());
					}
				} else if ((this.first != null) && (key.getFirst() != null)) {
					if ((this.second == null) && (key.getSecond() == null)) {
						return this.first.equals(key.getFirst());
					} else if ((this.second != null) && (key.getSecond() != null)) {
						return this.first.equals(key.getFirst()) && this.second.equals(key.getSecond());
					}
				}
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (this.first == null ? 0 : this.first.hashCode()) ^ (this.second == null ? 0 : this.second.hashCode()); // NOCS NOPMD
	}
}
