/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.common;

/**
 * Common configuration keys used by multiple tools.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public final class CommonConfigurationKeys {
	public static final String PREFIX = "kieker.tools.";

	public static final String SOURCE_STAGE = CommonConfigurationKeys.PREFIX + "source";

	/** do not instantiate factory. */
	private CommonConfigurationKeys() {
		// factory class constructor
	}

}
