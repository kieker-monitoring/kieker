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
package kieker.analysis.plugin.reader.newio.deserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import kieker.analysis.plugin.reader.newio.AbstractCharacterDataUnwrapper;

/**
 * Character-based unwrapper which separates blocks at newlines.
 * @author Holger Knoche
 * @since 2.0
 */
public class NewlineUnwrapper extends AbstractCharacterDataUnwrapper {

	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private final BufferedReader reader;

	public NewlineUnwrapper(final InputStream inputStream) {
		this.reader = new BufferedReader(new InputStreamReader(inputStream, DEFAULT_CHARSET));
	}

	@Override
	public CharBuffer fetchCharacterData() throws IOException {
		String nextLine = this.reader.readLine();

		if (nextLine != null) {
			return CharBuffer.wrap(nextLine);
		} else {
			return null;
		}
	}

}
