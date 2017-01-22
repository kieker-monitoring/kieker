package kieker.analysisteetime.util.graph.util.dot.attributes;

public enum DotEdgeAttribute {

	LABEL("label"), ARROWHEAD("arrowhead"), ARROWSIZE("arrowsize"), ARROWTAIL("arrowtail");

	private final String attribute;

	private DotEdgeAttribute(final String attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return this.attribute;
	}

}
