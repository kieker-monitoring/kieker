package kieker.test.analysis.junit.plugin;

import java.util.List;

import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractReaderPlugin;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.plugins.executionFilter.TimestampFilter;
import kieker.tools.traceAnalysis.plugins.executionFilter.TraceIdFilter;
import kieker.tools.traceAnalysis.plugins.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.junit.Assert;
import org.junit.Test;

/**
 * A simple test for the plugins in general. It tests for example if the
 * chaining of different plugins does work.
 * 
 * @author Nils Christian Ehmke
 */
public class GeneralPluginTest extends TestCase {

	@Test
	public void testChaining() {
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration());

		final ExecutionRecordTransformationFilter transformer = new ExecutionRecordTransformationFilter(new Configuration());

		final Configuration filter1byTraceIDConfig = new Configuration();
		filter1byTraceIDConfig.setProperty(TraceIdFilter.CONFIG_SELECT_ALL_TRACES, Boolean.FALSE.toString());
		filter1byTraceIDConfig.setProperty(TraceIdFilter.CONFIG_SELECTED_TRACES, Configuration.toProperty(new Long[] { 1l }));
		final TraceIdFilter filter1byTraceID = new TraceIdFilter(filter1byTraceIDConfig);

		final Configuration filter2ByTimestampConfiguration = new Configuration();
		filter2ByTimestampConfiguration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_BEFORE_TIMESTAMP, Long.toString(10l));
		filter2ByTimestampConfiguration.setProperty(TimestampFilter.CONFIG_IGNORE_EXECUTIONS_AFTER_TIMESTAMP, Long.toString(20l));
		final TimestampFilter filter2ByTimestamp = new TimestampFilter(filter2ByTimestampConfiguration);

		/* The records we will send. */
		final OperationExecutionRecord opExRec1 = new OperationExecutionRecord("", 1, 14, 15);
		final OperationExecutionRecord opExRec2 = new OperationExecutionRecord("", 2, 14, 15);
		final OperationExecutionRecord opExRec3 = new OperationExecutionRecord("", 1, 9, 15);
		final OperationExecutionRecord opExRec4 = new OperationExecutionRecord("", 1, 11, 21);
		final OperationExecutionRecord opExRec5 = new OperationExecutionRecord("", 2, 9, 15);
		final OperationExecutionRecord opExRec6 = new OperationExecutionRecord("", 2, 14, 21);
		final OperationExecutionRecord opExRec7 = new OperationExecutionRecord("", 1, 9, 21);
		final OperationExecutionRecord opExRec8 = new OperationExecutionRecord("", 1, 10, 20);
		final SourceClass src = new SourceClass(opExRec1, opExRec2, opExRec3, opExRec4, opExRec5, opExRec6, opExRec7, opExRec8);
		final ExecutionSinkClass dst = new ExecutionSinkClass(new Configuration());

		final AnalysisController controller = new AnalysisController();
		controller.registerReader(src);
		controller.registerFilter(transformer);
		controller.registerFilter(transformer);
		controller.registerFilter(filter1byTraceID);
		controller.registerFilter(filter2ByTimestamp);
		controller.registerFilter(dst);

		/* Connect the plugins. */
		Assert.assertTrue(controller.connect(src, SourceClass.OUTPUT_PORT_NAME, transformer, ExecutionRecordTransformationFilter.INPUT_PORT_NAME));
		Assert.assertTrue(transformer.connect(AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, systemModelRepository));
		Assert.assertTrue(controller.connect(transformer, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME, filter1byTraceID, TraceIdFilter.INPUT_PORT_NAME));
		Assert.assertTrue(controller.connect(filter1byTraceID, TraceIdFilter.OUTPUT_PORT_NAME, filter2ByTimestamp, TimestampFilter.INPUT_PORT_NAME));
		Assert.assertTrue(controller.connect(filter2ByTimestamp, TimestampFilter.OUTPUT_PORT_NAME, dst, ExecutionSinkClass.INPUT_PORT_NAME));

		src.read();

		final List<Execution> execs = dst.getExecutions();
		Assert.assertEquals(2, execs.size());

		boolean okay1 = false, okay2 = false;

		for (final Execution ex : execs) {
			if ((ex.getTraceId() == 1) && (ex.getTin() == 14) && (ex.getTout() == 15)) {
				okay1 = true;
			} else {
				if ((ex.getTraceId() == 1) && (ex.getTin() == 10) && (ex.getTout() == 20)) {
					okay2 = true;
				}
			}
		}

		Assert.assertTrue(okay1);
		Assert.assertTrue(okay2);
	}
}

/**
 * This is just a helper class used for testing the plugin structure. It should not be used outside this test class.
 * 
 * @author Nils Christian Ehmke
 * @version 1.0
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = SourceClass.OUTPUT_PORT_NAME, eventTypes = { OperationExecutionRecord.class })
		})
class SourceClass extends AbstractReaderPlugin {

	/**
	 * The array containing the records to be delivered.
	 */
	private final OperationExecutionRecord records[];
	/**
	 * The name of the default output port.
	 */
	public static final String OUTPUT_PORT_NAME = "output";

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param records
	 *            The records to be delivered via the output port.
	 */
	public SourceClass(final OperationExecutionRecord... records) {
		super(new Configuration());
		this.records = records;
	}

	@Override
	public boolean read() {
		for (final OperationExecutionRecord record : this.records) {
			Assert.assertTrue(super.deliver(SourceClass.OUTPUT_PORT_NAME, record));
		}
		return true;
	}

	@Override
	public void terminate(final boolean error) {}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
