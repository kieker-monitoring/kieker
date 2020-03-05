/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.plugin.reader.newio;

/**
 * Interface for raw data readers.
 * 
 * @author Holger Knoche
 * @since 1.13
 */
public interface IRawDataReader {
	
	/**
	 * Event handler that is called before any records are read.
	 * @return The outcome of the initialization
	 * @since 1.13
	 */
	public Outcome onInitialization();
	
	/**
	 * Starts the read operation.
	 * @return The outcome of the read operation
	 * @since 1.13
	 */
	public Outcome read();
	
	/**
	 * Event handler that is called when the reader is supposed to terminate.
	 * @return The outcome of the termination
	 * @since 1.13
	 */
	public Outcome onTermination();

}
