package kieker.analysis.plugin.filter.forward;

import kieker.analysis.IProjectContext;
import kieker.analysis.display.TagCloud;
import kieker.analysis.display.annotation.Display;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.signature.ClassOperationSignaturePair;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
@Plugin(outputPorts =
		@OutputPort(
				name = MethodAndComponentFlowDisplayFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS,
				eventTypes = { OperationExecutionRecord.class },
				description = "Provides each incoming object"))
public class MethodAndComponentFlowDisplayFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "inputEvents";
	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";

	private final TagCloud methodTagCloud = new TagCloud();
	private final TagCloud componentTagCloud = new TagCloud();

	public MethodAndComponentFlowDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = MethodAndComponentFlowDisplayFilter.INPUT_PORT_NAME_EVENTS, eventTypes = { OperationExecutionRecord.class })
	public void input(final OperationExecutionRecord record) {
		final String className = ClassOperationSignaturePair.splitOperationSignatureStr(record.getOperationSignature()).getSimpleClassname();

		this.methodTagCloud.incrementCounter(record.getOperationSignature());
		this.componentTagCloud.incrementCounter(className);

		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, record);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@Display(name = "Method Tag Cloud Display")
	public TagCloud methodTagCloudDisplay() {
		return this.methodTagCloud;
	}

	@Display(name = "Component Tag Cloud Display")
	public TagCloud componentTagCloudDisplay() {
		return this.componentTagCloud;
	}
}
