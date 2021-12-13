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

package kieker.monitoring.writer.print;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * A writer that prints incoming records to the specified PrintStream.
 *
 * @author Jan Waller
 *
 * @since 1.5
 */
public class PrintStreamWriter extends AbstractMonitoringWriter {

	private static final Logger LOGGER = LoggerFactory.getLogger(PrintStreamWriter.class);

	private static final String ENCODING = "UTF-8";
	private static final String PREFIX = PrintStreamWriter.class.getName() + ".";

	public static final String STREAM = PREFIX + "Stream"; // NOCS (decl. order)

	public static final String CONFIG_STREAM_STDOUT = "STDOUT"; // NOCS (decl. order)
	public static final String CONFIG_STREAM_STDERR = "STDERR"; // NOCS (decl. order)

	/** identifies which stream type this stream represents. */
	private final String configPrintStreamName;
	/** the stream to write out monitoring records. */
	private PrintStream printStream;

	/**
	 * Creates a new instance of this writer.
	 *
	 * @param configuration
	 *            The configuration which will be used to initialize this writer.
	 */
	// @SuppressFBWarnings("DM_DEFAULT_ENCODING")
	public PrintStreamWriter(final Configuration configuration) {
		super(configuration);
		this.configPrintStreamName = configuration.getStringProperty(STREAM);
		this.printStream = new PrintStream(new ByteArrayOutputStream()); // Null Object Pattern
	}

	@Override
	public void onStarting() {
		if (CONFIG_STREAM_STDOUT.equals(this.configPrintStreamName)) {
			this.printStream = System.out;
		} else if (CONFIG_STREAM_STDERR.equals(this.configPrintStreamName)) {
			this.printStream = System.err;
		} else {
			try {
				this.printStream = new PrintStream(new FileOutputStream(this.configPrintStreamName), false, ENCODING);
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				LOGGER.warn("An exception occurred", e);
			}
		}
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		this.printStream.println(record.getClass().getSimpleName() + ": " + record.toString());
	}

	@Override
	public void onTerminating() {
		if ((this.printStream != System.out) && (this.printStream != System.err)) {
			this.printStream.close();
		}
		LOGGER.info("{} shutting down.", this.getClass().getName());
	}
}
