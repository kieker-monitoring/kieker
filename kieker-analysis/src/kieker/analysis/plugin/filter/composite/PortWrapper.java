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

package kieker.analysis.plugin.filter.composite;

/**
 * 
 * @author Markus Fischer
 * @since 1.10
 * 
 *        Simple class to wrap up a portName and a corresponding eventtype
 */
public class PortWrapper {
	private final String outputPortName;
	private final Class<?> eventClass;

	/**
	 * Constructor.
	 * 
	 * @param outputPortName
	 *            OutputPortame
	 * @param eventClass
	 *            eventClass
	 */
	public PortWrapper(final String outputPortName, final Class<?> eventClass) {
		this.eventClass = eventClass;
		this.outputPortName = outputPortName;
	}

	public String getOutputPortName() {
		return this.outputPortName;

	}

	public Class<?> getEventClass() {
		return this.eventClass;

	}
}
