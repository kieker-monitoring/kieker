/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Reiner Jung
 *
 * @since 1.10
 */
public abstract class AbstractGeneratedKiekerTest extends AbstractKiekerTest {

	protected static final List<Integer> INT_VALUES = Collections.unmodifiableList(Arrays.asList(
			Integer.MIN_VALUE,
			0,
			Integer.MAX_VALUE));

	protected static final List<Short> SHORT_VALUES = Collections.unmodifiableList(Arrays.asList( // NOPMD NOCS (CS complains about white space)
			Short.MIN_VALUE, // NOPMD (pmd does not like short, however, we have to test it)
			(short) 0, // NOPMD
			Short.MAX_VALUE // NOPMD
	));

	protected static final List<Long> LONG_VALUES = Collections.unmodifiableList(Arrays.asList(
			Long.MIN_VALUE,
			0L,
			Long.MAX_VALUE));

	protected static final List<Byte> BYTE_VALUES = Collections.unmodifiableList(Arrays.asList(
			Byte.MIN_VALUE,
			(byte) 0,
			Byte.MAX_VALUE));

	protected static final List<Character> CHARACTER_VALUES = Collections.unmodifiableList(Arrays.asList(
			Character.MIN_VALUE,
			Character.MAX_VALUE));

	protected static final List<Boolean> BOOLEAN_VALUES = Collections.unmodifiableList(Arrays.asList(false, true));

	protected static final List<Float> FLOAT_VALUES = Collections.unmodifiableList(Arrays.asList(
			Float.MIN_VALUE, (float) Math.pow(1.0f, Float.MIN_EXPONENT), Float.MIN_NORMAL,
			0.0f, Float.MAX_VALUE, (float) Math.pow(1.0f, Float.MAX_EXPONENT),
			Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));

	protected static final List<Double> DOUBLE_VALUES = Collections.unmodifiableList(Arrays.asList(
			Double.MIN_VALUE, Math.pow(1.0f, Double.MIN_EXPONENT), Double.MIN_NORMAL,
			0.0, Double.MAX_VALUE, Math.pow(1.0, Double.MAX_EXPONENT),
			Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));

	protected static final List<String> STRING_VALUES = Collections.unmodifiableList(Arrays.asList(
			null,
			"",
			"abcdefghijklmnopqrstuvwxyz0123456789/.()$_ !%& *+-=#~"));

	protected static final int ARRAY_LENGTH = 8;

	/**
	 * Empty Default constructor.
	 */
	public AbstractGeneratedKiekerTest() {
		// Emtpy constructor.
	}

}
