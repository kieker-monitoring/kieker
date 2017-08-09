/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.printStream;

import java.io.PrintStream;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.print.PrintStreamWriter;

import kieker.test.tools.util.StringTeePrintStream;

/**
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public class BasicPrintStreamWriterTestStdErr extends AbstractPrintStreamWriterTest {

	private volatile PrintStream originalPrintStream;

	private volatile StringTeePrintStream stringTeePrintStream = null; // NOPMD (init for findbugs)

	/**
	 * Default constructor.
	 */
	public BasicPrintStreamWriterTestStdErr() {
		// empty default constructor
	}

	/**
	 * Initializes the test setup.
	 *
	 * @throws Exception
	 *             If something went wrong during the initialization.
	 */
	@Before
	public void setUp() throws Exception {
		this.originalPrintStream = System.err;
		this.stringTeePrintStream = new StringTeePrintStream(this.originalPrintStream);
		System.setErr(this.stringTeePrintStream);
	}

	/**
	 * Cleans up after the test.
	 *
	 * @throws Exception
	 *             If something went wrong.
	 */
	@After
	public void tearDown() throws Exception {
		System.setErr(this.originalPrintStream);
	}

	@Override
	protected String provideStreamName() {
		return PrintStreamWriter.CONFIG_STREAM_STDERR;
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) {
		this.checkRecords(this.stringTeePrintStream.getString(), eventsPassedToController);
	}
}
