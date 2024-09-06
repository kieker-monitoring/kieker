/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.log.replayer.stages;

import java.util.Date;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.log.replayer.stages.time.adjuster.ITimeAdjuster;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Adjust time stamps in records.
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class AdjustTimeStage extends AbstractConsumerStage<IMonitoringRecord> {

	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();
	private Long timeDelta;
	private final ITimeAdjuster[] timeAdjusters;

	/**
	 * Create an instance of the timestamp rewriter stage.
	 *
	 * @param timeAdjusters
	 *            sequence of {@link ITimeAdjuster}s to correct timestamps in event types.
	 */
	public AdjustTimeStage(final ITimeAdjuster... timeAdjusters) {
		this.timeAdjusters = timeAdjusters;
	}

	@Override
	protected void execute(final IMonitoringRecord element) throws Exception {
		if (this.timeDelta == null) {
			this.timeDelta = new Date().getTime() - element.getLoggingTimestamp();
		}
		element.setLoggingTimestamp(element.getLoggingTimestamp() + this.timeDelta);
		for (final ITimeAdjuster timeAdjuster : this.timeAdjusters) {
			timeAdjuster.apply(element, this.timeDelta);
		}
		this.outputPort.send(element);
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}

}
