package kieker.tools.opad.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.record.NamedDoubleRecord;

/**
 * Converts OperationExecutionRecords to NamedDoubleRecords.
 * 
 * @author Thomas Düllmann
 * 
 */
@Plugin(
		description = "Converts OperationExecutionRecords to NamedDoubleRecords",
		outputPorts = @OutputPort(name = RecordConverter.OUTPUT_PORT_NAME_NDR, eventTypes = { NamedDoubleRecord.class }))
public class RecordConverter extends AbstractFilterPlugin {

	/**
	 * Input port that accepts OperationExecutionRecords.
	 */
	public static final String INPUT_PORT_NAME_OER = "oer";

	/**
	 * Ouput port that delivers NamedDoubleRecords.
	 */
	public static final String OUTPUT_PORT_NAME_NDR = "ndr";

	/**
	 * Constructor.
	 * 
	 * @param configuration
	 *            configuration for the converter
	 * @param projectContext
	 *            controller
	 */
	public RecordConverter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * Receives OperationExecutionRecords and delivers them converted to NamedDoubleRecords.
	 * 
	 * @param oer
	 *            OperationExecutionRecord object to be converted
	 */
	@InputPort(name = RecordConverter.INPUT_PORT_NAME_OER, eventTypes = { OperationExecutionRecord.class })
	public void convert(final OperationExecutionRecord oer) {
		// string, long, double
		final long timestamp = oer.getLoggingTimestamp();
		final String application = oer.getHostname() + ":" + oer.getOperationSignature();
		final double response = oer.getTout() - oer.getTin();

		if (response >= 0.0d) {
			final NamedDoubleRecord ndr = new NamedDoubleRecord(application, timestamp, response);
			if (ndr != null) {
				super.deliver(OUTPUT_PORT_NAME_NDR, ndr);
			}
		}
	}
}
