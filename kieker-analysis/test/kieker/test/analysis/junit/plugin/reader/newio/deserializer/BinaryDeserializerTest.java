/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysis.junit.plugin.reader.newio.deserializer;

import java.nio.ByteBuffer;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import kieker.analysis.plugin.reader.newio.deserializer.BinaryDeserializer;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.misc.KiekerMetadataRecord;

/**
 * Tests for the binary deserializer.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class BinaryDeserializerTest {

	public BinaryDeserializerTest() {
		// Default serializer
	}

	/**
	 * Tests the decoding of a single record and its associated metadata record.
	 */
	@Test
	public void testDecodeSingleRecord() {
		final String dataBase64 = "S0lLQURGTFQAAAAAFJlRZxLkqNMAAAABAAAAAgAAAAMAAAABAAAAAAAAAAAAAAAABAAAAAAAAAABAAAABRS"
				+ "ZUWcS3PyIAAAABgAAAAcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIAAAAAAAAAAEJLmtpZWtlci5jb21tb24ucmVjb3JkLm1p"
				+ "c2MuS2lla2VyTWV0YWRhdGFSZWNvcmQNMS4xMy1TTkFQU0hPVAZLSUVLRVIHaWRlYXBhZAtOQU5PU0VDT05EUzlraWVrZXIuY29tb"
				+ "W9uLnJlY29yZC5jb250cm9sZmxvdy5PcGVyYXRpb25FeGVjdXRpb25SZWNvcmQEb3AoKQdTRVNTLUlEBGhvc3QAAABp";

		final IMonitoringRecord expectedMetadataRecord = new KiekerMetadataRecord("1.13-SNAPSHOT", "KIEKER", "ideapad", 1, false, 0, "NANOSECONDS", 1);
		expectedMetadataRecord.setLoggingTimestamp(1484307055335745747L);
		final IMonitoringRecord expectedOperationExecutionRecord = new OperationExecutionRecord("op()", "SESS-ID", 0, 0, 0, "host", 0, 1);
		expectedOperationExecutionRecord.setLoggingTimestamp(1484307055335242888L);
		final List<IMonitoringRecord> expected = ImmutableList.of(expectedMetadataRecord, expectedOperationExecutionRecord);

		final List<IMonitoringRecord> actual = this.decodeRecords(dataBase64);

		Assert.assertEquals(expected, actual);
	}

	private List<IMonitoringRecord> decodeRecords(final String dataBase64) {
		final byte[] rawData = DatatypeConverter.parseBase64Binary(dataBase64);
		final BinaryDeserializer deserializer = new BinaryDeserializer(null, null);
		final ByteBuffer rawDataBuffer = ByteBuffer.wrap(rawData);

		return deserializer.deserializeRecords(rawDataBuffer, rawData.length);
	}

}
