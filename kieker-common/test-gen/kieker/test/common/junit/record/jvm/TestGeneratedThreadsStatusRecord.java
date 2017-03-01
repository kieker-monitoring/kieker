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
package kieker.test.common.junit.record.jvm;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.jvm.ThreadsStatusRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class TestGeneratedThreadsStatusRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedThreadsStatusRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link ThreadsStatusRecord#TestThreadsStatusRecord(long, string, string, long, long, long, long)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ThreadsStatusRecord record = new ThreadsStatusRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("ThreadsStatusRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ThreadsStatusRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("ThreadsStatusRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("ThreadsStatusRecord.threadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.daemonThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getDaemonThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.peakThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getPeakThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.totalStartedThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTotalStartedThreadCount());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 7.", 7, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type String must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type String must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type Long must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type Long must be not null.", values[4]); 
			Assert.assertNotNull("Array value [5] of type Long must be not null.", values[5]); 
			Assert.assertNotNull("Array value [6] of type Long must be not null.", values[6]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type String", values[1] instanceof String);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type String", values[2] instanceof String);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type Long", values[3] instanceof Long);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type Long", values[4] instanceof Long);
			Assert.assertTrue("Type of array value [5] " + values[5].getClass().getCanonicalName() + " does not match the desired type Long", values[5] instanceof Long);
			Assert.assertTrue("Type of array value [6] " + values[6].getClass().getCanonicalName() + " does not match the desired type Long", values[6] instanceof Long);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[0]
					);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[1]
			);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[2]
			);
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[3]
					);
			Assert.assertEquals("Array value [4] " + values[4] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[4]
					);
			Assert.assertEquals("Array value [5] " + values[5] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[5]
					);
			Assert.assertEquals("Array value [6] " + values[6] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[6]
					);
		}
	}
	
	/**
	 * Tests {@link ThreadsStatusRecord#TestThreadsStatusRecord(long, string, string, long, long, long, long)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ThreadsStatusRecord record = new ThreadsStatusRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("ThreadsStatusRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ThreadsStatusRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("ThreadsStatusRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("ThreadsStatusRecord.threadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.daemonThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getDaemonThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.peakThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getPeakThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.totalStartedThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTotalStartedThreadCount());
		}
	}
	
	/**
	 * Tests {@link ThreadsStatusRecord#TestThreadsStatusRecord(long, string, string, long, long, long, long)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ThreadsStatusRecord record = new ThreadsStatusRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("ThreadsStatusRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ThreadsStatusRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("ThreadsStatusRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("ThreadsStatusRecord.threadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.daemonThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getDaemonThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.peakThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getPeakThreadCount());
			Assert.assertEquals("ThreadsStatusRecord.totalStartedThreadCount values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTotalStartedThreadCount());
		}
	}
	
	@Test
	public void testEquality() {
		int i = 0;
		ThreadsStatusRecord oneRecord = new ThreadsStatusRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
		i = 0;
		ThreadsStatusRecord copiedRecord = new ThreadsStatusRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
		
		Assert.assertEquals(oneRecord, copiedRecord);
	}	
	
	@Test
	public void testUnequality() {
		int i = 0;
		ThreadsStatusRecord oneRecord = new ThreadsStatusRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
		i = 2;
		ThreadsStatusRecord anotherRecord = new ThreadsStatusRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
		
		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
