/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.raw;

import java.nio.Buffer;

/**
 * Raw data wrappers wrap chunks of raw data that are sent over a low-level
 * medium that does not provide it own means to reconstruct the original data
 * chunk in its entirety. For instance, a chunk sent to a (named) pipe may be
 * sliced into smaller chunks by the operating system. For such media,
 * additional wrapping is required to recover the original chunk. Higher-level
 * media like TCP connections or messaging provide the necessary facilities and
 * do not require wrapping.
 * 
 * @param <T> The actual buffer type for this wrapper
 * 
 * @author Holger Knoche
 * @since 2.0
 *
 */
public interface IRawDataWrapper<T extends Buffer> {

	/**
	 * Denotes whether this wrapper supports binary data.
	 * @return see above
	 * @since 2.0
	 */
	public boolean supportsBinaryData();

	/**
	 * Denotes whether this wrapper supports character data.
	 * @return see above
	 * @since 2.0
	 */
	public boolean supportsCharacterData();
	
	/**
	 * Wraps the given chunk of data.
	 * @param data A buffer containing the data to wrap. The buffer is expected to be "flipped".
	 * @return The wrapped data chunk
	 * @since 2.0
	 */
	public T wrap(T data);
			
}
