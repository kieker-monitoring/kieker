package kieker.tools.traceAnalysis.filter.visualization.util.graphml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.graphdrawing.graphml.xmlns.GraphmlType;
import org.graphdrawing.graphml.xmlns.ObjectFactory;

public class GraphmlWriter extends JAXBElementWriter<GraphmlType> {

	private static final String GRAPHML_XML_NAMESPACE = "org.graphdrawing.graphml.xmlns";
	private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

	public GraphmlWriter() {
		super(GRAPHML_XML_NAMESPACE);
	}

	public void write(final GraphmlType graphml, final String filename) throws JAXBException {
		final JAXBElement<GraphmlType> jaxbObject = OBJECT_FACTORY.createGraphml(graphml);
		this.write(jaxbObject, filename);
	}
}
