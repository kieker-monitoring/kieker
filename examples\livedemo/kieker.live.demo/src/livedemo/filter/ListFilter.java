package livedemo.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * @param <T>
 * 
 * @author Bjoern Weissenfels
 */
@Plugin(programmaticOnly = true,
		description = "A filter collecting incoming objects in a list",
		outputPorts = @OutputPort(name = ListFilter.OUTPUT_PORT_NAME, eventTypes = { Object.class }, description = "Provides each incoming object"))
public class ListFilter<T> extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "inputObject";
	public static final String OUTPUT_PORT_NAME = "outputObjects";

	private final List<T> list = Collections.synchronizedList(new ArrayList<T>());

	public ListFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = ListFilter.INPUT_PORT_NAME)
	@SuppressWarnings("unchecked")
	public synchronized void input(final Object data) {
		this.list.add((T) data);
		super.deliver(OUTPUT_PORT_NAME, data);
	}

	@SuppressWarnings("unchecked")
	public synchronized List<T> getListAndClear() {
		CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList<T>((T[]) this.list.toArray());
		this.list.clear();
		return list;
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
