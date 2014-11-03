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

package kieker.common.record.factory;

import java.nio.ByteBuffer;

import kieker.common.util.registry.IRegistry;

/**
 * @param <T>
 *            the record type to used for creating new record instances
 *
 * @author Christian Wulf
 * @since 1.11
 */
public interface IRecordFactory<T> {

	/**
	 *
	 * @param buffer
	 *            the buffer to read from
	 * @param stringRegistry
	 *            the registry to read strings from
	 *
	 * @return a new instance of the declared record type
	 * @since 1.11
	 */
	T create(ByteBuffer buffer, IRegistry<String> stringRegistry);

	/**
	 *
	 * @param values
	 *            the values used to fill the new instance
	 *
	 * @return a new instance of the declared record type
	 * @since 1.11
	 */
	T create(Object[] values);
}
