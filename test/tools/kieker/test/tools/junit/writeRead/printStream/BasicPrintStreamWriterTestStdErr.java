/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.PrintStreamWriter;
import kieker.test.tools.junit.writeRead.util.StringTeePrintStream;

/**
 * TODO: introduce abstract intermediate class with {@link BasicPrintStreamWriterTestStdout},
 * because a lot of code is shared.
 * 
 * @author Andre van Hoorn
 * 
 */
public class BasicPrintStreamWriterTestStdErr extends AbstractPrintStreamWriterTest { // NOCS (test class without constructor)
	private volatile PrintStream originalPrintStream = null;

	private volatile StringTeePrintStream stringTeePrintStream = null;

	@Before
	public void setUp() throws Exception {
		this.originalPrintStream = System.err;
		this.stringTeePrintStream = new StringTeePrintStream(this.originalPrintStream);
		System.setErr(this.stringTeePrintStream);
	}

	@After
	public void tearDown() throws Exception {
		System.setErr(this.originalPrintStream);
	}

	@Override
	protected String provideStreamName() {
		return PrintStreamWriter.CONFIG_STREAM_STDERR;
	}

	@Override
	protected List<IMonitoringRecord> readEvents() {
		/*
		 * we cannot do anything meaningful here, because there's nothing like a PrintStreamReader.
		 * We'll return an empty List and use our own buffer when evaluating the result.
		 */
		return new ArrayList<IMonitoringRecord>();
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) {
		final String outputString = this.stringTeePrintStream.getString();

		for (final IMonitoringRecord rec : eventsPassedToController) {
			final StringBuilder inputRecordStringBuilder = new StringBuilder();
			inputRecordStringBuilder
					// note that this format needs to be adjusted if the writer's format changes
					.append(rec.getClass().getSimpleName())
					.append(": ")
					.append(rec).append(AbstractPrintStreamWriterTest.SYSTEM_NEWLINE_STRING);
			final String curLine = inputRecordStringBuilder.toString();
			Assert.assertTrue("Record '" + curLine + "' not found in output stream: '" + outputString + "'",
					outputString.indexOf(curLine) != -1);
		}
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return false;
	}
}
