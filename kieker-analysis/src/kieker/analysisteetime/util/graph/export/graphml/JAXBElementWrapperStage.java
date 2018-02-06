package kieker.analysisteetime.util.graph.export.graphml;

import javax.xml.bind.JAXBElement;

import org.graphdrawing.graphml.GraphmlType;
import org.graphdrawing.graphml.ObjectFactory;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class JAXBElementWrapperStage extends AbstractTransformation<GraphmlType, JAXBElement<GraphmlType>> {

	private final ObjectFactory objectFactory = new ObjectFactory();

	public JAXBElementWrapperStage() {
		super();
	}

	@Override
	protected void execute(final GraphmlType graphmlType) {

		final JAXBElement<GraphmlType> jaxbElement = this.objectFactory.createGraphml(graphmlType);
		this.getOutputPort().send(jaxbElement);

	}

}
