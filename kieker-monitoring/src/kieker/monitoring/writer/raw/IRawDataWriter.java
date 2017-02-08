/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
 * @author Holger Knoche
 *
 * @since 1.13
 */
public interface IRawDataWriter {

	/**
	 * Writes raw data contained in the given byte buffer.
	 *
	 * @param data
	 *            The buffer containing the data
	 * @param offset
	 *            The offset in the buffer where the data starts
	 * @param length
	 *            The length of the data to write
	 * @since 1.13
	 */
	public void writeData(ByteBuffer data, int offset, int length);

	/**
	 * Called by the collector before records are written.
	 * 
	 * @since 1.13
	 */
	public void init();

	/**
	 * Called by the collector to announce a shutdown of monitoring. Writers
	 * should return as soon as it is safe to terminate Kieker.
	 * 
	 * @since 1.13
	 */
	public void terminate();

}
