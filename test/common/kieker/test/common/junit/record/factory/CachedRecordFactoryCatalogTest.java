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

package kieker.test.common.junit.record.factory;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.CachedRecordFactoryCatalog;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.factory.old.RecordFactoryWrapper;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEventFactory;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.util.record.factory.TestRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.11
 */
public class CachedRecordFactoryCatalogTest extends AbstractKiekerTest {

	@Rule
	public final ExpectedException thrown = ExpectedException.none(); // NOCS (rule)

	private CachedRecordFactoryCatalog cachedRecordFactories;
	private ByteBuffer buffer;
	private IRegistry<String> stringRegistry;

	public CachedRecordFactoryCatalogTest() {
		// Nothing to do
	}

	@Before
	public void before() throws Exception {
		this.cachedRecordFactories = CachedRecordFactoryCatalog.getInstance();
		this.buffer = ByteBuffer.allocateDirect(1024);
		this.stringRegistry = new Registry<String>();
	}

	@Test
	public void testRecordWithFactoryIsAssignedItsFactory() {
		final String recordClassName = AfterOperationEvent.class.getName();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactories.get(recordClassName);
		Assert.assertEquals(AfterOperationEventFactory.class, recordFactory.getClass());
	}

	@Test
	public void testRecordConstructionWithFactory() {
		final String recordClassName = AfterOperationEvent.class.getName();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactories.get(recordClassName);

		final String operationSignature = "isEmpty()";
		final String classSignature = "java.util.List";
		final int orderIndex = 333;
		final long traceId = 666;
		final long timestamp = 111;
		final AfterOperationEvent expectedEvent = new AfterOperationEvent(timestamp, traceId, orderIndex, classSignature, operationSignature);
		expectedEvent.writeBytes(this.buffer, this.stringRegistry);
		this.buffer.flip();
		final IMonitoringRecord event = recordFactory.create(this.buffer, this.stringRegistry);

		Assert.assertEquals(expectedEvent.getClass(), event.getClass());
		final AfterOperationEvent castedEvent = (AfterOperationEvent) event;
		Assert.assertEquals(expectedEvent.getTimestamp(), castedEvent.getTimestamp());
		Assert.assertEquals(expectedEvent.getTraceId(), castedEvent.getTraceId());
		Assert.assertEquals(expectedEvent.getOrderIndex(), castedEvent.getOrderIndex());
		Assert.assertEquals(expectedEvent.getClassSignature(), castedEvent.getClassSignature());
		Assert.assertEquals(expectedEvent.getOperationSignature(), castedEvent.getOperationSignature());
	}

	@Test
	public void testRecordWithoutFactoryIsAssignedFactoryWrapper() {
		final String recordClassName = TestRecord.class.getName();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactories.get(recordClassName);
		Assert.assertEquals(RecordFactoryWrapper.class, recordFactory.getClass());
	}

	@Test
	public void testNotExistingRecordThrowsException() {
		final String recordClassName = "record.that.does.not.exist";
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactories.get(recordClassName);
		Assert.assertEquals(RecordFactoryWrapper.class, recordFactory.getClass());

		this.thrown.expect(RecordInstantiationException.class);
		recordFactory.create(this.buffer, this.stringRegistry);
	}

}
