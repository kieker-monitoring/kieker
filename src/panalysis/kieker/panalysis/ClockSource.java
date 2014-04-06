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

import java.util.Timer;
import java.util.TimerTask;

import kieker.panalysis.base.AbstractSource;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ClockSource extends AbstractSource<ClockSource.OUTPUT_PORT> {

	public static enum OUTPUT_PORT { // NOCS
		CLOCK_SIGNAL
	}

	private final Timer timer;

	public ClockSource(final long delay, final long period) {
		super(OUTPUT_PORT.class);

		final TimerTask task = new TimerTask() {
			@SuppressWarnings("synthetic-access")
			@Override
			public void run() {
				ClockSource.this.put(OUTPUT_PORT.CLOCK_SIGNAL, Boolean.TRUE);
			}
		};

		this.timer = new Timer();
		this.timer.scheduleAtFixedRate(task, delay, period);
	}

	public boolean execute() {
		// see timer execution
		return true;
	}

	@Override
	public void cleanUp() {
		this.timer.cancel();
	}

}
