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

package kieker.panalysis.stage.basic.distributor;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param T
 *            the type of the input port and the output ports
 */
public class Distributor<T> extends AbstractFilter<Distributor<T>> {

	public final IInputPort<Distributor<T>, T> genericInputPort = this.createInputPort();

	private IDistributorStrategy<T> strategy = new RoundRobinStrategy<T>();

	public IDistributorStrategy<T> getStrategy() {
		return this.strategy;
	}

	public void setStrategy(final IDistributorStrategy<T> strategy) {
		this.strategy = strategy;
	}

	@Override
	protected boolean execute(final Context<Distributor<T>> context) {
		final T object = context.tryTake(this.genericInputPort);
		if (object == null) {
			return false;
		}

		return this.strategy.distribute(context, this.getOutputPorts(), object);
	}

	public IOutputPort<Distributor<T>, T> getNewOutputPort() {
		final IOutputPort<Distributor<T>, T> newOutputPort = this.createOutputPort();
		return newOutputPort;
	}
}
