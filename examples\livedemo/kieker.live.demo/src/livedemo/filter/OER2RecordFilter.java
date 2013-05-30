package livedemo.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import livedemo.entities.Record;

/** 
 * @author Bjoern Weissenfels
 */
@Plugin(description = "A filter that ",
		outputPorts = @OutputPort(name = ListFilter.OUTPUT_PORT_NAME, eventTypes = { Record.class }, description = "Provides each incoming object"))
public class OER2RecordFilter extends AbstractFilterPlugin {
	
	public static final String INPUT_PORT_NAME = "inputObject";
	public static final String OUTPUT_PORT_NAME = "outputObjects";

	public OER2RecordFilter(Configuration configuration,
			IProjectContext projectContext) {
		super(configuration, projectContext);
		// TODO Auto-generated constructor stub
	}
	
	@InputPort(name = ListFilter.INPUT_PORT_NAME)
	public synchronized void input(final OperationExecutionRecord operationExecutionRecord) {
		Record record = new Record(operationExecutionRecord);
		super.deliver(OUTPUT_PORT_NAME, record);
	}


	@Override
	public Configuration getCurrentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
