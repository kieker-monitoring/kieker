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

package kieker.common.util.registry;

import java.util.UUID;

/**
 * Common utility functions for both lookups and registries.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
final class RegistryUtil {

	private RegistryUtil() {}

	/**
	 * Generates a random ID to use for a registry.
	 *
	 * @return see above
	 */
	public static long generateId() {
		return UUID.randomUUID().getLeastSignificantBits();
	}

}
