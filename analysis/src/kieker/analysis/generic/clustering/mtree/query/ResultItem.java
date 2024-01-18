/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.clustering.mtree.query;

/**
 * The type of the results for nearest-neighbor queries.
 *
 * @param <T>
 *            data type of the result item
 *
 * @author Eduardo R. D'Avila
 * @since 2.0.0
 */
public final class ResultItem<T> {

	/** A nearest-neighbor. */
	private final T data;

	/**
	 * The distance from the nearest-neighbor to the query data object
	 * parameter.
	 */
	private final double distance;

	public ResultItem(final T data, final double distance) {
		this.data = data;
		this.distance = distance;
	}

	public T getData() {
		return this.data;
	}

	public double getDistance() {
		return this.distance;
	}
}
