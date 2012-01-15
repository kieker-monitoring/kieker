package kieker.test.tools.junit.traceAnalysis.util;

import java.util.ArrayList;
import java.util.List;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractRepository;

public class SimpleSinkPlugin extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "input";
	private final List<Object> list = new ArrayList<Object>();

	public SimpleSinkPlugin() {
		super(new Configuration(), new AbstractRepository[0]);
	}

	@InputPort()
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
	protected AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}

	@Override
	public AbstractRepository[] getCurrentRepositories() {
		return new AbstractRepository[0];
	}
}
