/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Jan Waller
 */
public final class TestPluginShutdown extends AbstractKiekerTest {

	private static final AtomicInteger SHUTDOWNORDER = new AtomicInteger();

	public TestPluginShutdown() {
		// nothing to do...
	}

	@Before
	public void init() {
		SHUTDOWNORDER.set(0);
	}

	@Test
	public void testOnlyReader() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController ctrl = new AnalysisController();
		final ShutdownReader r1 = new ShutdownReader(new Configuration(), ctrl);
		ctrl.run();
		Assert.assertEquals(0, r1.shutdownNr);
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, ctrl.getState());
	}

	@Test
	public void testReaderWithConnectedFilter() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController ctrl = new AnalysisController();
		final ShutdownReader r1 = new ShutdownReader(new Configuration(), ctrl);
		final ShutdownFilter f1 = new ShutdownFilter(new Configuration(), ctrl);

		ctrl.connect(r1, ShutdownReader.OUTPUT_PORT_NAME, f1, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.run();
		Assert.assertEquals(0, r1.shutdownNr);
		Assert.assertEquals(1, f1.shutdownNr);
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, ctrl.getState());
	}

	@Test
	public void testReaderWithUnconnectedFilter() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController ctrl = new AnalysisController();
		final ShutdownReader r1 = new ShutdownReader(new Configuration(), ctrl);
		final ShutdownFilter f1 = new ShutdownFilter(new Configuration(), ctrl);

		ctrl.run();
		Assert.assertEquals(0, r1.shutdownNr);
		Assert.assertEquals(1, f1.shutdownNr);
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, ctrl.getState());
	}

	@Test
	public void testChainOfFilters() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController ctrl = new AnalysisController();
		final ShutdownReader r1 = new ShutdownReader(new Configuration(), ctrl);
		final ShutdownFilter f1 = new ShutdownFilter(new Configuration(), ctrl);
		final ShutdownFilter f2 = new ShutdownFilter(new Configuration(), ctrl);
		final ShutdownFilter f3 = new ShutdownFilter(new Configuration(), ctrl);
		final ShutdownFilter f4 = new ShutdownFilter(new Configuration(), ctrl);

		ctrl.connect(r1, ShutdownReader.OUTPUT_PORT_NAME, f1, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(f1, ShutdownFilter.OUTPUT_PORT_NAME, f2, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(f2, ShutdownFilter.OUTPUT_PORT_NAME, f3, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(f3, ShutdownFilter.OUTPUT_PORT_NAME, f4, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.run();
		Assert.assertEquals(0, r1.shutdownNr);
		Assert.assertEquals(1, f1.shutdownNr);
		Assert.assertEquals(2, f2.shutdownNr);
		Assert.assertEquals(3, f3.shutdownNr);
		Assert.assertEquals(4, f4.shutdownNr);
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, ctrl.getState());
	}

	@Test
	public void testTwoReadersOneFilter() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController ctrl = new AnalysisController();
		final ShutdownReader r1 = new ShutdownReader(new Configuration(), ctrl);
		final ShutdownReader r2 = new ShutdownReader(new Configuration(), ctrl);
		final ShutdownFilter f1 = new ShutdownFilter(new Configuration(), ctrl);

		ctrl.connect(r1, ShutdownReader.OUTPUT_PORT_NAME, f1, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(r2, ShutdownReader.OUTPUT_PORT_NAME, f1, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.run();
		Assert.assertEquals(0, r1.shutdownNr);
		Assert.assertEquals(1, r2.shutdownNr);
		Assert.assertEquals(2, f1.shutdownNr);
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, ctrl.getState());
	}

	/**
	 * This test would have more than one correct answer!
	 */
	@Test
	public void testLongWayShortWay() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController ctrl = new AnalysisController();
		final ShutdownReader r1 = new ShutdownReader(new Configuration(), ctrl);
		final ShutdownReader r2 = new ShutdownReader(new Configuration(), ctrl);
		final ShutdownFilter f1 = new ShutdownFilter(new Configuration(), ctrl);
		final ShutdownFilter f2 = new ShutdownFilter(new Configuration(), ctrl);
		final ShutdownFilter f3 = new ShutdownFilter(new Configuration(), ctrl);

		ctrl.connect(r1, ShutdownReader.OUTPUT_PORT_NAME, f1, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(r2, ShutdownReader.OUTPUT_PORT_NAME, f3, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(f1, ShutdownFilter.OUTPUT_PORT_NAME, f2, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(f2, ShutdownFilter.OUTPUT_PORT_NAME, f3, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.run();
		Assert.assertEquals(0, r1.shutdownNr);
		Assert.assertEquals(1, f1.shutdownNr);
		Assert.assertEquals(2, f2.shutdownNr);
		Assert.assertEquals(3, r2.shutdownNr);
		Assert.assertEquals(4, f3.shutdownNr);
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, ctrl.getState());
	}

	/**
	 * This test would have more than one correct answer!
	 */
	@Test
	public void testLoop() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController ctrl = new AnalysisController();
		final ShutdownReader r1 = new ShutdownReader(new Configuration(), ctrl);
		final ShutdownFilter f1 = new ShutdownFilter(new Configuration(), ctrl);
		final ShutdownFilter f2 = new ShutdownFilter(new Configuration(), ctrl);

		ctrl.connect(r1, ShutdownReader.OUTPUT_PORT_NAME, f1, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(f1, ShutdownFilter.OUTPUT_PORT_NAME, f2, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.connect(f2, ShutdownFilter.OUTPUT_PORT_NAME, f1, ShutdownFilter.INPUT_PORT_NAME);
		ctrl.run();
		Assert.assertEquals(0, r1.shutdownNr);
		Assert.assertEquals(1, f2.shutdownNr);
		Assert.assertEquals(2, f1.shutdownNr);
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, ctrl.getState());
	}

	@Plugin(programmaticOnly = true, outputPorts = { @OutputPort(name = ShutdownReader.OUTPUT_PORT_NAME) })
	private static final class ShutdownReader extends AbstractReaderPlugin {
		public static final String OUTPUT_PORT_NAME = "out";

		private int shutdownNr = -1;

		public ShutdownReader(final Configuration configuration, final IProjectContext projectContext) {
			super(configuration, projectContext);
		}

		/**
		 * {@inheritDoc}
		 */
		public void terminate(final boolean error) {
			this.shutdownNr = SHUTDOWNORDER.getAndIncrement();
		}

		/**
		 * {@inheritDoc}
		 */
		public Configuration getCurrentConfiguration() {
			return new Configuration();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean read() {
			// don't send anything (else we would fail in loop!)
			// super.deliver(OUTPUT_PORT_NAME, new Object());
			return true;
		}
	}

	@Plugin(programmaticOnly = true, outputPorts = { @OutputPort(name = ShutdownFilter.OUTPUT_PORT_NAME) })
	private static final class ShutdownFilter extends AbstractFilterPlugin {
		public static final String OUTPUT_PORT_NAME = "out";
		public static final String INPUT_PORT_NAME = "in";

		private int shutdownNr = -1;

		public ShutdownFilter(final Configuration configuration, final IProjectContext projectContext) {
			super(configuration, projectContext);
		}

		public Configuration getCurrentConfiguration() {
			return new Configuration();
		}

		@Override
		public void terminate(final boolean error) {
			this.shutdownNr = SHUTDOWNORDER.getAndIncrement();
		}

		@InputPort(name = INPUT_PORT_NAME)
		public final void inputEvent(final Object event) {
			super.deliver(OUTPUT_PORT_NAME, event);
		}
	}
}
