/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.misc.KiekerMetadataRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public class TestGeneratedKiekerMetadataRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedKiekerMetadataRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link KiekerMetadataRecord#TestKiekerMetadataRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
	for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			KiekerMetadataRecord record = new KiekerMetadataRecord(STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()), BOOLEAN_VALUES.get(i % BOOLEAN_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("KiekerMetadataRecord.version values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"1.10":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVersion());
			Assert.assertEquals("KiekerMetadataRecord.controllerName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"<no-controller-name>":STRING_VALUES.get(i % STRING_VALUES.size()), record.getControllerName());
			Assert.assertEquals("KiekerMetadataRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"<no-hostname>":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("KiekerMetadataRecord.experimentId values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getExperimentId());
			Assert.assertEquals("KiekerMetadataRecord.debugMode values are not equal.", (boolean) BOOLEAN_VALUES.get(i % BOOLEAN_VALUES.size()), record.isDebugMode());
			Assert.assertEquals("KiekerMetadataRecord.timeOffset values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimeOffset());
			Assert.assertEquals("KiekerMetadataRecord.timeUnit values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"NANOSECONDS":STRING_VALUES.get(i % STRING_VALUES.size()), record.getTimeUnit());
			Assert.assertEquals("KiekerMetadataRecord.numberOfRecords values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNumberOfRecords());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 8.", 8, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type String must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type String must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type String must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type Integer must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type Boolean must be not null.", values[4]); 
			Assert.assertNotNull("Array value [5] of type Long must be not null.", values[5]); 
			Assert.assertNotNull("Array value [6] of type String must be not null.", values[6]); 
			Assert.assertNotNull("Array value [7] of type Long must be not null.", values[7]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type String", values[0] instanceof String);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type String", values[1] instanceof String);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type String", values[2] instanceof String);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type Integer", values[3] instanceof Integer);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type Boolean", values[4] instanceof Boolean);
			Assert.assertTrue("Type of array value [5] " + values[5].getClass().getCanonicalName() + " does not match the desired type Long", values[5] instanceof Long);
			Assert.assertTrue("Type of array value [6] " + values[6].getClass().getCanonicalName() + " does not match the desired type String", values[6] instanceof String);
			Assert.assertTrue("Type of array value [7] " + values[7].getClass().getCanonicalName() + " does not match the desired type Long", values[7] instanceof Long);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"1.10":STRING_VALUES.get(i % STRING_VALUES.size()), values[0]
			);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"<no-controller-name>":STRING_VALUES.get(i % STRING_VALUES.size()), values[1]
			);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"<no-hostname>":STRING_VALUES.get(i % STRING_VALUES.size()), values[2]
			);
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + INT_VALUES.get(i % INT_VALUES.size()),
				(int) INT_VALUES.get(i % INT_VALUES.size()), (int) (Integer)values[3]
					);
			Assert.assertEquals("Array value [4] " + values[4] + " does not match the desired value " + BOOLEAN_VALUES.get(i % BOOLEAN_VALUES.size()),
				(boolean) BOOLEAN_VALUES.get(i % BOOLEAN_VALUES.size()), (boolean) (Boolean)values[4]
					);
			Assert.assertEquals("Array value [5] " + values[5] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				(long) LONG_VALUES.get(i % LONG_VALUES.size()), (long) (Long)values[5]
					);
			Assert.assertEquals("Array value [6] " + values[6] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"NANOSECONDS":STRING_VALUES.get(i % STRING_VALUES.size()), values[6]
			);
			Assert.assertEquals("Array value [7] " + values[7] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				(long) LONG_VALUES.get(i % LONG_VALUES.size()), (long) (Long)values[7]
					);
		}
	}
	
	/**
	 * Tests {@link KiekerMetadataRecord#TestKiekerMetadataRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			KiekerMetadataRecord record = new KiekerMetadataRecord(STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()), BOOLEAN_VALUES.get(i % BOOLEAN_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("KiekerMetadataRecord.version values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"1.10":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVersion());
			Assert.assertEquals("KiekerMetadataRecord.controllerName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"<no-controller-name>":STRING_VALUES.get(i % STRING_VALUES.size()), record.getControllerName());
			Assert.assertEquals("KiekerMetadataRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"<no-hostname>":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("KiekerMetadataRecord.experimentId values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getExperimentId());
			Assert.assertEquals("KiekerMetadataRecord.debugMode values are not equal.", (boolean) BOOLEAN_VALUES.get(i % BOOLEAN_VALUES.size()), record.isDebugMode());
			Assert.assertEquals("KiekerMetadataRecord.timeOffset values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimeOffset());
			Assert.assertEquals("KiekerMetadataRecord.timeUnit values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"NANOSECONDS":STRING_VALUES.get(i % STRING_VALUES.size()), record.getTimeUnit());
			Assert.assertEquals("KiekerMetadataRecord.numberOfRecords values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNumberOfRecords());
		}
	}
	
	/**
	 * Tests {@link KiekerMetadataRecord#TestKiekerMetadataRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			KiekerMetadataRecord record = new KiekerMetadataRecord(STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()), BOOLEAN_VALUES.get(i % BOOLEAN_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("KiekerMetadataRecord.version values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"1.10":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVersion());
			Assert.assertEquals("KiekerMetadataRecord.controllerName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"<no-controller-name>":STRING_VALUES.get(i % STRING_VALUES.size()), record.getControllerName());
			Assert.assertEquals("KiekerMetadataRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"<no-hostname>":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("KiekerMetadataRecord.experimentId values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getExperimentId());
			Assert.assertEquals("KiekerMetadataRecord.debugMode values are not equal.", (boolean) BOOLEAN_VALUES.get(i % BOOLEAN_VALUES.size()), record.isDebugMode());
			Assert.assertEquals("KiekerMetadataRecord.timeOffset values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimeOffset());
			Assert.assertEquals("KiekerMetadataRecord.timeUnit values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"NANOSECONDS":STRING_VALUES.get(i % STRING_VALUES.size()), record.getTimeUnit());
			Assert.assertEquals("KiekerMetadataRecord.numberOfRecords values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNumberOfRecords());
		}
	}
}
