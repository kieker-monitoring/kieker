package livedemo.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import livedemo.entities.Record;

@Plugin(programmaticOnly = true,
	description = "A filter collecting incoming objects in a list",
	outputPorts = @OutputPort(
			name = OER2RecordFilter.OUTPUT_PORT_NAME, 
			eventTypes = { Record.class }, 
			description = "Provides each incoming object"))
public class OER2RecordFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "inputObject";
	public static final String OUTPUT_PORT_NAME = "outputObjects";
	
	public OER2RecordFilter(Configuration configuration,
			IProjectContext projectContext) {
		super(configuration, projectContext);
	}
	
	@InputPort(name = OER2RecordFilter.INPUT_PORT_NAME)
	public void input(final OperationExecutionRecord record){
		this.deliver(OUTPUT_PORT_NAME, new Record(record));
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

}
