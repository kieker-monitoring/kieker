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

package kieker.test.common.junit.record.controlflow;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.io.DefaultValueDeserializer;
import kieker.common.record.io.DefaultValueSerializer;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;

/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 *
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
public class TestOperationExecutionRecordConstructors extends AbstractKiekerTest {

	public TestOperationExecutionRecordConstructors() {
		// empty default constructor
	}

	/**
	 * Tests {@link OperationExecutionRecord#OperationExecutionRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testSignatureStringSessionIDTraceIDTinToutEoiEss() { // NOPMD (assert missing)
		final String sessionId = "IaYyf8m9B";
		final long traceId = 3486095; // any number will do
		final String hostname = "srv-gvNH6CgYFS";
		final long tin = 33444; // any number will do
		final long tout = 33449; // any number will do
		final int eoi = BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_EOI;
		final int ess = BookstoreOperationExecutionRecordFactory.EXEC0_0__BOOKSTORE_SEARCHBOOK_ESS;
		final OperationExecutionRecord opExecutionRecord = new OperationExecutionRecord(BookstoreOperationExecutionRecordFactory.FQ_SIGNATURE_BOOKSTORE_SEARCH_BOOK,
				sessionId, traceId,
				tin, tout, hostname, eoi, ess);

		this.checkTraceId(opExecutionRecord, traceId);
		this.checkTinTout(opExecutionRecord, tin, tout);
		this.checkEoiEss(opExecutionRecord, eoi, ess);
		this.checkHostName(opExecutionRecord, hostname);
		this.checkSessionId(opExecutionRecord, sessionId);

		this.checkToFromArrayAllFields(opExecutionRecord);
		this.checkToFromBinaryAllFields(opExecutionRecord);
	}

	private void checkSessionId(final OperationExecutionRecord opExecutionRecord, final String expectedSessionId) {
		Assert.assertEquals("Unexpected session ID", expectedSessionId, opExecutionRecord.getSessionId());
	}

	private void checkHostName(final OperationExecutionRecord opExecutionRecord, final String expectedHostName) {
		Assert.assertEquals("Unexpected host name", expectedHostName, opExecutionRecord.getHostname());
	}

	private void checkTraceId(final OperationExecutionRecord opExecutionRecord, final long expectedTraceId) {
		Assert.assertEquals("Unexpected trace ID", expectedTraceId, opExecutionRecord.getTraceId());
	}

	private void checkTinTout(final OperationExecutionRecord opExecutionRecord, final long tin, final long tout) {
		Assert.assertEquals("tin's differ", tin, opExecutionRecord.getTin());
		Assert.assertEquals("tout's differ", tout, opExecutionRecord.getTout());
	}

	private void checkEoiEss(final OperationExecutionRecord opExecutionRecord, final int eoi, final int ess) {
		Assert.assertEquals("eoi's differ", eoi, opExecutionRecord.getEoi());
		Assert.assertEquals("ess's differ", ess, opExecutionRecord.getEss());
	}

	private void checkToFromArrayAllFields(final OperationExecutionRecord opExecutionRecord) {
		final Object[] serializedRecord = opExecutionRecord.toArray();
		final OperationExecutionRecord deserializedRecord = new OperationExecutionRecord(serializedRecord);

		Assert.assertEquals("Records not equal (array)", opExecutionRecord, deserializedRecord);

		this.checkEoiEss(deserializedRecord, opExecutionRecord.getEoi(), opExecutionRecord.getEss());
		this.checkHostName(deserializedRecord, opExecutionRecord.getHostname());
		this.checkSessionId(deserializedRecord, opExecutionRecord.getSessionId());
		this.checkTinTout(deserializedRecord, opExecutionRecord.getTin(), opExecutionRecord.getTout());
		this.checkTraceId(deserializedRecord, opExecutionRecord.getTraceId());
	}

	private void checkToFromBinaryAllFields(final OperationExecutionRecord opExecutionRecord) {
		final IRegistry<String> stringRegistry = new Registry<String>();
		final ByteBuffer buffer = ByteBuffer.allocate(OperationExecutionRecord.SIZE);
		opExecutionRecord.serialize(DefaultValueSerializer.create(buffer, stringRegistry));
		buffer.flip();
		final OperationExecutionRecord deserializedRecord = new OperationExecutionRecord(DefaultValueDeserializer.create(buffer, stringRegistry));

		Assert.assertEquals("Records not equal (binary)", opExecutionRecord, deserializedRecord);

		this.checkEoiEss(deserializedRecord, opExecutionRecord.getEoi(), opExecutionRecord.getEss());
		this.checkHostName(deserializedRecord, opExecutionRecord.getHostname());
		this.checkSessionId(deserializedRecord, opExecutionRecord.getSessionId());
		this.checkTinTout(deserializedRecord, opExecutionRecord.getTin(), opExecutionRecord.getTout());
		this.checkTraceId(deserializedRecord, opExecutionRecord.getTraceId());
	}
}
