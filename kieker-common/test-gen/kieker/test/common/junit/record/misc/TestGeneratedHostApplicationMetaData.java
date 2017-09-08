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
package kieker.test.common.junit.record.misc;

//import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.misc.HostApplicationMetaData;
//import kieker.common.util.registry.IRegistry;
//import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
//import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;

/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class TestGeneratedHostApplicationMetaData extends AbstractGeneratedKiekerTest {

	public TestGeneratedHostApplicationMetaData() {
		// empty default constructor
	}

	/**
	 * Tests {@link HostApplicationMetaData#TestHostApplicationMetaData(string, string, string, string)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
		for (int i = 0; i < ARRAY_LENGTH; i++) {
			// initialize
			final HostApplicationMetaData record = new HostApplicationMetaData(STRING_VALUES.get(i % STRING_VALUES.size()),
					STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));

			// check values
			Assert.assertEquals("HostApplicationMetaData.systemName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getSystemName());
			Assert.assertEquals("HostApplicationMetaData.ipAddress values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getIpAddress());
			Assert.assertEquals("HostApplicationMetaData.hostName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("HostApplicationMetaData.applicationName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());

			final Object[] values = record.toArray();

			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 4.", 4, values.length);

			// check all object values exist
			Assert.assertNotNull("Array value [0] of type String must be not null.", values[0]);
			Assert.assertNotNull("Array value [1] of type String must be not null.", values[1]);
			Assert.assertNotNull("Array value [2] of type String must be not null.", values[2]);
			Assert.assertNotNull("Array value [3] of type String must be not null.", values[3]);

			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type String",
					values[0] instanceof String);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type String",
					values[1] instanceof String);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type String",
					values[2] instanceof String);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type String",
					values[3] instanceof String);

			// check all object values
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), values[0]);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), values[1]);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), values[2]);
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), values[3]);
		}
	}

	/**
	 * Tests {@link HostApplicationMetaData#TestHostApplicationMetaData(string, string, string, string)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i = 0; i < ARRAY_LENGTH; i++) {
			// initialize
			final HostApplicationMetaData record = new HostApplicationMetaData(STRING_VALUES.get(i % STRING_VALUES.size()),
					STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));

			// check values
			Assert.assertEquals("HostApplicationMetaData.systemName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getSystemName());
			Assert.assertEquals("HostApplicationMetaData.ipAddress values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getIpAddress());
			Assert.assertEquals("HostApplicationMetaData.hostName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("HostApplicationMetaData.applicationName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
		}
	}

	/**
	 * Tests {@link HostApplicationMetaData#TestHostApplicationMetaData(string, string, string, string)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i = 0; i < ARRAY_LENGTH; i++) {
			// initialize
			final HostApplicationMetaData record = new HostApplicationMetaData(STRING_VALUES.get(i % STRING_VALUES.size()),
					STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));

			// check values
			Assert.assertEquals("HostApplicationMetaData.systemName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getSystemName());
			Assert.assertEquals("HostApplicationMetaData.ipAddress values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getIpAddress());
			Assert.assertEquals("HostApplicationMetaData.hostName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("HostApplicationMetaData.applicationName values are not equal.",
					STRING_VALUES.get(i % STRING_VALUES.size()) == null ? "" : STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
		}
	}

	@Test
	public void testEquality() {
		int i = 0;
		final HostApplicationMetaData oneRecord = new HostApplicationMetaData(STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
		i = 0;
		final HostApplicationMetaData copiedRecord = new HostApplicationMetaData(STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));

		Assert.assertEquals(oneRecord, copiedRecord);
	}

	@Test
	public void testUnequality() {
		int i = 0;
		final HostApplicationMetaData oneRecord = new HostApplicationMetaData(STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));
		i = 2;
		final HostApplicationMetaData anotherRecord = new HostApplicationMetaData(STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()));

		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
