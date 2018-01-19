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
package kieker.analysis.plugin.reader.pipe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.newio.AbstractRawDataReader;
import kieker.analysis.plugin.reader.newio.IRawDataProcessor;
import kieker.analysis.plugin.reader.newio.IRawDataUnwrapper;
import kieker.analysis.plugin.reader.newio.Outcome;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * A reader that reads data from a OS pipe, such as {@code stdin} or a named pipe.
 *
 * @author Holger Knoche
 * @since 2.0
 */
public class OSPipeReader extends AbstractRawDataReader {

	/** The name of the configuration property for the input pipe name. */
	public static final String CONFIG_PROPERTY_PIPE_NAME = "pipeName";

	private static final String SYMBOLIC_NAME_STDIN = "-";

	private static final Log LOG = LogFactory.getLog(OSPipeReader.class);

	private final IRawDataUnwrapper dataUnwrapper;

	private volatile boolean terminated;

	/**
	 * Creates a new OS pipe reader with the given configuration, sending the data to the given processor.
	 * @param configuration
	 * @param unwrapperType
	 * @param processor
	 */
	public OSPipeReader(final Configuration configuration, final Class<? extends IRawDataUnwrapper> unwrapperType, final IRawDataProcessor processor) throws AnalysisConfigurationException {
		super(processor);

		String pipeName = configuration.getStringProperty(CONFIG_PROPERTY_PIPE_NAME);

		// Assume stdin if no pipe name is given
		if (pipeName == null) {
			pipeName = SYMBOLIC_NAME_STDIN;
			LOG.info("No input pipe name given, stdin assumed.");
		}

		// Open input stream and instantiate an appropriate unwrapper
		final InputStream inputStream = this.openInputStream(pipeName);
		this.dataUnwrapper = this.instantiateUnwrapper(unwrapperType, inputStream);
	}

	private InputStream openInputStream(final String inputStreamName) throws AnalysisConfigurationException {
		try {
			// Open stdin if desired, otherwise try to open the file with the given name
			if (SYMBOLIC_NAME_STDIN.equals(inputStreamName)) {
				return System.in;
			} else {
				return new FileInputStream(inputStreamName);
			}
		} catch (final FileNotFoundException e) {
			throw new AnalysisConfigurationException("Unable to open input pipe.", e);
		}
	}

	private IRawDataUnwrapper instantiateUnwrapper(final Class<? extends IRawDataUnwrapper> unwrapperType, final InputStream inputStream) {
		if (unwrapperType == null) {
			LOG.error("No unwrapper type was supplied.");
			return null;
		}

		IRawDataUnwrapper unwrapper = null;

		try {
			// Try to instantiate the unwrapper
			unwrapper = unwrapperType.getConstructor(InputStream.class).newInstance(inputStream);
		} catch (final NoSuchMethodException e) {
			LOG.error("Class " + unwrapperType.getName() + " must implement a (public) constructor that accepts an input stream.", e);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException e) {
			LOG.error("Unable to instantiate " + unwrapperType.getName() + ".", e);
		}

		return unwrapper;
	}

	@Override
	public Outcome onInitialization() {
		return Outcome.SUCCESS;
	}

	@Override
	public Outcome read() {
		final IRawDataUnwrapper unwrapper = this.dataUnwrapper;

		if (unwrapper == null) {
			return Outcome.FAILURE;
		}

		try {
			Outcome outcome;

			if (unwrapper.supportsCharacterData()) {
				outcome = this.readCharacterData();
			} else {
				outcome = this.readBinaryData();
			}

			return outcome;
		} catch (final IOException e) {
			LOG.error("Error reading data.", e);
			return Outcome.FAILURE;
		}
	}

	private Outcome readBinaryData() throws IOException {
		final IRawDataUnwrapper unwrapper = this.dataUnwrapper;
		final IRawDataProcessor processor = this.dataProcessor;

		while (!this.terminated) {
			final ByteBuffer dataChunk = unwrapper.fetchBinaryData();

			// Null represents "end of data"
			if (dataChunk == null) {
				this.terminated = true;
				break;
			}

			processor.decodeBytesAndDeliverRecords(dataChunk, dataChunk.limit());
		}

		return Outcome.SUCCESS;
	}

	private Outcome readCharacterData() throws IOException {
		final IRawDataUnwrapper unwrapper = this.dataUnwrapper;
		final IRawDataProcessor processor = this.dataProcessor;

		while (!this.terminated) {
			final CharBuffer dataChunk = unwrapper.fetchCharacterData();

			// Null represents "end of data"
			if (dataChunk == null) {
				this.terminated = true;
				break;
			}

			processor.decodeCharactersAndDeliverRecords(dataChunk, dataChunk.limit());
		}

		return Outcome.SUCCESS;
	}

	@Override
	public Outcome onTermination() {
		this.terminated = true;
		return Outcome.SUCCESS;
	}

}
