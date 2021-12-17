/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.analysis.junit.plugin.filter.sink;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.display.TagCloud;
import kieker.analysis.sink.display.MethodAndComponentFlowDisplaySink;
import kieker.common.record.controlflow.OperationExecutionRecord;

import kieker.test.common.junit.AbstractKiekerTest;

import teetime.framework.test.StageTester;

/**
 * Test cases for {@link MethodAndComponentFlowDisplaySink}.
 *
 * @author Lars Bluemke
 *
 * @since 1.13
 */
public class TestMethodAndComponentFlowDisplayFilter extends AbstractKiekerTest {

	private static final String OPERATION_SIGNATURE = "public void package.subpackage.Class.method(package.Type)";
	private static final String SESSION_ID = "test_session";
	private static final long TRACE_ID = 1;
	private static final long TIN = 2;
	private static final long TOUT = 3;
	private static final String HOSTNAME = "test_host";
	private static final int EOI = 4;
	private static final int ESS = 5;

	private MethodAndComponentFlowDisplaySink methodAndComponentFlowDisplayFilter;
	private final OperationExecutionRecord record = new OperationExecutionRecord(OPERATION_SIGNATURE, SESSION_ID, TRACE_ID, TIN, TOUT, HOSTNAME, EOI, ESS);

	/**
	 * Empty default constructor.
	 */
	public TestMethodAndComponentFlowDisplayFilter() {
		// empty default constructor
	}

	/**
	 * Initializes a new filter before each test.
	 */
	@Before
	public void initializeNewFilter() {
		this.methodAndComponentFlowDisplayFilter = new MethodAndComponentFlowDisplaySink();
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
