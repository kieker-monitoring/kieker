/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;

/**
 * @author Danish Manzoor
 *
 * @since 1.15
 */
@RunWith(PowerMockRunner.class)
// as we want to mock System classes, we must prepare the test class and not the
// to be mocked classes, as this is done for other static classes
// https://github.com/powermock/powermock/wiki/Mock-System
@PrepareForTest({ TextMapFileHandler.class }) // NOCS
@PowerMockIgnore({"com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*", "javax.management.*"})
public class TextMapFileHandlerTest {

	private static final String TEST_PATH = "this/is/a/test/path";

	public TextMapFileHandlerTest() {
		// test class
	}

	/**
	 * Test method for
	 * {@link kieker.monitoring.writer.filesystem.TextMapFileHandler#create(java.nio.file.Path, java.nio.charset.Charset)}.
	 */
	@Test
	public void testCreate() {
		PowerMockito.mockStatic(Files.class);
		final Path location = Paths.get(TEST_PATH);
		final Charset charset = Charset.defaultCharset();
		// defining dummy writer for mocking purposes
		final Writer dummyWriter = new DummyWriter();
		final BufferedWriter writer = new BufferedWriter(dummyWriter);

		// mocking file.newBufferedWriter with our dummy writer
		try {
			PowerMockito.when(
					Files.newBufferedWriter(location, charset))
					.thenReturn(writer);
		} catch (final IOException e) {
			Assert.fail(e.getLocalizedMessage());
		}

		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();

		final TextMapFileHandler handler = new TextMapFileHandler(configuration);

		handler.create(location, charset);
		// adding dummy record
		handler.add(0, "my.event.EventClass");
		Assert.assertEquals("String doesnot match", "$0=my.event.EventClass",
				((DummyWriter) dummyWriter).getBufferAsString());
		// adding dummy record 2
		handler.add(1, "my.event.EventClass1");
		Assert.assertEquals("String doesnot match", "$1=my.event.EventClass1",
				((DummyWriter) dummyWriter).getBufferAsString());

		// closing
		handler.close();

		// check whether close was called.

	}

}
