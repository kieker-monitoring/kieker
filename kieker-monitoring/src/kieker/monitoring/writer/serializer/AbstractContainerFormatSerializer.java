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
public abstract class AbstractContainerFormatSerializer extends AbstractBinaryRecordSerializer {

	/** Container format identifier. */
	public static final int CONTAINER_IDENTIFIER = FormatIdentifier.CONTAINER_FORMAT.getIdentifierValue();

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
	public final void serializeRecordToByteBuffer(final IMonitoringRecord record, final ByteBuffer buffer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void serializeRecordsToByteBuffer(final Collection<IMonitoringRecord> records, final ByteBuffer buffer) {
		// Put the container header into the file
		buffer.putInt(CONTAINER_IDENTIFIER);
		buffer.putInt(this.getFormatIdentifier());

		// Write the actual payload
		this.writeRecords(records, buffer);
	}

	/**
	 * Write the given records to the given byte buffer.
	 * 
	 * @param records
	 *            The records to write
	 * @param buffer
	 *            The buffer to write to
	 */
	protected abstract void writeRecords(final Collection<IMonitoringRecord> records, final ByteBuffer buffer);

	/**
	 * Returns the format identifier for the contained data format.
	 * 
	 * @return see above
	 */
	protected abstract int getFormatIdentifier();

}
