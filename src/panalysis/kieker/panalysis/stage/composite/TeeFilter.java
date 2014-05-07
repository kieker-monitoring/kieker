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

package kieker.panalysis.stage.composite;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.framework.sequential.MethodCallPipe;
import kieker.panalysis.stage.Printer;
import kieker.panalysis.stage.basic.Distributor;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class TeeFilter<T> extends AbstractFilter<TeeFilter<T>> {

	private final Distributor<T> stage0;
	private final Printer<T> stage1;

	public TeeFilter(final IPipe<T> pipe) {
		this.stage0 = new Distributor<T>();
		this.stage1 = new Printer<T>();

		final IOutputPort<Distributor<T>, T> outputPort = this.stage0.getNewOutputPort();

		pipe.setSourcePort(outputPort);
		pipe.setTargetPort(this.stage0.OBJECT);

		final IPipe<T> internalPipeline = new MethodCallPipe<T>();
		internalPipeline.setSourcePort(outputPort);
		internalPipeline.setTargetPort(this.stage1.input);
	}

	@Override
	protected boolean execute(final Context<TeeFilter<T>> context) {
		return this.stage0.execute();
	}

}
