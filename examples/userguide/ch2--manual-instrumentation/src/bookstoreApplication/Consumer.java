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

package bookstoreApplication;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.common.configuration.Configuration;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.analysis.repository.AbstractRepository;

public class Consumer extends AbstractAnalysisPlugin {

	private final long maxResponseTime;
	public static final String INPUT_PORT_NAME = "newEvent";

	public Consumer(final long maxResponseTime) {
		super(new Configuration(null), new AbstractRepository[0]);
		this.maxResponseTime = maxResponseTime;
	}

	@InputPort(eventTypes = { IMonitoringRecord.class })
	public void newEvent(final Object event) {
		if (!(event instanceof OperationExecutionRecord)) {
			return;
		}
		final OperationExecutionRecord rec = (OperationExecutionRecord) event;
		/* Derive response time from the record. */
		final long responseTime = rec.getTout() - rec.getTin();
		/* Now compare with the response time threshold: */
		if (responseTime > this.maxResponseTime) {
			System.err.println("maximum response time exceeded by "
					+ (responseTime - this.maxResponseTime) + " ns: " + rec.getClassName()
					+ "." + rec.getOperationName());
		} else {
			System.out.println("response time accepted: " + rec.getClassName()
					+ "." + rec.getOperationName());
		}
		return;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(final boolean error) {}

	public Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
	
	public AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}

	public AbstractRepository[] getCurrentRepositories() {
		return new AbstractRepository[0];
	}
}
