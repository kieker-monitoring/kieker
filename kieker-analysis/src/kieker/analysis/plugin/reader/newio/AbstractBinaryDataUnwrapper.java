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

import java.nio.CharBuffer;

/**
 * Abstract supertype for unwrappers working with binary data.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public abstract class AbstractBinaryDataUnwrapper implements IRawDataUnwrapper {

	@Override
	public final boolean supportsBinaryData() {
		return true;
	}

	@Override
	public final boolean supportsCharacterData() {
		return false;
	}

	@Override
	public final CharBuffer fetchCharacterData() {
		throw new UnsupportedOperationException("This unwrapper does not support character data.");
	}

}
