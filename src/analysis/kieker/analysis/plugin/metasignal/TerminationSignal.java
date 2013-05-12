package kieker.analysis.plugin.metasignal;

import kieker.analysis.plugin.reader.AbstractReaderPlugin;

public class TerminationSignal extends MetaSignal {

	private final AbstractReaderPlugin reader;

	public TerminationSignal(final AbstractReaderPlugin reader) {
		this.reader = reader;
	}

	public AbstractReaderPlugin getReader() {
		return this.reader;
	}

}
