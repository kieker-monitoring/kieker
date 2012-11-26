/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.junit.util;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.util.StringUtils;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Jan Waller
 */
public class TestStringUtils extends AbstractKiekerTest {

	private static final String decodedString = "Hallo\nTest\r asjd \\ asd \t";
	private static final String encodedString = "Hallo\\nTest\\r asjd \\\\ asd \t";
	private static final String irrelevantString = "Hallo Test asjd asd \t";

	@Test
	public final void testEncodeNewline() {
		Assert.assertSame("", StringUtils.encodeNewline(""));
		Assert.assertSame(irrelevantString, StringUtils.encodeNewline(irrelevantString));
		Assert.assertEquals(encodedString, StringUtils.encodeNewline(decodedString));
		Assert.assertNotSame(encodedString, StringUtils.encodeNewline(decodedString));
	}

	@Test
	public final void testDecodeNewline() {
		Assert.assertSame("", StringUtils.encodeNewline(""));
		Assert.assertSame(irrelevantString, StringUtils.encodeNewline(irrelevantString));
		Assert.assertEquals(decodedString, StringUtils.decodeNewline(encodedString));
		Assert.assertNotSame(decodedString, StringUtils.decodeNewline(encodedString));
		Assert.assertSame(decodedString, StringUtils.decodeNewline(decodedString));
	}

	@Test
	public final void testEncodeDecodeNewline() {
		Assert.assertEquals(decodedString, StringUtils.decodeNewline(StringUtils.encodeNewline(decodedString)));
		Assert.assertNotSame(decodedString, StringUtils.decodeNewline(StringUtils.encodeNewline(decodedString)));
		Assert.assertEquals(encodedString, StringUtils.decodeNewline(StringUtils.encodeNewline(encodedString)));
		Assert.assertNotSame(encodedString, StringUtils.decodeNewline(StringUtils.encodeNewline(encodedString)));
	}
}
