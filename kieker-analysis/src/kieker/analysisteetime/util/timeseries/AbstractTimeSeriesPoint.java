/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util.timeseries;

import java.time.Instant;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public abstract class AbstractTimeSeriesPoint implements ITimeSeriesPoint {

	private final Instant time;

	public AbstractTimeSeriesPoint(final Instant time) {
		this.time = time;
	}

	@Override
	public Instant getTime() {
		return this.time;
	}

	@Override
	public final String toString() {
		return "[" + this.time + "=" + this.valueToString() + "]";
	}

	public abstract String valueToString();
}
