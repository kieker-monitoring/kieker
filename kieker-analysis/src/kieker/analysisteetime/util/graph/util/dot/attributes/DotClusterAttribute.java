package kieker.analysisteetime.util.graph.util.dot.attributes;

public enum DotClusterAttribute {

	LABEL("label");

	private final String attribute;

	private DotClusterAttribute(final String attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return this.attribute;
	}

}
