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
package kieker.monitoring.writer.pipe;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.monitoring.writer.raw.IRawDataWriter;

/**
 * A writer that writes data to a OS pipe, such as {@code stdout} or a named
 * pipe.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public class OSPipeWriter implements IRawDataWriter {

	private static final String PREFIX = OSPipeWriter.class.getName() + ".";

	/** The name of the configuration property for the output pipe name. */
	public static final String CONFIG_PROPERTY_PIPE_NAME = PREFIX + "pipeName"; // NOCS Declaration order

	private static final String SYMBOLIC_NAME_STDOUT = "-";

	private static final Logger LOGGER = LoggerFactory.getLogger(OSPipeWriter.class);

	private final OutputStream outputStream;

	public OSPipeWriter(final Configuration configuration) {
		String pipeName = configuration.getStringProperty(CONFIG_PROPERTY_PIPE_NAME);

		// Assume stdout if no pipe name is given
		if (pipeName == null) {
			pipeName = SYMBOLIC_NAME_STDOUT;
			LOGGER.warn("No output name given, stout assumed.");
		}

		this.outputStream = this.openOutputStream(pipeName);
	}

	private OutputStream openOutputStream(final String outputStreamName) {
		try {
			// Open stdout if desired, otherwise try to open the file with the given name
			if (SYMBOLIC_NAME_STDOUT.equals(outputStreamName)) {
				return System.out;
			} else {
				return new FileOutputStream(outputStreamName);
			}
		} catch (final FileNotFoundException e) {
			LOGGER.error("Unable to open output pipe '{}'.", outputStreamName, e);
			return null;
		}
	}

	@Override
	public void onInitialization() {
		// Do nothing
	}

	@Override
	public void writeData(final ByteBuffer buffer) {
		final byte[] rawData = new byte[buffer.limit()];
		buffer.get(rawData);

		try {
			this.outputStream.write(rawData);
		} catch (final IOException e) {
			LOGGER.error("An exception occurred writing the data.", e);
		}
	}

	@Override
	public boolean requiresWrappedData() {
		return true;
	}

	@Override
	public void onTermination() {
		try {
			if (this.outputStream != null) {
				// Flush and close the output stream
				this.outputStream.flush();
				this.outputStream.close();
			}
		} catch (final IOException e) {
			LOGGER.error("Error closing output pipe.", e);
		}
	}

}
