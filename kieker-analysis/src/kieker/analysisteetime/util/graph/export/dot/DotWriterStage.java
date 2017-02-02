package kieker.analysisteetime.util.graph.export.dot;

import java.io.Writer;
import java.util.function.Function;

import kieker.analysisteetime.util.graph.Graph;

import teetime.framework.AbstractConsumerStage;

/**
 * This stage writes a {@link Graph} to given {@link Writer} in the dot file format. The writer
 * can be configured by a {@link Function}, which maps the graph to a writer. For example, this
 * allows one write the graph to a file with the name of the graph. For a non generic writer
 * simply use something like {@code x -> new MyWriter()}.
 *
 * In addition, this stage can be configured by a {@link DotExportConfiguration}, which specifies
 * how attributes (for graphs, vertices and edges) are mapped to the dot graph.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DotWriterStage extends AbstractConsumerStage<Graph> {

	protected final Function<Graph, Writer> writerMapper;

	protected final DotExportConfiguration exportConfiguration;

	public DotWriterStage(final Function<Graph, Writer> writerMapper) {
		super();
		this.writerMapper = writerMapper;
		this.exportConfiguration = new SimpleDotExportConfiguration();
	}

	public DotWriterStage(final Function<Graph, Writer> writerMapper, final DotExportConfiguration exportConfiguration) {
		super();
		this.writerMapper = writerMapper;
		this.exportConfiguration = exportConfiguration;
	}

	@Override
	protected final void execute(final Graph graph) {
		final DotExporter dotExporter = new DotExporter(graph, this.writerMapper.apply(graph), this.exportConfiguration);
		dotExporter.transform();
	}

}
