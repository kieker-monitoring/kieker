package kieker.monitoring.writer;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kieker.common.record.IMonitoringRecord;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
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
 * ==================================================
 */

/**
 * A writer that does nothing but consuming records.
 * 
 * @author Andre van Hoorn, Jan Waller
 * 
 */
public class DummyLogWriter extends AbstractMonitoringLogWriter {
	private static final Log log = LogFactory.getLog(DummyLogWriter.class);

	public DummyLogWriter(final Properties properties) {
		super(properties);
		log.info(this.getClass().getName() + " initialized with properties:\n" + getProperties());
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		return true; // we don't care about incoming records
	}

	@Override
	public void start() {
		log.info(this.getClass().getName() + " started");
	}
	
	@Override
	public void terminate() {
		log.info(this.getClass().getName() + " shutting down");
	}

	@Override
	public Properties getDefaultProperties() {
		return null; //no Properties
	}
}
