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
package kieker.tools.opad.record.junit;

//import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.record.NamedDoubleRecord;
//import kieker.common.util.registry.IRegistry;
//import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
//import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;

/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 *
 * @author Tom Frotscher
 *
 * @since 1.10
 */
public class TestGeneratedNamedDoubleRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedNamedDoubleRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link NamedDoubleRecord#TestNamedDoubleRecord(string, long, double)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i = 0; i < ARRAY_LENGTH; i++) {
			// initialize
			final NamedDoubleRecord record = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()),
					DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));

			// check values
			Assert.assertEquals("NamedDoubleRecord.applicationName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
			Assert.assertEquals("NamedDoubleRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("NamedDoubleRecord.responseTime values are not equal.",
					DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getResponseTime(), 0.0000001);
		}
	}

	/**
	 * Tests {@link NamedDoubleRecord#TestNamedDoubleRecord(string, long, double)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i = 0; i < ARRAY_LENGTH; i++) {
			// initialize
			final NamedDoubleRecord record = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()),
					DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));

			// check values
			Assert.assertEquals("NamedDoubleRecord.applicationName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
			Assert.assertEquals("NamedDoubleRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("NamedDoubleRecord.responseTime values are not equal.",
					DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getResponseTime(), 0.0000001);
		}
	}

	@Test
	public void testEquality() {
		int i = 0;
		final NamedDoubleRecord oneRecord = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()),
				DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		i = 0;
		final NamedDoubleRecord copiedRecord = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()),
				DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));

		Assert.assertEquals(oneRecord, copiedRecord);
	}

	@Test
	public void testUnequality() {
		int i = 0;
		final NamedDoubleRecord oneRecord = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()),
				DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		i = 2;
		final NamedDoubleRecord anotherRecord = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()),
				DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));

		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
