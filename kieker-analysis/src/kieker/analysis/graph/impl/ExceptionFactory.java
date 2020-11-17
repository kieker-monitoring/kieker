/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.graph.impl;

/**
 * The ExceptionFactory provides standard exceptions for graphs.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
final class ExceptionFactory {

	private ExceptionFactory() {}

	// Graph related exceptions

	public static IllegalArgumentException vertexIdCanNotBeNull() {
		return new IllegalArgumentException("Vertex id can not be null");
	}

	public static IllegalArgumentException edgeIdCanNotBeNull() {
		return new IllegalArgumentException("Edge id can not be null");
	}

	public static IllegalArgumentException vertexWithIdAlreadyExists(final Object id) {
		return new IllegalArgumentException("Vertex with id already exists: " + id);
	}

	public static IllegalArgumentException edgeWithIdAlreadyExists(final Object id) {
		return new IllegalArgumentException("Edge with id already exists: " + id);
	}

	public static IllegalStateException vertexWithIdDoesNotExist(final Object id) {
		return new IllegalStateException("Vertex with id does not exist: " + id);
	}

	public static IllegalStateException edgeWithIdDoesNotExist(final Object id) {
		return new IllegalStateException("Edge with id does not exist: " + id);
	}

	public static IllegalStateException verticesAreNotInSameGraph(final Object id1, final Object id2) {
		return new IllegalStateException("Vertices with ids are not in the same graph: " + id1 + ", " + id2);
	}

	public static IllegalArgumentException bothIsNotSupported() {
		return new IllegalArgumentException("A direction of BOTH is not supported");
	}

	// Element related exceptions

	public static IllegalArgumentException propertyKeyIsReserved(final String key) {
		return new IllegalArgumentException("Property key is reserved for all elements: " + key);
	}

	public static IllegalArgumentException propertyKeyCanNotBeEmpty() {
		return new IllegalArgumentException("Property key can not be the empty string");
	}

	public static IllegalArgumentException propertyKeyCanNotBeNull() {
		return new IllegalArgumentException("Property key can not be null");
	}

	public static IllegalArgumentException propertyValueCanNotBeNull() {
		return new IllegalArgumentException("Property value can not be null");
	}

}
