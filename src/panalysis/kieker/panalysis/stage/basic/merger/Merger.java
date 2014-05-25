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

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.Description;
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
@Description("This stage merges data from the input ports, by taking elements according to the chosen merge strategy and by putting them to the output port.")
public class Merger<T> extends AbstractFilter<Merger<T>> {

	public final IOutputPort<Merger<T>, T> outputPort = this.createOutputPort();

	private IMergerStrategy<T> strategy = new RoundRobinStrategy<T>();

	public IMergerStrategy<T> getStrategy() {
		return this.strategy;
	}

	public void setStrategy(final IMergerStrategy<T> strategy) {
		this.strategy = strategy;
	}

	@Override
	protected boolean execute(final Context<Merger<T>> context) {
		final T token = this.strategy.getNextInput(context, this.getInputPorts());
		if (token == null) {
			return false;
		}

		context.put(this.outputPort, token);
		return true;
	}

	public IInputPort<Merger<T>, T> getNewInputPort() {
		final IInputPort<Merger<T>, T> newInputPort = this.createInputPort();
		return newInputPort;
	}

}
