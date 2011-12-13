package kieker.test.analysis.junit.plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.port.AOutputPort;
import kieker.analysis.plugin.port.APlugin;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.OperationExecutionRecord;
import kieker.test.analysis.junit.util.ExecutionSinkClass;
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
public class GeneralPluginTest {

	@Test
	public void testChaining() {
		final ExecutionRecordTransformationFilter transformer = new ExecutionRecordTransformationFilter("", new SystemModelRepository());
		final TraceIdFilter filter1 = new TraceIdFilter(new HashSet<Long>(Arrays.asList(new Long[] { 1l })));
		final TimestampFilter filter2 = new TimestampFilter(10, 20);

		/* The records we will send. */
		final OperationExecutionRecord opExRec1 = new OperationExecutionRecord("", "", 1, 14, 15);
		final OperationExecutionRecord opExRec2 = new OperationExecutionRecord("", "", 2, 14, 15);
		final OperationExecutionRecord opExRec3 = new OperationExecutionRecord("", "", 1, 9, 15);
		final OperationExecutionRecord opExRec4 = new OperationExecutionRecord("", "", 1, 11, 21);
		final OperationExecutionRecord opExRec5 = new OperationExecutionRecord("", "", 2, 9, 15);
		final OperationExecutionRecord opExRec6 = new OperationExecutionRecord("", "", 2, 14, 21);
		final OperationExecutionRecord opExRec7 = new OperationExecutionRecord("", "", 1, 9, 21);
		final OperationExecutionRecord opExRec8 = new OperationExecutionRecord("", "", 1, 10, 20);
		final SourceClass src = new SourceClass(opExRec1, opExRec2, opExRec3, opExRec4, opExRec5, opExRec6, opExRec7, opExRec8);
		final ExecutionSinkClass dst = new ExecutionSinkClass(null);

		/* Connect the plugins. */
		Assert.assertTrue(AbstractPlugin.connect(src, SourceClass.OUTPUT_PORT_NAME, transformer, ExecutionRecordTransformationFilter.INPUT_PORT_NAME));
		Assert.assertTrue(AbstractPlugin.connect(transformer, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME, filter1, TraceIdFilter.INPUT_PORT_NAME));
		Assert.assertTrue(AbstractPlugin.connect(filter1, TraceIdFilter.OUTPUT_PORT_NAME, filter2, TimestampFilter.INPUT_PORT_NAME));
		Assert.assertTrue(AbstractPlugin.connect(filter2, TimestampFilter.OUTPUT_PORT_NAME, dst, ExecutionSinkClass.INPUT_PORT_NAME));

		src.read();

		final ConcurrentLinkedQueue<Execution> lst = dst.getList();
		Assert.assertEquals(2, lst.size());

		boolean okay1 = false, okay2 = false;

		final Iterator<Execution> iter = lst.iterator();
		for (int i = 0; i < 2; i++) {
			final Execution ex = iter.next();
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

@APlugin(
		outputPorts = {
			@AOutputPort(name = SourceClass.OUTPUT_PORT_NAME, eventTypes = { OperationExecutionRecord.class })
		})
class SourceClass extends AbstractReaderPlugin {

	private final OperationExecutionRecord records[];
	public static final String OUTPUT_PORT_NAME = "output";

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
	public void terminate() {}

	@Override
	protected Configuration getDefaultConfiguration() {
		return null;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return null;
	}

}
