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
package kieker.panalysis.stage.basic.merger;

import java.util.List;

import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public final class RoundRobinStrategy<T> implements IMergerStrategy<T> {

	private int index = 0;

	public <S extends Merger<T>> T getNextInput(final Context<S> context, final List<IInputPort<S, ?>> inputPorts) {
		int size = inputPorts.size();
		// check each port at most once to avoid a potentially infinite loop
		while (size-- > 0) {
			final IInputPort<S, T> inputPort = this.getNextPortInRoundRobinOrder(inputPorts);
			final T token = context.tryTake(inputPort);
			if (token != null) {
				return token;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <S extends Merger<T>> IInputPort<S, T> getNextPortInRoundRobinOrder(final List<IInputPort<S, ?>> inputPorts) {
		final IInputPort<S, T> port = (IInputPort<S, T>) inputPorts.get(this.index);

		this.index = (this.index + 1) % inputPorts.size();

		return port;
	}

}
