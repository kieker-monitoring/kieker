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

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class Clock extends AbstractFilter<Clock> {

	public final IOutputPort<Clock, Long> timestampOutputPort = this.createOutputPort();

	@Override
	protected boolean execute(final Context<Clock> context) {
		final long timestamp = this.getTimestamp();
		context.put(this.timestampOutputPort, timestamp);
		return true;
	}

	/**
	 * @since 1.10
	 */
	private long getTimestamp() {
		return System.nanoTime();
	}

}
