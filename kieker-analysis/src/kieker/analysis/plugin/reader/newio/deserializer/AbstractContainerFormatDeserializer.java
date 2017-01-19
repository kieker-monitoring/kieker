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

package kieker.analysis.plugin.reader.newio.deserializer;

import java.nio.ByteBuffer;
import java.util.List;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * @author holger
 *
 * @since 1.13
 */
public abstract class AbstractContainerFormatDeserializer extends AbstractMonitoringRecordDeserializer {

	/** Container format identifier. Reads "KIKA" in ASCII encoding. */
	public static final int CONTAINER_IDENTIFIER = 0x4B494B41;

	private static final int HEADER_SIZE = 8;

	/**
	 * Creates a new record deserializer.
	 *
	 * @param configuration
	 *            The configuration to use
	 * @param projectContext
	 *            The project context to use
	 */
	public AbstractContainerFormatDeserializer(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public List<IMonitoringRecord> deserializeRecords(final ByteBuffer buffer, final int dataSize) {
		// Check the container identifier
		final int magic = buffer.getInt();
		if (magic != CONTAINER_IDENTIFIER) {
			throw new InvalidFormatException(String.format("Invalid magic value %08x was found.", magic));
		}

		// Check the actual format identifier
		final int formatIdentifier = buffer.getInt();
		if (formatIdentifier != this.getFormatIdentifier()) {
			throw new InvalidFormatException(String.format("An invalid format identifier %08x was found.", formatIdentifier));
		}

		return this.decodeRecords(buffer, (dataSize - HEADER_SIZE));
	}

	/**
	 * Decodes the records from the container's payload.
	 *
	 * @param buffer
	 *            The buffer to decode the data from. It is positioned at the
	 *            first byte of the payload
	 * @param dataSize
	 *            The size of the payload data in bytes
	 * @return The decoded records
	 * @throws InvalidFormatException
	 *             When an invalid data format is encountered
	 */
	protected abstract List<IMonitoringRecord> decodeRecords(ByteBuffer buffer, int dataSize)
			throws InvalidFormatException;

	protected abstract int getFormatIdentifier();

}
