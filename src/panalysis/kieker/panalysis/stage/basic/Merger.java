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

package kieker.panalysis.stage.basic;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * 
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param <T>
 *            the type of the input ports and the output port
 */
public class Merger<T> extends AbstractFilter<Merger<T>> {

	public final IOutputPort<Merger<T>, T> OBJECT = this.createOutputPort();

	private int index = 0;

	// TODO add parameter: MergeStrategy
	/**
	 * @since 1.10
	 */
	public Merger() {}

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<Merger<T>> context) {
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
	@SuppressWarnings("unchecked")
	private IInputPort<Merger<T>, T> getInputPort(final int portIndex) {
		final IInputPort<Merger<T>, ?> port = this.getInputPorts().get(portIndex);
		return (IInputPort<Merger<T>, T>) port;
	}

	/**
	 * @since 1.10
	 * @return
	 */
	public IInputPort<Merger<T>, T> getNewInputPort() {
		final IInputPort<Merger<T>, T> newInputPort = this.createInputPort();
		return newInputPort;
	}
}
