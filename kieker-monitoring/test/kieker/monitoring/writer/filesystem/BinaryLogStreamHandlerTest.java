/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.charset.Charset;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.registry.writer.WriterRegistry;
import kieker.monitoring.writer.compression.ICompressionFilter;
import kieker.monitoring.writer.compression.NoneCompressionFilter;

/**
 * @author Danish Manzoor
 *
 */

public class BinaryLogStreamHandlerTest {

//	Initializing parameters for TextlogStreamhandler object instance
	Boolean flushLogFile = true;
	Integer bufferSize = 10;
	int runCheck = 0;
	final Charset charset = Charset.defaultCharset();
	final ICompressionFilter compressionFilter = new NoneCompressionFilter(null);
	WriterRegistry reg = new WriterRegistry(null);
	final IMonitoringRecord record = new OperationExecutionRecord("testing", "abc", 1, 0, 1, "localhost", 123, 456);

	@Test
	public void testClose() {
		// test not required
	}

	@Test
	public void testSerialize() throws IOException {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final BinaryLogStreamHandler handler = new BinaryLogStreamHandler(this.flushLogFile, this.bufferSize,
				this.charset, this.compressionFilter, this.reg);

		try {
			// channel initializing
			handler.initialize(byteArrayOutputStream, Paths.get("test-filename"));
			// serializing test record
			handler.serialize(this.record, 2);
			Assert.assertEquals("String doesnot match", "$2;-1;testing;abc;1;0;1;localhost;123;456",
					byteArrayOutputStream.toString().trim());
			System.out.println(byteArrayOutputStream.toString());

		} catch (final Exception e) {
			// as buffer size is to small it will always catch exception on first execution
			System.out.println("BufferOverflow Exception Occurred");
		} finally {
			// runCheck a static variable to maintain execution count
			if (this.runCheck == 0) {
				this.bufferSize = 10240;
				this.runCheck++;
				byteArrayOutputStream.close();
				this.testSerialize();
			}
		}
	}

	@Test
	public void testBinaryLogStreamHandler() {
		// test not required
	}

}
