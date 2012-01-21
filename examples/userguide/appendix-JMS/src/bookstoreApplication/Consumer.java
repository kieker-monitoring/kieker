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

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;

public class Consumer extends AbstractAnalysisPlugin {

	public static final String CONFIG_MAX_RESPONSE_TIME = Consumer.class.getName() + ".maxResponseTime";
	public static final String INPUT_PORT_NAME = "newMonitoringRecord";
	private final long maxResponseTime;

	public Consumer(final Configuration configuration, final AbstractRepository repositories[]) {
		super(configuration, repositories);
		
		this.maxResponseTime = configuration.getLongProperty(CONFIG_MAX_RESPONSE_TIME);
		System.out.println(this.maxResponseTime);
	}

	@InputPort(eventTypes = { IMonitoringRecord.class })
	public boolean newMonitoringRecord(final Object record) {
		if (!(record instanceof OperationExecutionRecord)) {
			return true;
		}
		final OperationExecutionRecord rec = (OperationExecutionRecord) record;
		/* Derive response time from the record. */
		final long responseTime = rec.getTout() - rec.getTin();
		/* Now compare with the response responseTime threshold: */
		if (responseTime > this.maxResponseTime) {
			System.err.println("maximum response time exceeded by "
					+ (responseTime - this.maxResponseTime) + " ns: " + rec.getClassName()
					+ "." + rec.getOperationName());
		} else {
			System.out.println("response time accepted: " + rec.getClassName()
					+ "." + rec.getOperationName());
		}
		return true;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(final boolean error) {}
	
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();
		
		configuration.setProperty(CONFIG_MAX_RESPONSE_TIME, Long.toString(Long.MAX_VALUE));
		
		return configuration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		
		configuration.setProperty(CONFIG_MAX_RESPONSE_TIME, Long.toString(this.maxResponseTime));
		
		return configuration;
	}
	
	@Override
	public AbstractRepository[] getDefaultRepositories() {
		return new AbstractRepository[0];
	}
	
	@Override
	public AbstractRepository[] getCurrentRepositories() {
		return new AbstractRepository[0];
	}
}
