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
package kieker.analysis.plugin.reader.newio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Raw data unwrappers unwrap chunks of raw data that are sent over a low-level
 * medium that does not provide it own means to reconstruct the original data
 * chunk in its entirety. For instance, a chunk sent to a (named) pipe may be
 * sliced into smaller chunks by the operating system. For such media,
 * additional wrapping is required to recover the original chunk. Higher-level
 * media like TCP connections or messaging provide the necessary facilities and
 * do not require wrapping.
 *
 * @author Holger Knoche
 * @since 2.0
 *
 */
public interface IRawDataUnwrapper {

	/**
	 * Denotes whether this unwrapper supports binary data.
	 * @return see above
	 * @since 2.0
	 */
	public boolean supportsBinaryData();

	/**
	 * Denotes whether this unwrapper supports character data.
	 * @return see above
	 * @since 2.0
	 */
	public boolean supportsCharacterData();

	/**
	 * Fetches the next chunk of binary data, if supported (see {@link #supportsBinaryData()}).
	 * @return The next chunk of binary data from the input medium
	 * @throws IOException If an I/O error occurs
	 * @since 2.0
	 */
	public ByteBuffer fetchBinaryData() throws IOException;

	/**
	 * Fetches the next chunk of character data, if supported (see {@link #supportsCharacterData()}).
	 * @return The next chunk of character data from the input medium
	 * @throws IOException If an I/O error occurs
	 * @since 2.0
	 */
	public CharBuffer fetchCharacterData() throws IOException;

}
