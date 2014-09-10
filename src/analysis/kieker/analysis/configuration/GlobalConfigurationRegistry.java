/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.configuration;


/**
 * 
 * @author Markus Fischer
 * 
 *         Singleton to provide a global registry for all updateable Filters.<br>
 *         All updateable Filters have to be stored to the Registry with a unique ID.
 * @since 1.10
 */
public final class GlobalConfigurationRegistry extends AbstractConfigurationRegistry {

	private static IConfigurationRegistry instance = new GlobalConfigurationRegistry();

	private GlobalConfigurationRegistry() {}

	public static IConfigurationRegistry getInstance() {
		return instance;
	}
}
