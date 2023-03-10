/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.log.replayer;

import java.util.Date;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IEventRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Rewrite time stamps in records.
 *
 * @author Reiner Jung
 * @since 1.16
 *
 */
public class RewriteTime extends AbstractConsumerStage<IMonitoringRecord> {

	private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();
	private Long timeDelta;

	/**
	 * Create an instance of the timestamp rewriter stage.
	 */
	public RewriteTime() {
		// nothing to do
	}

	@Override
	protected void execute(final IMonitoringRecord element) throws Exception {
		if (this.timeDelta == null) {
			this.timeDelta = new Date().getTime() - element.getLoggingTimestamp();
		}
		element.setLoggingTimestamp(element.getLoggingTimestamp() + this.timeDelta);
		if (element instanceof IEventRecord) {
			final IEventRecord event = (IEventRecord) element;
			event.setTimestamp(event.getTimestamp() + this.timeDelta);
		}

		this.outputPort.send(element);
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}

}
