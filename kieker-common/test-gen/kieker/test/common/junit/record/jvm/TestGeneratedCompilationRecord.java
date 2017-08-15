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

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.jvm.CompilationRecord;
import kieker.test.common.junit.AbstractGeneratedKiekerTest;

/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class TestGeneratedCompilationRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedCompilationRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link CompilationRecord#TestCompilationRecord(long, string, string, string, long)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			CompilationRecord record = new CompilationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("CompilationRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("CompilationRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("CompilationRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("CompilationRecord.jitCompilerName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getJitCompilerName());
			Assert.assertEquals("CompilationRecord.totalCompilationTimeMS values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTotalCompilationTimeMS());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 5.", 5, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type String must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type String must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type String must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type Long must be not null.", values[4]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type String", values[1] instanceof String);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type String", values[2] instanceof String);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type String", values[3] instanceof String);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type Long", values[4] instanceof Long);
								
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
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[3]
			);
			Assert.assertEquals("Array value [4] " + values[4] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[4]
					);
		}
	}
	
	/**
	 * Tests {@link CompilationRecord#TestCompilationRecord(long, string, string, string, long)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			CompilationRecord record = new CompilationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("CompilationRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("CompilationRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("CompilationRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("CompilationRecord.jitCompilerName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getJitCompilerName());
			Assert.assertEquals("CompilationRecord.totalCompilationTimeMS values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTotalCompilationTimeMS());
		}
	}
	
	/**
	 * Tests {@link CompilationRecord#TestCompilationRecord(long, string, string, string, long)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			CompilationRecord record = new CompilationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("CompilationRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("CompilationRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("CompilationRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("CompilationRecord.jitCompilerName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getJitCompilerName());
			Assert.assertEquals("CompilationRecord.totalCompilationTimeMS values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTotalCompilationTimeMS());
		}
	}
	
	@Test
	public void testEquality() {
		int i = 0;
		CompilationRecord oneRecord = new CompilationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
		i = 0;
		CompilationRecord copiedRecord = new CompilationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
		
		Assert.assertEquals(oneRecord, copiedRecord);
	}	
	
	@Test
	public void testUnequality() {
		int i = 0;
		CompilationRecord oneRecord = new CompilationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
		i = 2;
		CompilationRecord anotherRecord = new CompilationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
		
		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
