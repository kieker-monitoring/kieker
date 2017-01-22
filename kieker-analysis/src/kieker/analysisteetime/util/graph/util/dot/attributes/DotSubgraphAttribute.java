package kieker.analysisteetime.util.graph.util.dot.attributes;

public enum DotSubgraphAttribute {

	;

	private final String attribute;

	private DotSubgraphAttribute(final String attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return this.attribute;
	}

}
