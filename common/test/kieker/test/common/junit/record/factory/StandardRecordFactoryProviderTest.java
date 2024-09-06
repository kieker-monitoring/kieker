/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.factory.StandardRecordFactoryProvider;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEventFactory;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.util.record.factory.TestRecord;

/**
 * Test cases for the standard record factory provider.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public class StandardRecordFactoryProviderTest extends AbstractKiekerTest {

	private StandardRecordFactoryProvider provider;

	/** test constructor. */
	public StandardRecordFactoryProviderTest() {
		// Nothing to do
	}

	@Before
	public void before() throws Exception {
		this.provider = new StandardRecordFactoryProvider();
	}

	/**
	 * Test case: The expected factory for a record type with a factory is returned.
	 */
	@Test
	public void testRecordWithFactory() {
		final IRecordFactory<?> recordFactory = this.provider.createFactoryFor(AfterOperationEvent.class);
		Assert.assertEquals(AfterOperationEventFactory.class, recordFactory.getClass());
	}

	/**
	 * Test case: No record factory is returned for a record type without an according factory.
	 */
	@Test
	public void testRecordWithoutFactory() {
		final IRecordFactory<?> recordFactory = this.provider.createFactoryFor(TestRecord.class);
		Assert.assertNull(recordFactory);
	}

}
