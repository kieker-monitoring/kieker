/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.analysis.junit.plugin.reader.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kicker.analysis.AnalysisController;
import kicker.analysis.IAnalysisController;
import kicker.analysis.exception.AnalysisConfigurationException;
import kicker.analysis.plugin.filter.forward.CountingFilter;
import kicker.analysis.plugin.reader.filesystem.FSReader;
import kicker.common.configuration.Configuration;
import kicker.common.util.filesystem.FSUtil;
import kicker.test.common.junit.AbstractKickerTest;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class TestLegacyExecutionRecordReader extends AbstractKickerTest {

	private static final String MAP_FILE = "$0=kicker.common.record.controlflow.OperationExecutionRecord\n"
			+ "$1=kicker.common.record.OperationExecutionRecord\n"
			+ "$2=kicker.tpmon.monitoringRecord.executions.KickerExecutionRecord\n";

	private static final String DAT_FILE = "\n" // empty ignored line
			+ "$0;1;public void kicker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n" // modern record
			+ "$0;1;-1;public void kicker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n" // legacy record
			+ "-1;public void kicker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n" // very legacy record
			+ "$1;1;public void kicker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n" // legacy names
			+ "$2;1;public void kicker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n"; // legacy names

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	public TestLegacyExecutionRecordReader() {
		// default constructor
	}

	/**
	 * Performs an initial test setup.
	 * 
	 * @throws IOException
	 *             If the setup failed.
	 */
	@Before
	public void setUp() throws IOException {
		final File mapFile = this.tmpFolder.newFile(FSUtil.MAP_FILENAME);
		final PrintStream mapStream = new PrintStream(new FileOutputStream(mapFile), false, FSUtil.ENCODING);
		mapStream.print(MAP_FILE);
		mapStream.close();
		final File datFile = this.tmpFolder.newFile(FSUtil.FILE_PREFIX + FSUtil.NORMAL_FILE_EXTENSION);
		final PrintStream datStream = new PrintStream(new FileOutputStream(datFile), false, FSUtil.ENCODING);
		datStream.print(DAT_FILE);
		datStream.close();
	}

	@Test
	public void testRecords() throws IOException, IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController analysisController = new AnalysisController();

		final Configuration configurationFSReader = new Configuration();
		configurationFSReader.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, this.tmpFolder.getRoot().getCanonicalPath());
		final FSReader reader = new FSReader(configurationFSReader, analysisController);

		final CountingFilter sink = new CountingFilter(new Configuration(), analysisController);

		analysisController.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, sink, CountingFilter.INPUT_PORT_NAME_EVENTS);

		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());
		Assert.assertEquals(5L, sink.getMessageCount()); // 5 records are expected
	}
}
