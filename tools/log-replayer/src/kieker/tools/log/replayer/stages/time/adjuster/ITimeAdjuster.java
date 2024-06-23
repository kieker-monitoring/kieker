/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.log.replayer.stages.time.adjuster;

import kieker.common.record.IMonitoringRecord;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public interface ITimeAdjuster {

	/**
	 * Modify time values in a specific {@link MonitoringRecord} type.
	 *
	 * @param record
	 *            record
	 * @param timeDelta
	 *            time delta to be added
	 */
	void apply(IMonitoringRecord record, long timeDelta);
}
