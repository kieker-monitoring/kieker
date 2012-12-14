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

package kieker.test.tools.junit.writeRead.printStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import kieker.common.record.IMonitoringRecord;

/**
 * TODO: introduce abstract intermediate class with {@link BasicPrintStreamWriterTestStdout},
 * because a lot of code is shared.
 * 
 * @author Andre van Hoorn
 */
public class BasicPrintStreamWriterTestFile extends AbstractPrintStreamWriterTest { // NOCS (testClass without constructor)
	private static final String OUTPUT_BASE_FN = "S0fYvPsI.out"; // the name doesn't matter

	private static final String ENCODING = "UTF-8";

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (must be public)

	private volatile File outputFile = null; // NOPMD (init for findbugs)

	@Before
	public void setUp() throws Exception {
		this.tmpFolder.create();
		this.outputFile = this.tmpFolder.newFile(BasicPrintStreamWriterTestFile.OUTPUT_BASE_FN);
	}

	@After
	public void tearDown() throws Exception {
		this.tmpFolder.delete();
	}

	@Override
	protected String provideStreamName() {
		return this.outputFile.getAbsolutePath();
	}

	@Override
	protected List<IMonitoringRecord> readEvents() {
		/*
		 * we cannot do anything meaningful here, because there's nothing like a PrintStreamReader.
		 * We'll return an empty List and use our own buffer when evaluating the result.
		 */
		return new ArrayList<IMonitoringRecord>();
	}

	// TODO: Move this method to an IO helper class, because it is also used by other classes
	public static String readOutputFileAsString(final File theFile) throws IOException {
		final byte[] buffer = new byte[(int) theFile.length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(theFile));
			if (f.read(buffer) == -1) {
				Assert.fail("Failed to read file into buffer: " + theFile.getAbsolutePath());
			}
		} finally {
			if (f != null) {
				f.close();
			}
		}
		return new String(buffer, BasicPrintStreamWriterTestFile.ENCODING);
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) throws Exception {

		final String outputString = BasicPrintStreamWriterTestFile.readOutputFileAsString(this.outputFile);

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
