package kieker.analysisteetime.util.graph.export.dot;

import java.io.Writer;
import java.util.function.Function;

import kieker.analysisteetime.util.graph.Graph;

import teetime.framework.AbstractConsumerStage;

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
		DotExporter dotExporter = new DotExporter(graph, writerMapper.apply(graph), exportConfiguration);
		dotExporter.transform();
	}

}
