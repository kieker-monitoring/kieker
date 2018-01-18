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
package kieker.monitoring.writer.serializer;

import java.nio.CharBuffer;

/**
 * No-op implementation of a character wrapper.
 * 
 * @author Holger Knoche
 * @since 2.0
 */
public class NopCharacterWrapper extends AbstractCharacterDataWrapper {

	public NopCharacterWrapper(final int bufferSize) {
		// Do nothing
	}
	
	@Override
	public CharBuffer wrap(final CharBuffer data) {
		return data;
	}
	
}
