package kieker.monitoring.writer;

import java.util.Properties;

import kieker.common.record.IMonitoringRecord;
/* ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
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
 * ==================================================
 */

/**
 * @author Jan Waller
 */
abstract class AbstractMonitoringLogWriter implements IMonitoringLogWriter {

	protected final Properties properties;

	/**
	 * Initialize instance from passed initialization string which is typically a
	 * list of separated parameter/values pairs.
	 * 
	 * @param initString the initialization string
	 */
	protected AbstractMonitoringLogWriter(final Properties properties) {
		this.properties = new Properties(getDefaultProperties());
		for (String property : properties.stringPropertyNames()) {
			this.properties.setProperty(property, properties.getProperty(property));
		}
	}
	
	protected String getProperties() {
		final StringBuilder sb = new StringBuilder();
		for (String property : properties.stringPropertyNames()) {
			sb.append(property);
			sb.append("=");
			sb.append(properties.getProperty(property));
			sb.append("\n");		
		}
		return sb.toString();
	}

	public abstract Properties getDefaultProperties(); 
	
	@Override
	public abstract boolean newMonitoringRecord(IMonitoringRecord record);

	@Override
	public abstract void start();

	@Override
	public abstract void terminate();

	@Override
	public String getInfoString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Writer: ");
		sb.append(this.getClass().getName());
		sb.append("\n");
		sb.append(getProperties());
		return sb.toString();
	}
}
