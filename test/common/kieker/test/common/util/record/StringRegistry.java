/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.util.record;

import kieker.common.util.IString4UniqueId;
import kieker.common.util.IUniqueId4String;
import kieker.monitoring.core.registry.IRegistry;
import kieker.monitoring.core.registry.Registry;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class StringRegistry implements IString4UniqueId, IUniqueId4String {

	private final IRegistry<String> registry;

	public StringRegistry() {
		this.registry = new Registry<String>();
	}

	public int getIdForString(final String string) {
		return this.registry.get(string);
	}

	public String getStringForId(final int id) {
		return this.registry.get(id);
	}
}
