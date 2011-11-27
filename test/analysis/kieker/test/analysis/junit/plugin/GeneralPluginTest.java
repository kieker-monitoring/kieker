package kieker.test.analysis.junit.plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.analysis.plugin.port.AbstractInputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.common.record.OperationExecutionRecord;
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

		final ConcurrentLinkedQueue<Execution> lst = new ConcurrentLinkedQueue<Execution>();
		final OutputPort source = new OutputPort("", Arrays.asList(new Class<?>[] { OperationExecutionRecord.class }));
		final AbstractInputPort dst = new AbstractInputPort("", Arrays.asList(new Class<?>[] { Execution.class })) {

			@Override
			public void newEvent(final Object event) {
				lst.add((Execution) event);
			}
		};

		final ExecutionRecordTransformationFilter transformer = new ExecutionRecordTransformationFilter("", new SystemModelRepository());
		final TraceIdFilter filter1 = new TraceIdFilter(new HashSet<Long>(Arrays.asList(new Long[] { 1l })));
		final TimestampFilter filter2 = new TimestampFilter(10, 20);

		/* Connect them. */
		source.subscribe(transformer.getExecutionInputPort());
		transformer.getExecutionOutputPort().subscribe(filter1.getExecutionInputPort());
		filter1.getExecutionOutputPort().subscribe(filter2.getExecutionInputPort());
		filter2.getExecutionOutputPort().subscribe(dst);

		/* The records we will send. */
		final OperationExecutionRecord opExRec1 = new OperationExecutionRecord("", "", 1, 14, 15);
		final OperationExecutionRecord opExRec2 = new OperationExecutionRecord("", "", 2, 14, 15);
		final OperationExecutionRecord opExRec3 = new OperationExecutionRecord("", "", 1, 9, 15);
		final OperationExecutionRecord opExRec4 = new OperationExecutionRecord("", "", 1, 11, 21);
		final OperationExecutionRecord opExRec5 = new OperationExecutionRecord("", "", 2, 9, 15);
		final OperationExecutionRecord opExRec6 = new OperationExecutionRecord("", "", 2, 14, 21);
		final OperationExecutionRecord opExRec7 = new OperationExecutionRecord("", "", 1, 9, 21);
		final OperationExecutionRecord opExRec8 = new OperationExecutionRecord("", "", 1, 10, 20);

		source.deliver(opExRec1);
		source.deliver(opExRec2);
		source.deliver(opExRec3);
		source.deliver(opExRec4);
		source.deliver(opExRec5);
		source.deliver(opExRec6);
		source.deliver(opExRec7);
		source.deliver(opExRec8);

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
