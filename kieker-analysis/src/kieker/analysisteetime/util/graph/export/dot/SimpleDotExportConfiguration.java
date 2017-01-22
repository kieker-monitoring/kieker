package kieker.analysisteetime.util.graph.export.dot;

import kieker.analysisteetime.util.graph.Edge;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.mapping.DirectPropertyMapper;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotNodeAttribute;

public class SimpleDotExportConfiguration extends DotExportConfiguration {

	public SimpleDotExportConfiguration() {
		super();
		this.getNodeAttributes().put(DotNodeAttribute.LABEL, new DirectPropertyMapper<Vertex>("label"));
		this.getEdgeAttributes().put(DotEdgeAttribute.LABEL, new DirectPropertyMapper<Edge>("label"));
	}

}
