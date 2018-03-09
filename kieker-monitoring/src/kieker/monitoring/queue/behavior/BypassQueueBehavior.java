/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.queue.behavior;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * Insert behavior that bypasses the writer controller's queue and thus calls the writer synchronously. This
 * behavior is useful for writers such as the chunking collector which have their own queues and asynchronous
 * workers.
 *  
 * @author Holger Knoche
 * @since 1.13
 */
public class BypassQueueBehavior implements InsertBehavior<IMonitoringRecord> {

	private final AbstractMonitoringWriter writer;
	
	public BypassQueueBehavior(final AbstractMonitoringWriter writer) {
		this.writer = writer;
	}
	
	@Override
	public boolean insert(final IMonitoringRecord element) {
		this.writer.writeMonitoringRecord(element);
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder()
			.append(this.getClass());
		return builder.toString();
	}
}
