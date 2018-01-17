package kieker.analysisteetime.util.graph.export.dot;

import kieker.analysisteetime.util.graph.mapping.DirectPropertyMapper;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotNodeAttribute;

/**
 * A {@link DotExportConfiguration} that maps the property <em>label</em> of edges
 * and vertices to the <em>label</em> attribute of the corresponding nodes and
 * edges of the dot graph.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class SimpleDotExportConfiguration extends DotExportConfiguration {

	public SimpleDotExportConfiguration() {
		super();
		super.nodeAttributes.put(DotNodeAttribute.LABEL, DirectPropertyMapper.of("label"));
		super.edgeAttributes.put(DotEdgeAttribute.LABEL, DirectPropertyMapper.of("label"));
	}

}
