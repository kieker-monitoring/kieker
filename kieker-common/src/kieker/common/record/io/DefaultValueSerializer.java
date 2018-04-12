/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.ByteBuffer;

import kieker.common.registry.writer.IWriterRegistry;

/**
 * This class is used to fulfill our deprecation policy. Due to a rename
 * this class is replaced by {@link BinaryValueSerializer}.
 *
 * @author Reiner Jung
 *
 * @since 1.13
 *
 * @deprecated 1.14 renamed to {@link BinaryValueSerializer}
 */
@Deprecated
public class DefaultValueSerializer extends BinaryValueSerializer { // NOCS (Default in type name: is already deprecated)

	protected DefaultValueSerializer(final ByteBuffer buffer, final IWriterRegistry<String> stringRegistry) {
		super(buffer, stringRegistry);
	}

}
