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

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.factory.old.RecordFactoryWrapper;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEventFactory;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class CachedRecordFactoryRepositoryTest {

	private CachedRecordFactoryRepository cachedRecordFactories;
	private ByteBuffer buffer;
	private IRegistry<String> stringRegistry;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void before() throws Exception {
		final RecordFactoryRepository recordFactoryRepository = new RecordFactoryRepository();
		this.cachedRecordFactories = new CachedRecordFactoryRepository(recordFactoryRepository);
		this.buffer = ByteBuffer.allocateDirect(1024 * 8);
		this.stringRegistry = new Registry<String>();
	}

	@Test
	public void testRecordWithFactory() {
		final String recordClassName = AfterOperationEvent.class.getName();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactories.get(recordClassName);
		Assert.assertEquals(AfterOperationEventFactory.class, recordFactory.getClass());
	}

	@Test
	public void testRecordWithoutFactory() {
		final String recordClassName = TestRecord.class.getName();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactories.get(recordClassName);
		Assert.assertEquals(RecordFactoryWrapper.class, recordFactory.getClass());

		final OperationExecutionRecord operationExecutionRecord = new OperationExecutionRecord("test()", "1111-1111", 1, 1000, 2500, "localhost", 0, 0);
		operationExecutionRecord.writeBytes(this.buffer, this.stringRegistry);
		recordFactory.create(this.buffer, this.stringRegistry);
	}

	@Test
	public void testNotExistingRecord() {
		final String recordClassName = "record.that.does.not.exist";
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactories.get(recordClassName);
		Assert.assertEquals(RecordFactoryWrapper.class, recordFactory.getClass());

		this.thrown.expect(RecordInstantiationException.class);
		recordFactory.create(this.buffer, this.stringRegistry);
	}

}
