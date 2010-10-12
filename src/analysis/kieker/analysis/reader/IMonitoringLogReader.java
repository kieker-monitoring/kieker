package kieker.analysis.reader;

import kieker.common.record.IMonitoringRecordReceiver;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 *
 * This reader allows one to read a folder or an single tpmon file and
 * transforms it to monitoring events that are stored in the file system
 * again, written to a database, or whatever tpmon is configured to do
 * with the monitoring data.
 */
/**
 * 
 * @author Andre van Hoorn
 */
public interface IMonitoringLogReader {

	/**
	 * Initialize instance from passed initialization string which is typically
	 * a list of separated parameter/values pairs. The implementing class
	 * AbstractMonitoringLogWriter includes convenient methods to extract
	 * configuration values from an initString.
	 * 
	 * @param initString
	 *            the initialization string
	 * @return true if the initialization was successful; false if an error occurred
	 */
	public boolean init(String initString);

	/**
	 * Adds the given record receiver. This method is only used by the framework
	 * and should not be called manually to register a receiver. Use an
	 * AnalysisInstance instead.
	 * 
	 * @param receiver
	 *            the receiver
	 */
	public void addRecordReceiver(IMonitoringRecordReceiver receiver);

	/**
	 * Starts the reader. This method is intended to be a blocking operation,
	 * i.e., it is assumed that reading has finished before this method returns.
	 * The method should indicate an error by the return value false.
	 * 
	 * @return true if reading was successful; false if an error occurred
	 */
	public boolean read();
}
