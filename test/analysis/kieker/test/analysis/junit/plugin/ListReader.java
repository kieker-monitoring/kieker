package kieker.test.analysis.junit.plugin;

import java.util.ArrayList;
import java.util.List;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;

/**
 * Helper class that reads records added using the method {@link #addAllRecords(List)}.
 * 
 * @author Andre van Hoorn
 * 
 */
@Plugin(outputPorts = { @OutputPort(name = ListReader.OUTPUT_PORT_NAME, eventTypes = { Object.class }) })
public class ListReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";

	private final List<Object> objects = new ArrayList<Object>();

	public ListReader(final Configuration configuration) {
		super(configuration);
	}

	public void addAllObjects(final List<Object> records) {
		this.objects.addAll(records);
	}

	public void addObject(final Object object) {
		this.objects.add(object);
	}

	public boolean read() {
		for (final Object obj : this.objects) {
			super.deliver(ListReader.OUTPUT_PORT_NAME, obj);
		}
		return true;
	}

	public void terminate(final boolean error) {}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
