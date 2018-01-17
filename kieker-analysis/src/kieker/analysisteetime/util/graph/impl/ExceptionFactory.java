package kieker.analysisteetime.util.graph.impl;

/**
 * The ExceptionFactory provides standard exceptions for graphs.
 *
 * @author Sören Henning
 */
class ExceptionFactory {

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
