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

package kieker.analysis.generic.sink.graph.dot;

import java.io.Writer;
import java.util.function.Function;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;

import teetime.framework.AbstractConsumerStage;

/**
 * This stage writes a {@link IGraph} to given {@link Writer} in the dot file format. The writer
 * can be configured by a {@link Function}, which maps the graph to a writer. For example, this
 * allows one write the graph to a file with the name of the graph. For a non generic writer
 * simply use something like {@code x -> new MyWriter()}.
 *
 * In addition, this stage can be configured by a {@link DotExportMapper}, which specifies
 * how attributes (for graphs, vertices and edges) are mapped to the dot graph.
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DotWriterStage<N extends INode, E extends IEdge> extends AbstractConsumerStage<IGraph<N, E>> {

	protected final Function<IGraph<N, E>, Writer> writerMapper;

	protected final DotExportMapper<N, E> exportConfiguration;

	public DotWriterStage(final Function<IGraph<N, E>, Writer> writerMapper) {
		super();
		this.writerMapper = writerMapper;
		this.exportConfiguration = new SimpleDotExportConfiguration<>();
	}

	public DotWriterStage(final Function<IGraph<N, E>, Writer> writerMapper, final DotExportMapper<N, E> exportConfiguration) {
		super();
		this.writerMapper = writerMapper;
		this.exportConfiguration = exportConfiguration;
	}

	@Override
	protected final void execute(final IGraph<N, E> graph) {
		final DotTransformer<N, E> dotExporter = new DotTransformer<>(graph, this.writerMapper.apply(graph), this.exportConfiguration);
		dotExporter.transform();
	}

}
