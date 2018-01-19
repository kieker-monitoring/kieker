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

import java.nio.ByteBuffer;

/**
 * Abstract supertype for unwrappers working with character data.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public abstract class AbstractCharacterDataUnwrapper implements IRawDataUnwrapper {

	public AbstractCharacterDataUnwrapper() {
		// Do nothing
	}

	@Override
	public final boolean supportsBinaryData() {
		return false;
	}

	@Override
	public final boolean supportsCharacterData() {
		return true;
	}

	@Override
	public final ByteBuffer fetchBinaryData() {
		throw new UnsupportedOperationException("This unwrapper does not support binary data.");
	}

}
