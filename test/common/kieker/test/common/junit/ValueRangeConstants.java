/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.junit;

/**
 * @author Reiner Jung
 * 
 * @since 1.10
 */
public class ValueRangeConstants {
	public final static int[] INT_VALUES = new int[] {
		Integer.MIN_VALUE,
		0,
		Integer.MAX_VALUE,
	};
	public final static short[] SHORT_VALUES = new short[] {
		Short.MIN_VALUE,
		0,
		Short.MAX_VALUE,
	};
	public final static long[] LONG_VALUES = new long[] {
		Long.MIN_VALUE,
		0,
		Long.MAX_VALUE,
	};
	public final static byte[] BYTE_VALUES = new byte[] {
		Byte.MIN_VALUE,
		0,
		Byte.MAX_VALUE,
	};

	public final static char[] CHARACTER_VALUES = new char[] { Character.MIN_VALUE, Character.MAX_VALUE, };
	public final static boolean[] BOOLEAN_VALUES = new boolean[] { false, true, };

	public final static float[] FLOAT_VALUES = new float[] { Float.MIN_VALUE, Float.MIN_EXPONENT, Float.MIN_NORMAL,
		0, Float.MAX_VALUE, Float.MAX_EXPONENT,
		Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, };
	public final static double[] DOUBLE_VALUES = new double[] { Double.MIN_VALUE, Double.MIN_EXPONENT, Double.MIN_NORMAL,
		0, Double.MAX_VALUE, Double.MAX_EXPONENT,
		Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, };

	public final static String[] STRING_VALUES = new String[] { null, "", "abcdefghijklmnopqrstuvwxyz0123456789/.()$_ !%& *+-=#~", };

	public static int ARRAY_LENGTH = 8;

	/**
	 * Utility class.
	 */
	private ValueRangeConstants() {

	}
}
