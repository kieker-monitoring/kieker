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

package kieker.analysisteetime.plugin.filter.forward;

import kieker.analysis.plugin.filter.forward.util.KiekerHashMap;
import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

import teetime.stage.basic.AbstractFilter;

/**
 * This filter has exactly one input port and one output port.
 *
 * Every record received is cloned and each detected String is buffered in a shared area in order to save memory.
 *
 * @author Jan Waller, Lars Bluemke
 *
 * @since 1.6
 */
public class StringBufferFilter extends AbstractFilter<Object> {

	private final KiekerHashMap kiekerHashMap = new KiekerHashMap();

	/**
	 * Creates a new instance of this class.
	 */
	public StringBufferFilter() {}

	@SuppressWarnings("unchecked")
	@Override
	protected void execute(final Object object) {
		if (object instanceof String) {
			this.outputPort.send(this.kiekerHashMap.get((String) object));
		} else if (object instanceof IMonitoringRecord) {
			final Object[] objects = ((IMonitoringRecord) object).toArray();
			boolean stringBuffered = false;
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] instanceof String) {
					objects[i] = this.kiekerHashMap.get((String) objects[i]);
					stringBuffered = true;
				}
			}
			if (stringBuffered) {
				try {
					final IMonitoringRecord newRecord = AbstractMonitoringRecord.createFromArray((Class<? extends IMonitoringRecord>) object.getClass(), objects);
					newRecord.setLoggingTimestamp(((IMonitoringRecord) object).getLoggingTimestamp());
					this.outputPort.send(newRecord);
				} catch (final MonitoringRecordException ex) {
					this.logger.warn("Failed to recreate buffered monitoring record: " + object.toString(), ex);
				}
			} else {
				this.outputPort.send(object);
			}

		} else { // simply forward the object
			this.outputPort.send(object);
		}
	}

}
