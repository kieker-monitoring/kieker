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

package kieker.test.common.util.dataformat;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.util.dataformat.VariableLengthEncoding;

/**
 * Tests for the variable-length encoding of integers.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class VariableLengthEncodingTest {

	/**
	 * create test.
	 */
	public VariableLengthEncodingTest() {
		// Default Constructor
	}

	/**
	 * Tests a value that is encoded into a single byte.
	 */
	@Test
	public void testEncodeOneByteInteger() {
		final int value = 17;
		final byte[] expected = VariableLengthEncodingTest.asByteArray(0x11, 0x00, 0x00, 0x00, 0x00); // NOPMD plural not relevant

		final byte[] actual = this.encodeInt(value); // NOPMD plural not relevant

		Assert.assertArrayEquals(expected, actual);
	}

	/**
	 * Tests a value that is decoded from a single byte.
	 */
	@Test
	public void testDecodeOneByteInteger() {
		final byte[] data = VariableLengthEncodingTest.asByteArray(0x11); // NOPMD plural not relevant
		final int expected = 17;

		final int actual = this.decodeInt(data);

		Assert.assertEquals(expected, actual);
	}

	/**
	 * Tests a value that is encoded into multiple bytes.
	 */
	@Test
	public void testEncodeMultiByteInteger() {
		final int value = 85302;
		final byte[] expected = VariableLengthEncodingTest.asByteArray(0xB6, 0x9A, 0x05, 0x00, 0x00); // NOPMD plural not relevant

		final byte[] actual = this.encodeInt(value); // NOPMD plural not relevant

		Assert.assertArrayEquals(expected, actual);
	}

	/**
	 * Tests a value that is decoded from a multiple bytes.
	 */
	@Test
	public void testDecodeMultiByteInteger() {
		final byte[] data = VariableLengthEncodingTest.asByteArray(0xB6, 0x9A, 0x05); // NOPMD plural not relevant
		final int expected = 85302;

		final int actual = this.decodeInt(data);

		Assert.assertEquals(expected, actual);
	}

	/**
	 * Tests encoding MAX_INTEGER.
	 */
	@Test
	public void testEncodeMaxInteger() {
		final int value = Integer.MAX_VALUE;
		final byte[] expected = VariableLengthEncodingTest.asByteArray(0xFF, 0xFF, 0xFF, 0xFF, 0x07); // NOPMD plural not relevant

		final byte[] actual = this.encodeInt(value); // NOPMD plural not relevant

		Assert.assertArrayEquals(expected, actual);
	}

	/**
	 * Tests decoding MAX_INTEGER.
	 */
	@Test
	public void testDecodeMaxInteger() {
		final byte[] data = VariableLengthEncodingTest.asByteArray(0xFF, 0xFF, 0xFF, 0xFF, 0x07); // NOPMD plural not relevant
		final int expected = Integer.MAX_VALUE;

		final int actual = this.decodeInt(data);

		Assert.assertEquals(expected, actual);
	}

	/**
	 * Tests encoding a negative integer.
	 */
	@Test
	public void testEncodeNegativeInteger() {
		final int value = -13570409;
		final byte[] expected = VariableLengthEncodingTest.asByteArray(0x97, 0xDD, 0xC3, 0xF9, 0x0F); // NOPMD plural not relevant

		final byte[] actual = this.encodeInt(value); // NOPMD plural not relevant

		Assert.assertArrayEquals(expected, actual);
	}

	/**
	 * Tests decoding a negative integer.
	 */
	@Test
	public void testDecodeNegativeInteger() {
		final byte[] data = VariableLengthEncodingTest.asByteArray(0x97, 0xDD, 0xC3, 0xF9, 0x0F); // NOPMD plural not relevant
		final int expected = -13570409;

		final int actual = this.decodeInt(data); // NOPMD plural not relevant

		Assert.assertEquals(expected, actual);
	}

	private static byte[] asByteArray(final int... values) {
		final byte[] data = new byte[values.length]; // NOPMD plural not relevant

		for (int index = 0; index < values.length; index++) {
			data[index] = (byte) values[index];
		}

		return data;
	}

	private byte[] encodeInt(final int value) {
		final int bufferSize = 5;

		final byte[] data = new byte[bufferSize]; // NOPMD plural not relevant
		final ByteBuffer buffer = ByteBuffer.wrap(data);
		VariableLengthEncoding.encodeInt(value, buffer);

		return data;
	}

	private int decodeInt(final byte[] data) {
		final ByteBuffer buffer = ByteBuffer.wrap(data);
		return VariableLengthEncoding.decodeInt(buffer);
	}

}
