package kieker.analysisteetime.util.graph.util.dot.attributes;

/**
 * A enumeration of possible attributes for edges in dot graphs.
 *
 * <strong>Currently only the attributes we are using are implemented, but the enumeration
 * can be extended at will.</strong>
 *
 * The whole set of attributes including further documentation can be found here:
 * {@link http://www.graphviz.org/doc/info/attrs.html}
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
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
