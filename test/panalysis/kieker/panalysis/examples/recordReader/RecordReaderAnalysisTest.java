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
package kieker.panalysis.examples.recordReader;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.ClassNameRegistry;
import kieker.analysis.ClassNameRegistryRepository;
import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class RecordReaderAnalysisTest {

	private final static File INPUT_DIRECTORY = new File("examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20100830-082225522-UTC");

	@Test
	public void testExampleWithNewFramework() {
		final List<IMonitoringRecord> records = new LinkedList<IMonitoringRecord>();

		final RecordReaderAnalysis recordReaderAnalysis = new RecordReaderAnalysis();
		recordReaderAnalysis.init();
		recordReaderAnalysis.setInputFile(RecordReaderAnalysisTest.INPUT_DIRECTORY);
		recordReaderAnalysis.setOutputRecordList(records);
		recordReaderAnalysis.start();

		final ClassNameRegistryRepository classNameRegistryRepository = recordReaderAnalysis.getClassNameRegistryRepository();
		Assert.assertEquals(1, classNameRegistryRepository.size());

		final ClassNameRegistry classNameRegistry = classNameRegistryRepository.get(RecordReaderAnalysisTest.INPUT_DIRECTORY);
		Assert.assertNotNull(classNameRegistry);

		Assert.assertEquals(2, classNameRegistry.size());
		Assert.assertEquals("kieker.common.record.misc.KiekerMetadataRecord", classNameRegistry.get(0));
		Assert.assertEquals("kieker.common.record.controlflow.OperationExecutionRecord", classNameRegistry.get(1));
		Assert.assertEquals(6541, records.size());
		Assert.assertEquals(1283156546127117246l, records.get(0).getLoggingTimestamp());
	}

	@Test
	public void testExampleWithKiekerFramework() throws Exception {
		final Configuration configuration = null;
		final IProjectContext projectContext = null;
		final FSReader fsReader = new FSReader(configuration, projectContext);
		// fsReader.
		// TODO
	}
}
