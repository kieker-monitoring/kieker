/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.junit.plugin.reader.filesystem;

import java.io.File;

import org.junit.Test;

import kieker.analysis.tt.reader.filesystem.Dir2RecordsFilter;
import kieker.analysis.tt.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractStage;
import teetime.framework.CompositeStage;
import teetime.framework.Configuration;
import teetime.framework.Execution;
import teetime.framework.OutputPort;
import teetime.stage.InitialElementProducer;
import teetime.stage.io.Printer;

/**
 * @author Christian Wulf
 *
 * @since 1.14
 *
 * @deprecated since 1.15 remove in 1.16
 */
@Deprecated
public class Dir2RecordsFilterTest {

	public Dir2RecordsFilterTest() {
		// empty constructor
	}

	@Test
	public void shouldNotThrowAnyException() { // NOPMD (Test if no exception is thrown)
		new Execution<>(new TestConfiguration()).executeBlocking();
	}

	/**
	 * @since 1.14
	 */
	static class TestConfiguration extends Configuration { // NOPMD (default modifier intended)

		public TestConfiguration() {
			final ReadingComposite reader = new ReadingComposite(new File("."));
			final Printer<IMonitoringRecord> printer = new Printer<>();

			this.connectPorts(reader.getOutputPort(), printer.getInputPort());
		}
	}

	/**
	 * @since 1.14
	 */
	static class ReadingComposite extends CompositeStage { // NOPMD (default modifier intended)

		private final InitialElementProducer<File> producer;
		private final Dir2RecordsFilter reader;

		public ReadingComposite(final File importDirectory) {
			this.producer = new InitialElementProducer<>(importDirectory);
			this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

			this.connectPorts(this.producer.getOutputPort(), this.reader.getInputPort());
		}

		public OutputPort<IMonitoringRecord> getOutputPort() {
			return this.reader.getOutputPort();
		}

		public AbstractStage getFirstStage() {
			return this.producer;
		}

	}

}
