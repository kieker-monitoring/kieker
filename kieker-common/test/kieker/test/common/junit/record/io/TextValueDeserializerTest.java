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
package kieker.test.common.junit.record.io;

import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.io.TextValueDeserializer;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * Test text value serializer.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public class TextValueDeserializerTest extends AbstractKiekerTest { // NOCS NOPMD test, no constructor needed

	@Test
	public void testCreateAndReadKiekerMonitoringRecord() {
		final String string = "$0;1521828677048314440;1.14-SNAPSHOT;SingleCatBuyer;stockholm;1;false;0;NANOSECONDS;0";
		final CharBuffer buffer = CharBuffer.wrap(string.toCharArray());

		final char lead = buffer.get();

		Assert.assertEquals("Lead char was not $", lead, '$');

		final TextValueDeserializer deserializer = TextValueDeserializer.create(buffer);

		Assert.assertEquals("record id error", deserializer.getInt(), 0);
		Assert.assertEquals("logging timestamp error", deserializer.getLong(), 1521828677048314440L);
		Assert.assertEquals("version error", deserializer.getString(), "1.14-SNAPSHOT");
		Assert.assertEquals("controller name error", deserializer.getString(), "SingleCatBuyer");
		Assert.assertEquals("hostname error", deserializer.getString(), "stockholm");
		Assert.assertEquals("experiment id error", deserializer.getInt(), 1);
		Assert.assertEquals("debug mode error", deserializer.getBoolean(), false);
		Assert.assertEquals("time offset error", deserializer.getLong(), 0);
		Assert.assertEquals("time unit error", deserializer.getString(), "NANOSECONDS");
		Assert.assertEquals("number of records error", deserializer.getLong(), 0);
	}

	/**
	 *
	 * @author Reiner Jung
	 *
	 * @since 1.14
	 */
	enum ETestExample {
		NO, YES
	}

	@Test
	public void testCreateAndReadCheckEveryMethod() {
		final String string = "false;0;1;2;3;4;5.5;6.6;a line of text with a semicolon(\\;);0";
		final CharBuffer buffer = CharBuffer.wrap(string.toCharArray());

		final TextValueDeserializer deserializer = TextValueDeserializer.create(buffer);

		Assert.assertEquals("boolean error", deserializer.getBoolean(), false);
		Assert.assertEquals("byte error", deserializer.getByte(), 0);
		Assert.assertEquals("char error", deserializer.getChar(), '1');
		Assert.assertEquals("short error", deserializer.getShort(), 2);
		Assert.assertEquals("int error", deserializer.getInt(), 3);
		Assert.assertEquals("long error", deserializer.getLong(), 4);
		Assert.assertEquals("float error", deserializer.getFloat(), 5.5, 0.00001);
		Assert.assertEquals("double error", deserializer.getDouble(), 6.6, 0.00001);
		Assert.assertEquals("string error", deserializer.getString(), "a line of text with a semicolon(\\;)");
		Assert.assertEquals("enum error", deserializer.getEnumeration(ETestExample.class), ETestExample.NO);
	}

	@Test
	public void testLastFieldEmpty() {
		final String string = "false;0;1;2;3;4;5.5;6.6;a line of text with a semicolon(\\;);";
		final CharBuffer buffer = CharBuffer.wrap(string.toCharArray());

		final TextValueDeserializer deserializer = TextValueDeserializer.create(buffer);

		Assert.assertEquals("boolean error", deserializer.getBoolean(), false);
		Assert.assertEquals("byte error", deserializer.getByte(), 0);
		Assert.assertEquals("char error", deserializer.getChar(), '1');
		Assert.assertEquals("short error", deserializer.getShort(), 2);
		Assert.assertEquals("int error", deserializer.getInt(), 3);
		Assert.assertEquals("long error", deserializer.getLong(), 4);
		Assert.assertEquals("float error", deserializer.getFloat(), 5.5, 0.00001);
		Assert.assertEquals("double error", deserializer.getDouble(), 6.6, 0.00001);
		Assert.assertEquals("string error", deserializer.getString(), "a line of text with a semicolon(\\;)");
		Assert.assertEquals("string error", deserializer.getString(), "");
	}

	@Test
	public void testWrongValueInField() {
		final String string = "text;number";
		final CharBuffer buffer = CharBuffer.wrap(string.toCharArray());

		final TextValueDeserializer deserializer = TextValueDeserializer.create(buffer);

		Assert.assertEquals("string error", deserializer.getString(), "text");
		try {
			deserializer.getInt();
			Assert.fail("getInt should never return a value for non-number strings.");
		} catch (final NumberFormatException e) {
			Assert.assertTrue(true);
		}
	}

}
