/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import kieker.common.configuration.Configuration;
import kieker.monitoring.timer.AbstractTimeSource;

public class CustomTimeSource extends AbstractTimeSource {

	private final AtomicLong counter = new AtomicLong();

	public CustomTimeSource(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public long getTime() {
		return this.counter.getAndIncrement();
	}

	@Override
	public long getOffset() {
		return 0;
	}

	@Override
	public TimeUnit getTimeUnit() {
		return TimeUnit.MILLISECONDS;
	}

	@Override
	public String toString() {
		return "Custom TimeSource";
	}

}
