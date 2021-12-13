/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class ObjectIdentifierRegistry {

	private final Map<Object, Integer> identifiers = new HashMap<>(); // NOPMD (class not designed for concurrent access)
	private int counter; // is 0 per default

	public ObjectIdentifierRegistry() {
		// Create an empty registry
	}

	public int getIdentifier(final Object object) {
		final Integer identifier = this.identifiers.putIfAbsent(object, this.counter);
		if (identifier == null) {
			// Value was added
			final int id = this.counter;
			this.counter++;
			return id;
		} else {
			// Value was already present
			return identifier;
		}
	}

}
