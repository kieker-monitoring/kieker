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

package kieker.monitoring.writer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * A writer that prints incoming records to the specified PrintStream.
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public class PrintStreamWriter extends AbstractMonitoringWriter {
	private static final Log LOG = LogFactory.getLog(PrintStreamWriter.class);

	private static final String PREFIX = PrintStreamWriter.class.getName() + ".";
	public static final String STREAM = PREFIX + "Stream"; // NOCS (decl. order)
	public static final String CONFIG_STREAM_STDOUT = "STDOUT"; // NOCS (decl. order)
	public static final String CONFIG_STREAM_STDERR = "STDERR"; // NOCS (decl. order)

	private static final String ENCODING = "UTF-8";

	private final String configPrintStreamName;

	private PrintStream printStream;

	/**
	 * Creates a new instance of this writer.
	 * 
	 * @param configuration
	 *            The configuration which will be used to initialize this writer.
	 */
	public PrintStreamWriter(final Configuration configuration) {
		super(configuration);
		this.configPrintStreamName = configuration.getStringProperty(STREAM);
	}

	@Override
	public void init() throws FileNotFoundException, UnsupportedEncodingException {

		if (CONFIG_STREAM_STDOUT.equals(this.configPrintStreamName)) {
			this.printStream = System.out;
		} else if (CONFIG_STREAM_STDERR.equals(this.configPrintStreamName)) {
			this.printStream = System.err;
		} else {
			this.printStream = new PrintStream(new FileOutputStream(this.configPrintStreamName), false, ENCODING);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		this.printStream.println(record.getClass().getSimpleName() + ": " + record.toString());
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate() {
		if ((this.printStream != null) && (this.printStream != System.out) && (this.printStream != System.err)) {
			this.printStream.close();
		}
		LOG.info(this.getClass().getName() + " shutting down.");
	}
}
