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

package kieker.analysis.plugin;

import java.lang.reflect.Method;

/**
 * 
 * @author Nils Christian Ehmke
 */
public class PluginInputPortReference {
	private final AbstractPlugin plugin;
	private final Method inputPortMethod;
	private final Class<?>[] eventTypes;

	public PluginInputPortReference(final AbstractPlugin plugin, final Method inputPortMethod, final Class<?>[] eventTypes) {
		this.plugin = plugin;
		this.inputPortMethod = inputPortMethod;
		this.eventTypes = eventTypes;
	}

	public AbstractPlugin getPlugin() {
		return this.plugin;
	}

	public Method getInputPortMethod() {
		return this.inputPortMethod;
	}

	public Class<?>[] getEventTypes() {
		return this.eventTypes;
	}
}
