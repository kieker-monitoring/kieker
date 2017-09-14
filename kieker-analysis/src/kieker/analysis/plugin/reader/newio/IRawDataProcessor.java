/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.ByteBuffer;

/**
 * Interface for raw data processors, i.e. classes which decode raw data to
 * records and feed them into a processing pipeline.
 * 
 * @author Holger Knoche
 *
 * @since 1.14
 */
public interface IRawDataProcessor {
	
	/**
	 * Decodes the given raw data and delivers the decoded records.
	 * 
	 * @param rawData
	 *            The raw data to decode
	 *            
	 * @since 1.14
	 */
	public void decodeAndDeliverRecords(byte[] rawData);
	
	/**
	 * Decodes the given raw data and delivers the decoded records.
	 * 
	 * @param rawData
	 *            The raw data to decode
	 * @param dataSize
	 *            The size of the contained data
	 *            
	 * @since 1.14
	 */
	public void decodeAndDeliverRecords(final ByteBuffer rawData, final int dataSize);

}
