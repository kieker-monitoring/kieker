/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.generic.graph;

import java.util.Set;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public interface IElement {

	String getId();

	/**
	 * Returns the property of an element identified by a key.
	 *
	 * @param key
	 *            property name
	 * @param <T>
	 *            type of the property value
	 *
	 * @return a property value
	 * @since 1.14
	 */
	<T> T getProperty(String key);

	/**
	 * Return all property keys of an element.
	 *
	 * @return list of keys
	 *
	 * @since 1.14
	 */
	Set<String> getPropertyKeys();

	/**
	 * Set a property of an element. Overwrite the value in case the property already exists.
	 *
	 * @param key
	 *            key name
	 * @param value
	 *            value object
	 *
	 * @since 1.14
	 */
	void setProperty(String key, Object value);

	/**
	 * Set a property of an element only if no property with the given key already exists.
	 *
	 * @param key
	 *            key name
	 * @param value
	 *            value object
	 *
	 * @since 1.14
	 */
	void setPropertyIfAbsent(String key, Object value);

	/**
	 * Remove a property from the element.
	 *
	 * @param key
	 *            key identifying the property
	 * @param <T>
	 *            type of the property value
	 * @return the removed property value
	 * @since 1.14
	 */
	<T> T removeProperty(String key);
}
