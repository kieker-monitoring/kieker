/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.generic;

import teetime.stage.basic.AbstractFilter;

/**
 * Counts all events and hands them to the next filter unchanged. The stage outputs the number of
 * events for every <code>numberOfOccurance</code> event occurrence and on termination. Output is
 * written to info log channel.
 *
 * @param <T>
 *            event type
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class CountEventsStage<T> extends AbstractFilter<T> {

	private long counter;
	private final long numberOfOccurance;

	public CountEventsStage(final long numberOfOccurance) {
		this.numberOfOccurance = numberOfOccurance;
	}

	@Override
	protected void execute(final T element) throws Exception {
		this.counter++;
		if ((this.counter % this.numberOfOccurance) == 0) {
			this.logger.info("Received {} events.", this.counter);
		}
		this.outputPort.send(element);
	}

	@Override
	protected void onTerminating() {
		this.logger.info("Received {} events.", this.counter);
		super.onTerminating();
	}
}
