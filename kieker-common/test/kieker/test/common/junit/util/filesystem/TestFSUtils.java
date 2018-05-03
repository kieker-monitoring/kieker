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

package kieker.test.common.junit.util.filesystem;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.util.filesystem.FSUtil;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public class TestFSUtils extends AbstractKiekerTest {

	private static final String DECODED_STRING = "Hallo\nTest\r asjd \\ asd \t";
	private static final String ENCODED_STRING = "Hallo\\nTest\\r asjd \\\\ asd \t";
	private static final String IRRELEVANT_STRING = "Hallo Test asjd asd \t";

	/**
	 * Default constructor.
	 */
	public TestFSUtils() {
		// empty default constructor
	}

	/**
	 * Test the method {@link FSUtil#encodeNewline(String)}.
	 */
	@Test
	public final void testEncodeNewline() {
		Assert.assertSame("", FSUtil.encodeNewline(""));
		Assert.assertSame(IRRELEVANT_STRING, FSUtil.encodeNewline(IRRELEVANT_STRING));
		Assert.assertEquals(ENCODED_STRING, FSUtil.encodeNewline(DECODED_STRING));
		Assert.assertNotSame(ENCODED_STRING, FSUtil.encodeNewline(DECODED_STRING));
	}

	/**
	 * Test the method {@link FSUtil#decodeNewline(String)}.
	 */
	@Test
	public final void testDecodeNewline() {
		Assert.assertSame("", FSUtil.encodeNewline(""));
		Assert.assertSame(IRRELEVANT_STRING, FSUtil.encodeNewline(IRRELEVANT_STRING));
		Assert.assertEquals(DECODED_STRING, FSUtil.decodeNewline(ENCODED_STRING));
		Assert.assertNotSame(DECODED_STRING, FSUtil.decodeNewline(ENCODED_STRING));
		Assert.assertSame(DECODED_STRING, FSUtil.decodeNewline(DECODED_STRING));
	}

	/**
	 * Test the methods {@link FSUtil#encodeNewline(String)} and {@link FSUtil#decodeNewline(String)} toegether.
	 */
	@Test
	public final void testEncodeDecodeNewline() {
		Assert.assertEquals(DECODED_STRING, FSUtil.decodeNewline(FSUtil.encodeNewline(DECODED_STRING)));
		Assert.assertNotSame(DECODED_STRING, FSUtil.decodeNewline(FSUtil.encodeNewline(DECODED_STRING)));
		Assert.assertEquals(ENCODED_STRING, FSUtil.decodeNewline(FSUtil.encodeNewline(ENCODED_STRING)));
		Assert.assertNotSame(ENCODED_STRING, FSUtil.decodeNewline(FSUtil.encodeNewline(ENCODED_STRING)));
	}
}
