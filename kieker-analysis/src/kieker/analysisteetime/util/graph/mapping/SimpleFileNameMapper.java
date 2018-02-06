package kieker.analysisteetime.util.graph.mapping;

import java.util.function.Function;

import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.util.FileExtension;

/**
 * This function maps a graph to a file name with the pattern:
 * output directory + graph name + file extension
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class SimpleFileNameMapper implements Function<Graph, String> {

	private final String outputDirectory;
	private final FileExtension fileExtension;

	public SimpleFileNameMapper(final String outputDirectory, final FileExtension fileExtension) {
		this.outputDirectory = outputDirectory;
		this.fileExtension = fileExtension;
	}

	@Override
	public String apply(final Graph graph) {
		return this.outputDirectory + '/' + graph.getName() + '.' + this.fileExtension;
	}

}
