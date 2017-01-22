package kieker.analysisteetime.util.graph.export.dot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.mapping.SimpleFileNameMapper;
import kieker.analysisteetime.util.graph.util.FileExtension;

public class DotFileWriterStage extends DotWriterStage {

	public DotFileWriterStage(final Function<Graph, String> fileNameMapper) {
		this(fileNameMapper, new SimpleDotExportConfiguration());
	}

	public DotFileWriterStage(final Function<Graph, String> fileNameMapper, final DotExportConfiguration exportConfiguration) {
		super(fileNameMapper.andThen(fileName -> {
			try {
				return new FileWriter(fileName);
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}), exportConfiguration);
	}

	public DotFileWriterStage(final String outputDirectory) {
		this(outputDirectory, new SimpleDotExportConfiguration());
	}

	public DotFileWriterStage(final String outputDirectory, final DotExportConfiguration exportConfiguration) {
		this(new SimpleFileNameMapper(outputDirectory, FileExtension.DOT), exportConfiguration);
	}

}
