/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.monitoring.core.registry;

/**
 * 
 * @author Jan Waller
 */
public final class StringRegistry extends Registry<String> {

	/**
	 * private constructor
	 */
	private StringRegistry() {
		super(true); // send records on new entries
	}

	/**
	 * Get the singleton instance of the StringRegistry.
	 * 
	 * @return
	 *         the singleton StringRegistry
	 */
	public static final StringRegistry getRegistry() {
		return LazyStringHolder.INSTANCE;
	}

	/**
	 * SINGLETON StringRegistry
	 */
	private static final class LazyStringHolder { // NOCS
		static {
			INSTANCE = new StringRegistry();
		}
		private static final StringRegistry INSTANCE;
	}
}
