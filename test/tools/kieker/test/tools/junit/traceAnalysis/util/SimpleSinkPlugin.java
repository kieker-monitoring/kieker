package kieker.test.tools.junit.traceAnalysis.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;

public class SimpleSinkPlugin extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "input";
	private final List<Object> list = new ArrayList<Object>();

	public SimpleSinkPlugin() {
		super(new Configuration(), new HashMap<String, AbstractRepository>());
	}

	@InputPort(name = SimpleSinkPlugin.INPUT_PORT_NAME)
	public void input(final Object data) {
		this.list.add(data);
	}

	public void clear() {
		this.list.clear();
	}

	public List<Object> getList() {
		return this.list;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return null;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return null;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(final boolean error) {}

	@Override
	public Map<String, AbstractRepository> getCurrentRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
