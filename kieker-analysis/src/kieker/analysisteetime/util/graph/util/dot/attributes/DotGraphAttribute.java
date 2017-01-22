package kieker.analysisteetime.util.graph.util.dot.attributes;

public enum DotGraphAttribute {

	LABEL("label"), RANKDIR("rankdir");

	private final String attribute;

	private DotGraphAttribute(final String attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return this.attribute;
	}

}
