package kieker.analysisteetime.util.graph.export.graphml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.function.Function;

import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.mapping.SimpleFileNameMapper;
import kieker.analysisteetime.util.graph.util.FileExtension;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class GraphMLFileWriterStage extends GraphMLWriterStage {

	public GraphMLFileWriterStage(final Function<Graph, String> fileNameMapper) {
		super(fileNameMapper.andThen(fileName -> {
			try {
				return new FileOutputStream(fileName);
			} catch (final FileNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}));
	}

	public GraphMLFileWriterStage(final String outputDirectory) {
		this(new SimpleFileNameMapper(outputDirectory, FileExtension.GRAPHML));
	}

}
