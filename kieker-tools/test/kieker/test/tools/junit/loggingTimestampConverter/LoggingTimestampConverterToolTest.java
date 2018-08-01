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

package kieker.test.tools.junit.loggingTimestampConverter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.tools.loggingTimestampConverter.LoggingTimestampConverterTool;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public class LoggingTimestampConverterToolTest extends AbstractKiekerTest {

	private ByteArrayOutputStream testOutputBuffer;
	private PrintStream testOutputStream;
	private PrintStream systemOutputStream;

	public LoggingTimestampConverterToolTest() {
		// Nothing to do here
	}

	@Before
	public final void redirectOutputStream() throws UnsupportedEncodingException {
		this.testOutputBuffer = new ByteArrayOutputStream();
		this.testOutputStream = new PrintStream(this.testOutputBuffer, true, "UTF-8");
		this.systemOutputStream = System.out;

		System.setOut(this.testOutputStream);
	}

	@After
	public final void correctOutputStream() {
		this.testOutputStream.close();

		System.setOut(this.systemOutputStream);
	}

	@Test
	public void testSingleTimestamp() throws UnsupportedEncodingException {
		LoggingTimestampConverterTool.main(new String[] { "-t", "1283156545583183369" });

		final String[] lines = this.testOutputBuffer.toString("UTF-8").split(System.getProperty("line.separator"));

		Assert.assertEquals(3, lines.length);
		// First line logging output
		Assert.assertTrue(lines[1].matches(
				"1283156545583183369: Mon, 30 Aug 2010 08:22:25.583 \\+0000 \\(UTC\\) \\(.* \\(local time\\)\\)"));
		// Last line logging output
	}

	@Test
	public void testMultipleTimestamps() throws UnsupportedEncodingException {
		LoggingTimestampConverterTool.main(new String[] { "-t", "0", "1283156545583183369" });

		final String[] lines = this.testOutputBuffer.toString("UTF-8").split(System.getProperty("line.separator"));

		Assert.assertEquals(4, lines.length);
		// First line logging output
		Assert.assertTrue(
				lines[1].matches("0: Thu, 1 Jan 1970 00:00:00.000 \\+0000 \\(UTC\\) \\(.* \\(local time\\)\\)"));
		Assert.assertTrue(lines[2].matches(
				"1283156545583183369: Mon, 30 Aug 2010 08:22:25.583 \\+0000 \\(UTC\\) \\(.* \\(local time\\)\\)"));
		// Last line logging output
	}

}
