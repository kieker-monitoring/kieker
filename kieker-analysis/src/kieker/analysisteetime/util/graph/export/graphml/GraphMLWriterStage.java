package kieker.analysisteetime.util.graph.export.graphml;

import java.io.OutputStream;
import java.util.function.Function;

import org.graphdrawing.graphml.GraphmlType;

import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.stage.FunctionStage;
import kieker.analysisteetime.util.stage.JAXBMarshalStage;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class GraphMLWriterStage extends CompositeStage {

	private final InputPort<Graph> inputPort;

	public GraphMLWriterStage(final Function<Graph, OutputStream> outputStreamMapper) {

		final Distributor<Graph> graphDistributor = new Distributor<>(new CopyByReferenceStrategy());
		final GraphMLTransformationStage graphMLTransformer = new GraphMLTransformationStage();
		final JAXBElementWrapperStage jaxbElementWrapper = new JAXBElementWrapperStage();
		final JAXBMarshalStage<GraphmlType> jaxbMarshaller = new JAXBMarshalStage<>(GraphmlType.class);
		final FunctionStage<Graph, OutputStream> outputStreamMapperStage = new FunctionStage<>(outputStreamMapper);

		this.inputPort = graphDistributor.getInputPort();

		super.connectPorts(graphDistributor.getNewOutputPort(), graphMLTransformer.getInputPort());
		super.connectPorts(graphMLTransformer.getOutputPort(), jaxbElementWrapper.getInputPort());
		super.connectPorts(jaxbElementWrapper.getOutputPort(), jaxbMarshaller.getInputPort1());
		super.connectPorts(graphDistributor.getNewOutputPort(), outputStreamMapperStage.getInputPort());
		super.connectPorts(outputStreamMapperStage.getOutputPort(), jaxbMarshaller.getInputPort2());

	}

	public InputPort<Graph> getInputPort() {
		return this.inputPort;
	}

}
