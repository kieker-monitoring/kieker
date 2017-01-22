package kieker.analysisteetime.util.graph.util.dot.attributes;

public enum DotNodeAttribute {

	LABEL("label"), SHAPE("shape");

	private final String attribute;

	private DotNodeAttribute(final String attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return this.attribute;
	}

}
