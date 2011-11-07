/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.analysis.plugin;

import java.util.Collection;

import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author matthias
 */
public class DummyRecordConsumer extends AbstractAnalysisPlugin {

	/**
	 * Constructs a {@link DummyRecordConsumer}.
	 */
	public DummyRecordConsumer() {
		super.registerInputPort("in", new AbstractInputPort("in") {

			@Override
			public void newEvent(Object event) {
				newMonitoringRecord((IMonitoringRecord) event);
			}
		});
	}

	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		System.out.println("DummyRecordConsumer consumed (" + monitoringRecord.getClass().getSimpleName() + ") " + monitoringRecord);
		return true;
	}

	@Override
	public boolean execute() {
		System.out.println("DummyRecordConsumer.execute()");
		return true;
	}

	@Override
	public void terminate(final boolean error) {
		// nothing to do
	}

}
