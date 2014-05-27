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
package kieker.panalysis.stage;

import kieker.panalysis.examples.throughput.TimestampObject;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class StartTimestampFilter extends AbstractFilter<StartTimestampFilter> {

	public final IInputPort<StartTimestampFilter, TimestampObject> inputPort = this.createInputPort();
	public final IOutputPort<StartTimestampFilter, TimestampObject> outputPort = this.createOutputPort();

	@Override
	protected boolean execute(final Context<StartTimestampFilter> context) {
		final TimestampObject inputObject = context.tryTake(this.inputPort);
		if (inputObject == null) {
			return false;
		}

		inputObject.setStartTimestamp(System.nanoTime());

		context.put(this.outputPort, inputObject);

		return true;
	}

}
