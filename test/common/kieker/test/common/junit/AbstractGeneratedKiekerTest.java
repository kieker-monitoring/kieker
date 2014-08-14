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
public abstract class AbstractGeneratedKiekerTest extends AbstractKiekerTest {
	protected static final int[] INT_VALUES = {
		Integer.MIN_VALUE,
		0,
		Integer.MAX_VALUE,
	};
	protected static final short[] SHORT_VALUES = { // NOPMD (ignore short type warning)
	Short.MIN_VALUE,
		0,
		Short.MAX_VALUE,
	};
	protected static final long[] LONG_VALUES = {
		Long.MIN_VALUE,
		0,
		Long.MAX_VALUE,
	};
	protected static final byte[] BYTE_VALUES = {
		Byte.MIN_VALUE,
		0,
		Byte.MAX_VALUE,
	};

	protected static final char[] CHARACTER_VALUES = {
		Character.MIN_VALUE,
		Character.MAX_VALUE,
	};
	protected static final boolean[] BOOLEAN_VALUES = { false, true, };

	protected static final float[] FLOAT_VALUES = {
		Float.MIN_VALUE, Float.MIN_EXPONENT, Float.MIN_NORMAL,
		0, Float.MAX_VALUE, Float.MAX_EXPONENT,
		Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY,
	};
	protected static final double[] DOUBLE_VALUES = {
		Double.MIN_VALUE, Double.MIN_EXPONENT, Double.MIN_NORMAL,
		0, Double.MAX_VALUE, Double.MAX_EXPONENT,
		Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
	};

	protected static final String[] STRING_VALUES = {
		null,
		"",
		"abcdefghijklmnopqrstuvwxyz0123456789/.()$_ !%& *+-=#~",
	};

	protected static final int ARRAY_LENGTH = 8;

}
