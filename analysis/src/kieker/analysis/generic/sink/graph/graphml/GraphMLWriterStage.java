/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.analysis.generic.sink.graph.graphml;

import java.io.OutputStream;
import java.util.function.Function;

import org.graphdrawing.graphml.GraphmlType;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.util.stage.FunctionStage;
import kieker.analysis.util.stage.JAXBMarshalStage;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

/**
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphMLWriterStage<N extends INode, E extends IEdge> extends CompositeStage {

	private final InputPort<IGraph<N, E>> inputPort;

	public GraphMLWriterStage(final Function<IGraph<N, E>, OutputStream> outputStreamMapper) {

		final Distributor<IGraph<N, E>> graphDistributor = new Distributor<>(new CopyByReferenceStrategy());
		final GraphMLTransformationStage<N, E> graphMLTransformer = new GraphMLTransformationStage<>();
		final JAXBElementWrapperStage jaxbElementWrapper = new JAXBElementWrapperStage();
		final JAXBMarshalStage<GraphmlType> jaxbMarshaller = new JAXBMarshalStage<>(GraphmlType.class);
		final FunctionStage<IGraph<N, E>, OutputStream> outputStreamMapperStage = new FunctionStage<>(outputStreamMapper);

		this.inputPort = graphDistributor.getInputPort();

		super.connectPorts(graphDistributor.getNewOutputPort(), graphMLTransformer.getInputPort());
		super.connectPorts(graphMLTransformer.getOutputPort(), jaxbElementWrapper.getInputPort());
		super.connectPorts(jaxbElementWrapper.getOutputPort(), jaxbMarshaller.getInputPort1());
		super.connectPorts(graphDistributor.getNewOutputPort(), outputStreamMapperStage.getInputPort());
		super.connectPorts(outputStreamMapperStage.getOutputPort(), jaxbMarshaller.getInputPort2());

	}

	public InputPort<IGraph<N, E>> getInputPort() {
		return this.inputPort;
	}

}
