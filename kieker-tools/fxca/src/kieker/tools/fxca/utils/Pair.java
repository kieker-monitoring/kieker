/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.fxca.utils;

import java.util.Comparator;

/**
 * Class for a tuple.
 *
 * @author Henning Schnoor
 *
 * @param <F>
 *            first element type
 * @param <S>
 *            second element type
 *
 * @since 1.3.0
 */
public class Pair<F, S> {

	public F first;
	public S second;

	public Pair(final F first, final S second) {
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
	public int hashCode() {
		return this.getFirst().hashCode() + this.getSecond().hashCode();
	}

	@Override
	public boolean equals(final Object compareto) {
		if (this == compareto) {
			return true;
		}
		if (compareto == null) {
			return false;
		}
		if (!(compareto instanceof Pair<?, ?>)) {
			return false;
		}

		final Pair<?, ?> comparePair = (Pair<?, ?>) compareto;

		return (comparePair.first.getClass().equals(this.first.getClass()))
				&& (comparePair.second.getClass().equals(this.second.getClass()))
				&& (comparePair.first.equals(this.first)) && (comparePair.second.equals(this.second));
	}

	public static <T extends Comparable<T>, S extends Comparable<S>> Comparator<Pair<S, T>> getComparatorFirstSecond() {
		return (pair1, pair2) -> {
			final int result = pair1.first.compareTo(pair2.first);
			if (result != 0) {
				return result;
			}
			return pair1.second.compareTo(pair2.second);
		};
	}

	@Override
	public String toString() {
		return this.getFirst() + " -> " + this.getSecond();
	}
}
