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

package kieker.test.analysis.junit.plugin.reader.filesystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.util.filesystem.FSUtil;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Jan Waller
 * 
 * @since 1.6
 */
public class TestLegacyExecutionRecordReader extends AbstractKiekerTest {

	private static final String MAP_FILE = "$0=kieker.common.record.controlflow.OperationExecutionRecord\n"
			+ "$1=kieker.common.record.OperationExecutionRecord\n"
			+ "$2=kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord\n";

	private static final String DAT_FILE = "\n" // empty ignored line
			+ "$0;1;public void kieker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n" // modern record
			+ "$0;1;-1;public void kieker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n" // legacy record
			+ "-1;public void kieker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n" // very legacy record
			+ "$1;1;public void kieker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n" // legacy names
			+ "$2;1;public void kieker.test.Class.method();<no-session-id>;2;3;4;HOST;1;1\n"; // legacy names

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
		final File datFile = this.tmpFolder.newFile(FSUtil.FILE_PREFIX + FSUtil.DAT_FILE_EXTENSION);
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
