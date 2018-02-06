package kieker.analysisteetime.util.graph.util;

/**
 * Enumeration for file extensions.
 *
 * @author Sören Henning
 *
 */
public enum FileExtension {

	GRAPHML("graphml"), DOT("dot");

	private final String fileExtension;

	private FileExtension(final String fileExtension) {
		this.fileExtension = fileExtension;
	}

	@Override
	public String toString() {
		return this.fileExtension;
	}

}
