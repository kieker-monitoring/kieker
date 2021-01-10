/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.sink;

import java.util.HashMap;
import java.util.Map;

import teetime.framework.AbstractConsumerStage;

/**
 * Act like a null device.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public class NullStage extends AbstractConsumerStage<Object> {

	public static int DEFAULT_REPORT_INTERVAL = 100000;

	private long count;
	private final boolean silent;

	private final Map<Class<? extends Object>, Integer> types = new HashMap<>(); // NOPMD no concurrent access
	private final int reportInterval;

	/**
	 * Null stage.
	 *
	 * @param silent
	 *            silent operations.
	 */
	public NullStage(final boolean silent) {
		this(silent, DEFAULT_REPORT_INTERVAL);
	}

	/**
	 * Null stage.
	 *
	 * @param silent
	 *            silent operations.
	 * @param reportInterval
	 *            number of records to be received before logging
	 */
	public NullStage(final boolean silent, final int reportInterval) {
		this.silent = silent;
		this.reportInterval = reportInterval;
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminatiing {}", this.getClass().getCanonicalName());
		super.onTerminating();
	}

	@Override
	protected void execute(final Object record) {
		if (!this.silent) {
			Integer counter = this.types.get(record.getClass());
			if (counter == null) {
				counter = 1;
			} else {
				counter++;
			}
			this.types.put(record.getClass(), counter);

			this.count++;
			if ((this.count % this.reportInterval) == 0) {
				this.logger.info("{} records received.", this.count);
			}
			if ((counter % this.reportInterval) == 0) {
				this.logger.info("{} {} records received.", counter, record.getClass());
			}
		}
	}

	public long getCount() {
		return this.count;
	}

	public Map<Class<? extends Object>, Integer> getTypes() {
		return this.types;
	}

}
