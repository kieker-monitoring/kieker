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

package kieker.common.record.factory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEventFactory;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class RecordFactoryRepositoryTest {

	private RecordFactoryRepository recordFactories;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() throws Exception {
		this.recordFactories = new RecordFactoryRepository();
	}

	@Test
	public void testRecordWithFactory() throws ClassNotFoundException, MonitoringRecordException {
		final String recordClassName = AfterOperationEvent.class.getName();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
		Assert.assertEquals(AfterOperationEventFactory.class, recordFactory.getClass());
	}

	@Test
	public void testRecordWithoutFactory() throws ClassNotFoundException, MonitoringRecordException {
		this.thrown.expect(ClassNotFoundException.class);
		final String recordClassName = OperationExecutionRecord.class.getName();
		@SuppressWarnings("unused")
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
	}

	@Test
	public void testNotExistingRecord() throws ClassNotFoundException, MonitoringRecordException {
		this.thrown.expect(ClassNotFoundException.class);
		// this.thrown.expectMessage(substring);
		final String recordClassName = "record.that.does.not.exist";
		@SuppressWarnings("unused")
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactories.get(recordClassName);
	}

}
