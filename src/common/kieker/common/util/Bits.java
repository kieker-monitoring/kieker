/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.util;

// FIXME: check license!
/**
 * Mostly copied from JDK.
 * 
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class Bits {

	public static final void putLong(final byte[] b, final int off, final long val) {
		b[off + 7] = (byte) (val);
		b[off + 6] = (byte) (val >>> 8);
		b[off + 5] = (byte) (val >>> 16);
		b[off + 4] = (byte) (val >>> 24);
		b[off + 3] = (byte) (val >>> 32);
		b[off + 2] = (byte) (val >>> 40);
		b[off + 1] = (byte) (val >>> 48);
		b[off] = (byte) (val >>> 56);
	}

	public static final void putInt(final byte[] b, final int off, final int val) {
		b[off + 3] = (byte) (val);
		b[off + 2] = (byte) (val >>> 8);
		b[off + 1] = (byte) (val >>> 16);
		b[off] = (byte) (val >>> 24);
	}

	public static final void putChar(final byte[] b, final int off, final char val) {
		b[off + 1] = (byte) (val);
		b[off] = (byte) (val >>> 8);
	}

	public static final void putShort(final byte[] b, final int off, final short val) {
		b[off + 1] = (byte) (val);
		b[off] = (byte) (val >>> 8);
	}

	public static final void putByte(final byte[] b, final int off, final byte val) {
		b[off] = val;
	}

	public static final void putBoolean(final byte[] b, final int off, final boolean val) {
		b[off] = (byte) (val ? 1 : 0);
	}

	public static final void putFloat(final byte[] b, final int off, final float val) {
		Bits.putInt(b, off, Float.floatToIntBits(val));
	}

	public static final void putDouble(final byte[] b, final int off, final double val) {
		Bits.putLong(b, off, Double.doubleToLongBits(val));
	}

	public static final void putString(final byte[] b, final int off, final String val) {
		Bits.putLong(b, off, 0L); // FIXME
	}

	public static final long getLong(final byte[] b, final int off) {
		return ((b[off + 7] & 0xFFL)) + ((b[off + 6] & 0xFFL) << 8)
				+ ((b[off + 5] & 0xFFL) << 16) + ((b[off + 4] & 0xFFL) << 24)
				+ ((b[off + 3] & 0xFFL) << 32) + ((b[off + 2] & 0xFFL) << 40)
				+ ((b[off + 1] & 0xFFL) << 48) + (((long) b[off]) << 56);
	}

	public static final int getInt(final byte[] b, final int off) {
		return ((b[off + 3] & 0xFF)) + ((b[off + 2] & 0xFF) << 8)
				+ ((b[off + 1] & 0xFF) << 16) + ((b[off]) << 24);
	}

	public static final char getChar(final byte[] b, final int off) {
		return (char) ((b[off + 1] & 0xFF) + (b[off] << 8));
	}

	public static final short getShort(final byte[] b, final int off) {
		return (short) ((b[off + 1] & 0xFF) + (b[off] << 8));
	}

	public static final byte getByte(final byte[] b, final int off) {
		return b[off];
	}

	public static final boolean getBoolean(final byte[] b, final int off) {
		return b[off] != 0;
	}

	public static final float getFloat(final byte[] b, final int off) {
		return Float.intBitsToFloat(Bits.getInt(b, off));
	}

	public static final double getDouble(final byte[] b, final int off) {
		return Double.longBitsToDouble(Bits.getLong(b, off));
	}
}
