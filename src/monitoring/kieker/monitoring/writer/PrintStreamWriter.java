/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 */
public class PrintStreamWriter extends AbstractMonitoringWriter {
	private static final Log LOG = LogFactory.getLog(PrintStreamWriter.class);
	private static final String PREFIX = PrintStreamWriter.class.getName() + ".";
	private static final String STREAM = PrintStreamWriter.PREFIX + "Stream";

	private static final String CONFIG_STREAM_STDOUT = "STDOUT";
	private static final String CONFIG_STREAM_STDERR = "STDERR";

	private static final String ENCODING = "UTF-8";

	private PrintStream printStream;

	public PrintStreamWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public void init() throws FileNotFoundException, UnsupportedEncodingException {
		final String printStreamName = this.configuration.getStringProperty(PrintStreamWriter.STREAM);
		if (PrintStreamWriter.CONFIG_STREAM_STDOUT.equals(printStreamName)) {
			this.printStream = System.out;
		} else if (PrintStreamWriter.CONFIG_STREAM_STDERR.equals(printStreamName)) {
			this.printStream = System.err;
		} else {
			this.printStream = new PrintStream(new FileOutputStream(printStreamName), false, PrintStreamWriter.ENCODING);
		}
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		this.printStream.println(record.getClass().getSimpleName() + ": " + record.toString());
		return true;
	}

	@Override
	public void terminate() {
		if ((this.printStream != null) && (this.printStream != System.out) && (this.printStream != System.err)) {
			this.printStream.close();
		}
		PrintStreamWriter.LOG.info(this.getClass().getName() + " shutting down.");
	}
}
