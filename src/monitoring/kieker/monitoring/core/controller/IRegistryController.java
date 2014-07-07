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

package kieker.monitoring.core.controller;

import kieker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public interface IRegistryController {

	/**
	 * Gets a unique id for a string.
	 * 
	 * @param string
	 *            the string
	 * @return
	 *         the unique id
	 * 
	 * @since 1.5
	 */
	public abstract int getUniqueIdForString(final String string);

	/**
	 * Gets a string for a unique id.
	 * 
	 * @param id
	 *            the unique id
	 * @return
	 *         the string
	 * 
	 * @since 1.8
	 */
	public abstract String getStringForUniqueId(final int id);

	/**
	 * Gets the used IRegistry<String>.
	 * 
	 * @return
	 *         the registry
	 * 
	 * @since 1.8
	 */
	public abstract IRegistry<String> getStringRegistry();

}
