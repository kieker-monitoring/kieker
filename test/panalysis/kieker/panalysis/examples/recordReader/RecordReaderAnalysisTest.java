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

import kieker.analysis.AnalysisController;
import kieker.analysis.ClassNameRegistry;
import kieker.analysis.ClassNameRegistryRepository;
import kieker.analysis.IAnalysisController;
import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.Registry;

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
		final IAnalysisController ac = new AnalysisController();

		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, INPUT_DIRECTORY.getAbsolutePath());
		final FSReader reader = new FSReader(readerConfiguration, ac);

		final ListCollectionFilter<IMonitoringRecord> records = new ListCollectionFilter<IMonitoringRecord>(new Configuration(), ac);
		final ClassNameRegistryFilter classNameRegistry = new ClassNameRegistryFilter(new Configuration(), ac);

		final CountingFilter countingFilter = new CountingFilter(new Configuration(), ac);

		ac.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);
		ac.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, records, ListCollectionFilter.INPUT_PORT_NAME);
		ac.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, classNameRegistry, ClassNameRegistryFilter.INPUT_PORT_NAME_RECORDS);

		ac.run();

		// Keep in mind that the metadata record is not processed in a regular way
		Assert.assertEquals(1, classNameRegistry.size());
		Assert.assertEquals("kieker.common.record.controlflow.OperationExecutionRecord", classNameRegistry.get(0));
		Assert.assertEquals(6540, countingFilter.getMessageCount());
		Assert.assertEquals(1283156545581511026L, records.getList().get(0).getLoggingTimestamp());
	}

	private static class ClassNameRegistryFilter extends AbstractFilterPlugin {

		public static final String INPUT_PORT_NAME_RECORDS = "input";

		private final Registry<String> registry = new Registry<String>();

		public ClassNameRegistryFilter(final Configuration configuration, final IProjectContext projectContext) {
			super(configuration, projectContext);
		}

		@InputPort(name = ClassNameRegistryFilter.INPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class)
		public void input(final IMonitoringRecord record) {
			this.registry.get(record.getClass().getName());
		}

		public Object get(final int id) {
			return this.registry.get(id);
		}

		public Object size() {
			return this.registry.getSize();
		}

		@Override
		public Configuration getCurrentConfiguration() {
			return new Configuration();
		}

	}
}
