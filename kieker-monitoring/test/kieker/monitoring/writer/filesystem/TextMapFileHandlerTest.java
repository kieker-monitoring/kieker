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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Danish Manzoor
 *
 */
//@RunWith(PowerMockRunner.class)
//@RunWith(PowerMockRunner.class)
//@PrepareForTest(TextMapFileHandler.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({ File.class })
public class TextMapFileHandlerTest {

	private PrintWriter printWriter;

	/**
	 * Test method for
	 * {@link kieker.monitoring.writer.filesystem.TextMapFileHandler#TextMapFileHandler(kieker.common.configuration.Configuration)}.
	 */
	@Test
	public void testTextMapFileHandler() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link kieker.monitoring.writer.filesystem.TextMapFileHandler#create(java.nio.file.Path, java.nio.charset.Charset)}.
	 */

	@Test

	public void testCreate() {
		final Path path = null;
		final Charset cs = null;

//		final Files fileMock = EasyMock.createMock(Files.class);
//
//		try {
//			final Writer w = fileMock.newBufferedWriter(path, cs, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
//			this.printWriter = new PrintWriter(w);
//
//		} catch (final IOException e) {
//			throw new IllegalStateException("Error on mock for Kieker's mapping file.", e);
//		}

		final Files fileMock = PowerMock.createMock(Files.class);
		try {
			PowerMockito.whenNew(Files.class).withAnyArguments().thenReturn(fileMock);

		} catch (final Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
//			this.printWriter = new PrintWriter(fileMock.createTempFile("abc", null, new File("D:/")));
//			final Writer w = Files.newBufferedWriter(fileMock.createFile(path)); this is what I'm trying to do
			final Writer w = Files.newBufferedWriter(Files.createFile(path));
			this.printWriter = new PrintWriter(w);

		} catch (final IOException e) {
			throw new IllegalStateException("Error on mock for Kieker's mapping file.", e);
		}

		// this is what I found on
		// https://stackoverflow.com/questions/16035365/is-it-possible-to-use-powermock-to-mock-new-file-creation/16118611
		// using easymock

		// first, create a mock for File
//		final File fileMock = EasyMock.createMock(File.class);
//		EasyMock.expect(fileMock.getAbsolutePath()).andReturn("/my/fake/file/path");
////		EasyMock.replay(fileMock);
//
//		// then return the mocked object if the constructor is invoked
//		final Class<?>[] parameterTypes = new Class[] { String.class };
//		try {
//			PowerMock.expectNew(File.class, parameterTypes, EasyMock.isA(String.class)).andReturn(fileMock);
//		} catch (final Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
////		PowerMock.replay(File.class);
//		PowerMock.replay(fileMock, File.class);
//		// try constructing a real File and check if the mock kicked in
//		final String mockedFilePath = new File("/real/path/for/file").getAbsolutePath();
//		Assert.assertEquals("/my/fake/file/path", mockedFilePath);
//
//		

	}

	/**
	 * Test method for
	 * {@link kieker.monitoring.writer.filesystem.TextMapFileHandler#close()}.
	 */
	@Test
	public void testClose() {
		Assert.fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link kieker.monitoring.writer.filesystem.TextMapFileHandler#add(int, java.lang.String)}.
	 */
	@Test
	public void testAdd() {
		Assert.fail("Not yet implemented"); // TODO
	}

}
