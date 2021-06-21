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

package kieker.common.util.dataformat;

import java.nio.ByteBuffer;

/**
 * Utility functions for variable-length encoding of integers.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public final class VariableLengthEncoding {

	private VariableLengthEncoding() {
		// Empty constructor
	}

	/**
	 * Encodes a given int value using a variable-length encoding. Each encoded byte starts
	 * with an indicator bit, followed by 7 data bits. The data bits are stored in lsb-first
	 * fashion to facilitate the encoding, which is more time-critical.
	 *
	 * @param value
	 *            The value to encode
	 * @param buffer
	 *            The buffer to write the data to
	 */
	public static void encodeInt(final int value, final ByteBuffer buffer) {
		int remainingData = value;

		while (true) {
			byte currentByte = (byte) (remainingData & 0x7F);
			remainingData = remainingData >>> 7;

			// Set the most significant bit to 1 if further bytes follow,
			// and to 0 otherwise
			if (remainingData != 0) {
				currentByte |= (byte) 0x80;
				buffer.put(currentByte);
			} else {
				buffer.put(currentByte);
				break;
			}
		}
	}

	/**
	 * Decodes a variable-length integer value stored at the current position
	 * in the byte buffer.
	 *
	 * @param buffer
	 *            The buffer to decode the data from
	 * @return The decoded integer value
	 */
	public static int decodeInt(final ByteBuffer buffer) {
		final int startPosition = buffer.position();
		int value = 0;
		int shiftAmount = 0;

		while (true) {
			final byte currentByte = buffer.get();
			// Non-terminal bytes have their msb set and are thus negative
			final boolean terminalByte = (currentByte >= 0);

			if (terminalByte) {
				value |= (currentByte << shiftAmount);
				break;
			} else {
				value |= ((currentByte & 0x7F) << shiftAmount);
				shiftAmount += 7;
			}
		}

		// Check whether the highest allowed shift amount was exceeded
		if (shiftAmount > 28) {
			throw new IllegalArgumentException("Unterminated variable-length int found at position " + startPosition);
		}

		return value;
	}
}
