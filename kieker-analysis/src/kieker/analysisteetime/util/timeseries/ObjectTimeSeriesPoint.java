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
 * {@link ITimeSeriesPoint} wrapper for any type.
 *
 * @param <X>
 *            Type of this time series point value
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class ObjectTimeSeriesPoint<X> extends AbstractTimeSeriesPoint {

	private final X value;

	public ObjectTimeSeriesPoint(final Instant time, final X value) {
		super(time);
		this.value = value;
	}

	public X getValue() {
		return this.value;
	}

	@Override
	public String valueToString() {
		return this.value.toString();
	}

}
