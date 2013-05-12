package kieker.analysis.plugin.metasignal;

import kieker.analysis.plugin.reader.AbstractReaderPlugin;

public class InitializationSignal extends MetaSignal {

	private final AbstractReaderPlugin reader;

	public InitializationSignal(final AbstractReaderPlugin reader) {
		this.reader = reader;
	}

	public AbstractReaderPlugin getReader() {
		return this.reader;
	}

}
