package kieker.test.analysisteetime.junit.plugin.filter.sink;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.display.TagCloud;
import kieker.analysisteetime.plugin.filter.sink.MethodAndComponentFlowDisplayFilter;
import kieker.common.record.controlflow.OperationExecutionRecord;

import teetime.framework.test.StageTester;

/**
 * Test cases for {@link MemSwapUtilizationDisplayStage}.
 *
 * @author Lars Erik Bluemke
 */
public class MethodAndComponentFlowDisplayFilterTest {

	private MethodAndComponentFlowDisplayFilter methodAndComponentFlowDisplayFilter = null;

	private static final String OPERATION_SIGNATURE = "public void package.subpackage.Class.method(package.Type)";
	private static final String SESSION_ID = "test_session";
	private static final long TRACE_ID = 1;
	private static final long TIN = 2;
	private static final long TOUT = 3;
	private static final String HOSTNAME = "test_host";
	private static final int EOI = 4;
	private static final int ESS = 5;

	private final OperationExecutionRecord record = new OperationExecutionRecord(OPERATION_SIGNATURE, SESSION_ID, TRACE_ID, TIN, TOUT, HOSTNAME, EOI, ESS);

	@Before
	public void initializeNewFilter() {
		this.methodAndComponentFlowDisplayFilter = new MethodAndComponentFlowDisplayFilter();
	}

	/**
	 * Tests if the counter value in the method TagCloud is set correctly.
	 */
	@Test
	public void methodTagCloudShouldBeCorrect() {
		final String methodName = "Class.method";
		final TagCloud methodTagCloud = new TagCloud();
		methodTagCloud.incrementCounter(methodName);

		StageTester.test(this.methodAndComponentFlowDisplayFilter).and().send(this.record).to(this.methodAndComponentFlowDisplayFilter.getInputPort()).start();

		Assert.assertEquals(this.methodAndComponentFlowDisplayFilter.methodTagCloudDisplay().getCounters().get(methodName).get(),
				methodTagCloud.getCounters().get(methodName).get());

	}

	/**
	 * Tests if the counter value in the component TagCloud is set correctly.
	 */
	@Test
	public void componentTagCloudShouldBeCorrect() {
		final String className = "Class";
		final TagCloud componentTagCloud = new TagCloud();
		componentTagCloud.incrementCounter(className);

		StageTester.test(this.methodAndComponentFlowDisplayFilter).and().send(this.record).to(this.methodAndComponentFlowDisplayFilter.getInputPort()).start();

		Assert.assertEquals(this.methodAndComponentFlowDisplayFilter.componentTagCloudDisplay().getCounters().get(className).get(),
				componentTagCloud.getCounters().get(className).get());
	}

}
