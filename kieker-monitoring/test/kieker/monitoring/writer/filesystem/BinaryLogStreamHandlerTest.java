/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.charset.Charset;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.registry.IRegistryListener;
import kieker.common.registry.writer.WriterRegistry;
import kieker.monitoring.writer.compression.ICompressionFilter;
import kieker.monitoring.writer.compression.NoneCompressionFilter;

/**
 * @author Danish Manzoor
 *
 * @since 1.14
 */
public class BinaryLogStreamHandlerTest implements IRegistryListener<String> {

	/** Initializing parameters for TextlogStreamhandler object instance. */
	private final Charset charset = Charset.defaultCharset();
	private final ICompressionFilter compressionFilter = new NoneCompressionFilter(null);
	private final WriterRegistry reg = new WriterRegistry(this);

	public BinaryLogStreamHandlerTest() {
		// nothing to be done here.
	}

	@Test
	public void testClose() {
		// test not required
	}

	@Test
	public void testSerializeBufferTooTiny() throws IOException {
		final IMonitoringRecord record = new OperationExecutionRecord("testing", "abc", 1, 0, 1, "localhost", 123, 456);

		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final BinaryLogStreamHandler handler = new BinaryLogStreamHandler(true, 10,
				this.charset, this.compressionFilter, this.reg);

		try {
			// channel initializing
			handler.initialize(byteArrayOutputStream, Paths.get("test-filename"));
			// serializing test record
			handler.serialize(record, 2);
			Assert.fail("Code should trigger an exception and not reach this point.");
		} catch (final BufferOverflowException e) { // NOPMD
			// as buffer size is to small it will always catch exception
		} finally {
			byteArrayOutputStream.close();
		}
	}

	@Test
	public void testSerializeBufferSufficient() throws IOException {
		final IMonitoringRecord record = new OperationExecutionRecord("testing", "abc", 1, 0, 1, "localhost", 123, 456);

		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final BinaryLogStreamHandler handler = new BinaryLogStreamHandler(true, 10240,
				this.charset, this.compressionFilter, this.reg);

		try {
			// channel initializing
			handler.initialize(byteArrayOutputStream, Paths.get("test-filename"));
			// serializing test record
			handler.serialize(record, 2);
			// may add a test here
		} catch (final BufferOverflowException e) {
			// as buffer size is to small it will always catch exception on first execution
			Assert.fail("Buffer should have been sufficient, overflow error.");
		} finally {
			byteArrayOutputStream.close();
		}
	}

	@Test
	public void testBinaryLogStreamHandler() {
		// test not required
	}

	@Override
	public void onNewRegistryEntry(final String value, final int id) {
		// ignore, as we only want to mock the serialization
	}

}
