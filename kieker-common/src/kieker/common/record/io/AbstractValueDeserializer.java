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

package kieker.common.record.io;

import kieker.common.exception.RecordInstantiationException;

/**
 * The AbstractValueDeserializer provides common functionality for {@link IValueDeserializer}.
 * Presently, this is limited to supportive routines to resolve enumeration values.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public abstract class AbstractValueDeserializer { // NOPMD no abstract methods, as they are defined by the corresponding IValueDeserializer interface

	/**
	 * Create an abstract value deserializer.
	 */
	public AbstractValueDeserializer() {
		// empty constructor
	}

	/**
	 * Compute the correct enumeration value for the given {@code enumType} and {@code ordinal} value.
	 *
	 * @param <T>
	 *            enumeration type
	 *
	 * @param enumType
	 *            class type referring to an enumeration type
	 * @param ordinal
	 *            the ordinal number of an element of the enumeration type
	 * @return returns an enumeration value
	 * @throws RecordInstantiationException
	 *             in case the ordinal value does not specify an element of the specified enumeration
	 */
	protected <T extends Enum<T>> T enumerationValueOf(final Class<T> enumType,
			final int ordinal) throws RecordInstantiationException {
		final T[] results = enumType.getEnumConstants();
		for (final T value : results) {
			if (value.ordinal() == ordinal) {
				return value;
			}
		}
		throw new RecordInstantiationException("The given ordinal %d does not refer to a valid enumeration element in %s.", ordinal, enumType.getCanonicalName());
	}
}
