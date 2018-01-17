package kieker.analysisteetime.util.graph.util.dot.attributes;

/**
 * A enumeration of possible attributes for clusters in dot graphs.
 *
 * <strong>Currently only the attributes we are using are implemented, but the enumeration
 * can be extended at will.</strong>
 *
 * The whole set of attributes including further documentation can be found here:
 * {@link http://www.graphviz.org/doc/info/attrs.html}
 *
 * @author S�ren Henning
 *
 * @since 1.13
 */
public enum DotClusterAttribute {

	LABEL("label"), STYLE("style"), FILLCOLOR("fillcolor");

	private final String attribute;

	private DotClusterAttribute(final String attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return this.attribute;
	}

}
