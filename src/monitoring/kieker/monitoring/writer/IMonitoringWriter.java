package kieker.monitoring.writer;

import kieker.common.record.IMonitoringRecordReceiver;
import kieker.monitoring.core.controller.IWriterController;

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
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 */
public interface IMonitoringWriter extends IMonitoringRecordReceiver {

	/**
	 * Called by the Monitoring Controller to announce a shutdown of monitoring.
	 * Writers should return as soon as it is safe to terminate Kieker.
	 */
	abstract public void terminate();

	/**
	 * Returns a human-readable information string about the writer's configuration and state.
	 *
	 * @return the information string.
	 */
	abstract public String getInfoString();


	/**
	 * Set the <code>IWriterController</code> controlling this writer.
	 *
	 * @param controller
	 * @throws Exception
	 */
	abstract public void setController(IWriterController controller) throws Exception;

}
