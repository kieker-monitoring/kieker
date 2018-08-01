/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer.serializer;

import java.nio.ByteBuffer;
import java.util.Collection;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.dataformat.FormatIdentifier;

/**
 * Abstract serializer for the Kieker container format for monitoring records. This container
 * format provides a flexible mechanism to embed data in different formats.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public abstract class AbstractContainerFormatSerializer extends AbstractMonitoringRecordSerializer {

	/** Container format identifier. */
	public static final int CONTAINER_IDENTIFIER = FormatIdentifier.CONTAINER_FORMAT.getIdentifierValue();

	private static final int HEADER_SIZE = 8;

	/**
	 * Creates a new serializer using the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use
	 */
	public AbstractContainerFormatSerializer(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public final int serializeRecord(final IMonitoringRecord record, final ByteBuffer buffer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final int serializeRecords(final Collection<IMonitoringRecord> records, final ByteBuffer buffer) {
		// Put the container header into the file
		buffer.putInt(CONTAINER_IDENTIFIER);
		buffer.putInt(this.getFormatIdentifier());

		// Write the actual payload
		final int bytesInPayload = this.writeRecords(records, buffer);

		return bytesInPayload + HEADER_SIZE;
	}

	/**
	 * Write the given records to the given byte buffer.
	 * 
	 * @param records
	 *            The records to write
	 * @param buffer
	 *            The buffer to write to
	 * @return The size of the written data in bytes
	 */
	protected abstract int writeRecords(final Collection<IMonitoringRecord> records, final ByteBuffer buffer);

	/**
	 * Returns the format identifier for the contained data format.
	 * 
	 * @return see above
	 */
	protected abstract int getFormatIdentifier();

}
