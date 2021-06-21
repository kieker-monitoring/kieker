/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.stage.select;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This filter has exactly one input port and one output port.
 * Please note that the @{link teetime.stage.InstanceOfFilter} provides similar functionality.
 *
 * Only the specified objects are forwarded to the output port.
 * All other objects are forwarded to the output-not port.
 *
 * @author Jan Waller, Lars Bluemke
 *
 * @since 1.5
 */
public class TypeFilter extends AbstractConsumerStage<Object> {

	private final Class<?>[] acceptedClasses;

	private final OutputPort<Object> matchingTypeOutputPort = this.createOutputPort();
	private final OutputPort<Object> mismatchingTypeOutputPort = this.createOutputPort();

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param acceptedClasses
	 *            The types which will be accepted by the filter.
	 */
	public TypeFilter(final Class<?>[] acceptedClasses) {
		this.acceptedClasses = acceptedClasses.clone();
	}

	/**
	 * This method represents the input port for the incoming objects.
	 *
	 * @param event
	 *            The new incoming object.
	 */
	@Override
	protected void execute(final Object event) {
		final Class<?> eventClass = event.getClass();
		for (final Class<?> clazz : this.acceptedClasses) {
			if (clazz.isAssignableFrom(eventClass)) {
				this.matchingTypeOutputPort.send(event);
				return; // only deliver once!
			}
		}
		this.mismatchingTypeOutputPort.send(event);
	}

	/**
	 * Returns the output port where the incoming matching objects will be sent to.
	 *
	 * @return matching type port
	 */
	public OutputPort<Object> getMatchingTypeOutputPort() {
		return this.matchingTypeOutputPort;
	}

	/**
	 * Returns the output port where the incoming objects will be sent to, which do not match the configured types.
	 *
	 * @return mismatching type port
	 */
	public OutputPort<Object> getMismatchingTypeOutputPort() {
		return this.mismatchingTypeOutputPort;
	}
}
