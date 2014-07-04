/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import kieker.common.record.IMonitoringRecord;

import kieker.test.tools.util.StringUtils;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.5
 */
public class BasicPrintStreamWriterTestFile extends AbstractPrintStreamWriterTest {
	private static final String OUTPUT_BASE_FN = "S0fYvPsI.out"; // the name doesn't matter

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (must be public)

	private volatile File outputFile = null; // NOPMD (init for findbugs)

	/**
	 * Default constructor.
	 */
	public BasicPrintStreamWriterTestFile() {
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
		this.tmpFolder.create();
		this.outputFile = this.tmpFolder.newFile(BasicPrintStreamWriterTestFile.OUTPUT_BASE_FN);
	}

	/**
	 * Cleans up after the test.
	 * 
	 * @throws Exception
	 *             If remaining files could not be removed.
	 */
	@After
	public void tearDown() throws Exception {
		this.tmpFolder.delete();
	}

	@Override
	protected String provideStreamName() {
		return this.outputFile.getAbsolutePath();
	}

	@Override
	protected void inspectRecords(final List<IMonitoringRecord> eventsPassedToController, final List<IMonitoringRecord> eventFromMonitoringLog) throws Exception {
		this.checkRecords(StringUtils.readOutputFileAsString(this.outputFile), eventsPassedToController);
	}
}
