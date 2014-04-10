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

package kieker.panalysis;

import kieker.panalysis.base.AbstractFilter;
import kieker.panalysis.base.IInputPort;
import kieker.panalysis.base.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class Merger<T> extends AbstractFilter<Merger<T>> {

	public final IOutputPort<Merger<T>, T> OBJECT = this.createOutputPort();

	private int index = 0;

	// TODO add parameter: MergeStrategy
	/**
	 * @since 1.10
	 * @param numInputPorts
	 */
	public Merger(int numInputPorts) {
		while (numInputPorts-- > 0) {
			this.createInputPort();
		}
	}

	/**
	 * @since 1.10
	 */
	public boolean execute() {
		int size = this.getInputPorts().size();
		// check each port at most once to avoid a potentially infinite loop
		while (size-- > 0) {
			final IInputPort<Merger<T>, T> inputPort = this.getNextPortInRoundRobinOrder();
			final T token = this.tryTake(inputPort);
			if (token != null) {
				this.put(this.OBJECT, token);
				return true;
			}
		}
		return false;
	}

	/**
	 * @since 1.10
	 * @return
	 */
	private IInputPort<Merger<T>, T> getNextPortInRoundRobinOrder() {
		final IInputPort<Merger<T>, T> port = this.getInputPort(this.index);
		this.index = (this.index + 1) % this.getInputPorts().size();
		return port;
	}

	/**
	 * @since 1.10
	 * @param portIndex
	 * @return
	 */
	public IInputPort<Merger<T>, T> getInputPort(final int portIndex) {
		@SuppressWarnings("unchecked")
		final IInputPort<Merger<T>, T> port = (IInputPort<Merger<T>, T>) this.getInputPorts().get(portIndex);
		return port;
	}

}
