/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader;

import kieker.analysis.plugin.IPlugin;

/**
 * This is the interface for reader plugins.
 * 
 * @author Andre van Hoorn
 * 
 * @since 0.95a
 */
public interface IReaderPlugin extends IPlugin {

	/**
	 * Starts the reader. This method is intended to be a blocking operation,
	 * i.e., it is assumed that reading has finished before this method returns.
	 * The method should indicate an error by the return value false.
	 * 
	 * In asynchronous scenarios, the {@link kieker.analysis.plugin.IPlugin#terminate(boolean)} method can be used
	 * to initiate the termination of this method.
	 * 
	 * @return true if reading was successful; false if an error occurred
	 * 
	 * @since 1.2
	 */
	public boolean read();
}
