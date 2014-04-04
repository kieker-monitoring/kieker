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

import kieker.panalysis.base.AbstractSource;
import kieker.panalysis.base.TaskBundle;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class RepeaterSource extends AbstractSource<RepeaterSource.OUTPUT_PORT> {

	private final Object outputRecord;
	private final int num;
	private long overallDuration;

	public static enum OUTPUT_PORT { // NOCS
		OUTPUT
	}

	public RepeaterSource(final Object outputRecord, final int num) {
		super(OUTPUT_PORT.class);
		this.outputRecord = outputRecord;
		this.num = num;
	}

	public void execute() {
		final long start = System.currentTimeMillis();

		int counter = this.num;
		while (counter-- > 0) {
			this.put(OUTPUT_PORT.OUTPUT, this.outputRecord);
		}

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

	public void execute(final TaskBundle taskBundle) {
		// TODO Auto-generated method stub

	}

}
