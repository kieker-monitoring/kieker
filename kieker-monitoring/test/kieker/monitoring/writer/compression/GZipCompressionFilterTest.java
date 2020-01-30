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
package kieker.monitoring.writer.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * @author Danish Manzoor
 *
 * @since 1.14
 */
public class GZipCompressionFilterTest {

	public GZipCompressionFilterTest() {
		// nothing to do
	}

	/**
	 * Test method for
	 * {@link kieker.monitoring.writer.compression.GZipCompressionFilter#GZipCompressionFilter(kieker.common.configuration.Configuration)}.
	 */
	@Test
	public void testGZipCompressionFilter() {
		// The constructor is empty.
	}

	/**
	 * Test method for
	 * {@link kieker.monitoring.writer.compression.GZipCompressionFilter#chainOutputStream(java.io.OutputStream, java.nio.file.Path)}.
	 */
	@Test
	public void testChainOutputStream() {

		final String inputStr = "Hello World";
		final byte[] inputData = inputStr.getBytes();
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		// Stream replicating constructor in test
		final ByteArrayOutputStream byteArrayOutputStreamR = new ByteArrayOutputStream();
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		final GZipCompressionFilter unit = new GZipCompressionFilter(configuration);
		final Path path = Paths.get("");

		try {
			// Passing inflated output stream
			final OutputStream value = unit.chainOutputStream(byteArrayOutputStream, path);
			// Writing byte array to stream
			value.write(inputData);
			// Closing stream
			value.close();

			// Replicating method to be tested
			final GZIPOutputStream gzip = new GZIPOutputStream(byteArrayOutputStreamR);
			gzip.write(inputData);
			gzip.close();

			// Checking if byteArrayOutputStreamR is equal to byteArrayOutputStream
			Assert.assertArrayEquals("Expected result does not match with actual result",
					byteArrayOutputStreamR.toByteArray(), byteArrayOutputStream.toByteArray());

		} catch (final IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Test method for
	 * {@link kieker.monitoring.writer.compression.GZipCompressionFilter#getExtension()}.
	 */
	@Test
	public void testGetExtension() {
		// Expected extension
		final String type = ".gz";
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		final GZipCompressionFilter unit = new GZipCompressionFilter(configuration);
		// Extension Returned
		final String value = unit.getExtension();
		// Checking expected and returned extension
		Assert.assertEquals("Wrong extension returned", type, value);
	}

}
