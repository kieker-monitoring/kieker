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

import kieker.panalysis.base.AbstractFilter;
import kieker.panalysis.base.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class RepeaterSource<T> extends AbstractFilter<RepeaterSource<T>> {

	public final IOutputPort<RepeaterSource<T>, T> OUTPUT = this.createOutputPort();

	private final T outputRecord;
	private final int num;
	private long overallDuration;

	public RepeaterSource(final T outputRecord, final int num) {
		this.outputRecord = outputRecord;
		this.num = num;
	}

	public boolean execute() {
		final long start = System.currentTimeMillis();

		int counter = this.num;
		while (counter-- > 0) {
			this.put(this.OUTPUT, this.outputRecord);
		}

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;

		return true;
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

}
