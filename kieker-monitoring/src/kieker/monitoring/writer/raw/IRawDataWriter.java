/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.raw;

import java.nio.ByteBuffer;

/**
 * Interface for raw data writers.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public interface IRawDataWriter {

	/**
	 * Writes raw data contained in the given byte buffer. The buffer is expected to be "flipped", i.e. the
	 * data resides between offset 0 and the buffer limit.
	 *
	 * @param data
	 *            The buffer containing the data
	 * @since 1.13
	 */
	public void writeData(ByteBuffer data);

	/**
	 * Called by the collector during initialization (before any records are written).
	 *
	 * @since 1.13
	 */
	void onInitialization();

	/**
	 * Called by the collector upon termination (after remaining records have been flushed).
	 *
	 * @since 1.13
	 */
	void onTermination();

	/**
	 * Denotes whether this writer requires data chunks to be wrapped (see {@link}).
	 *
	 * @return see above
	 *
	 * @since 2.0
	 */
	public boolean requiresWrappedData();

}
