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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.factory.RecordFactoryResolver;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEventFactory;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Christian Wulf
 *
 * @since 1.11
 */
public class RecordFactoryResolverTest extends AbstractKiekerTest {

	private RecordFactoryResolver recordFactoryResolver;

	public RecordFactoryResolverTest() {
		// Nothing to do
	}

	@Before
	public void before() throws Exception {
		this.recordFactoryResolver = new RecordFactoryResolver();
	}

	@Test
	public void testRecordWithFactory() {
		final String recordClassName = AfterOperationEvent.class.getName();
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactoryResolver.get(recordClassName);
		Assert.assertEquals(AfterOperationEventFactory.class, recordFactory.getClass());
	}

	@Test
	public void testRecordWithoutFactory() {
		final String recordClassName = TestRecord.class.getName();
		@SuppressWarnings("unused")
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactoryResolver.get(recordClassName);
		Assert.assertNull(recordFactory);
	}

	@Test
	public void testNotExistingRecord() {
		final String recordClassName = "record.that.does.not.exist";
		@SuppressWarnings("unused")
		final IRecordFactory<? extends IMonitoringRecord> recordFactory = this.recordFactoryResolver.get(recordClassName);
		Assert.assertNull(recordFactory);
	}

}
