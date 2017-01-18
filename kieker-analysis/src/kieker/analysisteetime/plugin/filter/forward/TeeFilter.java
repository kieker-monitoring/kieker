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

package kieker.analysisteetime.plugin.filter.forward;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import teetime.stage.basic.AbstractFilter;

/**
 * This filter has exactly one input port and one output port.
 *
 * A simple message is printed to a configurable stream and all objects are forwarded to the output port.
 *
 * @author Matthias Rohr, Jan Waller, Lars Bluemke
 *
 * @since 1.2
 */
public class TeeFilter extends AbstractFilter<Object> {

	/** The default value of the encoding property which determines that the filter uses utf-8. */
	public static final String DEFAULT_ENCODING = "UTF-8";
	/** The default value of the stream property which determines that the filter appends or overwrites a file. */
	public static final boolean DEFAULT_STREAM_APPEND = true;

	private final PrintStream printStream;
	private final String printStreamName;
	private final boolean active;

	private final boolean append;
	private final String encoding;

	/**
	 * Predefined types of streams for the {@link TeeFilter}.<br>
	 * <b>STDLOG</b> = standard log<br>
	 * <b>STDOUT</b> = standard output<br>
	 * <b>STDERR</b> = standard error output<br>
	 * <b>NULL</b> = filter doesn't print anything
	 *
	 * @since 1.13
	 */
	public enum TeeFilterStreamType {
		STDLOG, STDOUT, STDERR, NULL
	}

	/**
	 * Creates a new instance of this class using the given parameters. Uses one of the predefined {@link TeeFilterStreamType}s.
	 *
	 * @param streamType
	 *            Determines which of the predefined {@link TeeFilterStreamType}s the filter uses.
	 * @param encoding
	 *            Determines which encoding the filter uses. Pass null for default encoding {@value #DEFAULT_ENCODING}.
	 */
	public TeeFilter(final TeeFilterStreamType streamType, final String encoding) {

		// Get the encoding.
		if (encoding != null) {
			this.encoding = encoding;
		} else {
			this.encoding = DEFAULT_ENCODING;
		}

		this.printStreamName = null; // NOPMD (null)
		this.append = false;

		// Decide which stream to be used
		if (streamType.equals(TeeFilterStreamType.STDLOG)) {
			this.printStream = null; // NOPMD (null)
			this.active = true;
		} else if (streamType.equals(TeeFilterStreamType.STDOUT)) {
			this.printStream = System.out;
			this.active = true;
		} else if (streamType.equals(TeeFilterStreamType.STDERR)) {
			this.printStream = System.err;
			this.active = true;
		} else {
			this.printStream = null; // NOPMD (null)
			this.active = false;
		}
	}

	/**
	 * Creates a new instance of this class using the given parameters. Uses a {@link FileOutputStream}.
	 *
	 * @param fileName
	 *            The system dependent file name for the {@link FileOutputStream}.
	 * @param encoding
	 *            Determines which encoding the filter uses. Pass null for default encoding {@value #DEFAULT_ENCODING}.
	 * @param append
	 *            Determines whether the filter appends or overwrites a file.
	 */
	public TeeFilter(final String fileName, final String encoding, final boolean append) {

		// Get the encoding.
		if (encoding != null) {
			this.encoding = encoding;
		} else {
			this.encoding = DEFAULT_ENCODING;
		}

		this.append = append;
		PrintStream tmpPrintStream;
		try {
			tmpPrintStream = new PrintStream(new FileOutputStream(fileName, this.append), false, this.encoding);
		} catch (final UnsupportedEncodingException ex) {
			this.logger.error("Failed to initialize " + fileName, ex);
			tmpPrintStream = null; // NOPMD (null)
		} catch (final FileNotFoundException ex) {
			this.logger.error("Failed to initialize " + fileName, ex);
			tmpPrintStream = null; // NOPMD (null)
		}
		this.printStream = tmpPrintStream;
		this.printStreamName = fileName;
		this.active = true;
	}

	@Override
	public void onTerminating() throws Exception {
		if ((this.printStream != null) && (this.printStream != System.out) && (this.printStream != System.err)) {
			this.printStream.close();
		}
		super.onTerminating();
	}

	/**
	 * This method receives incoming objects from the filter's input port. Every object will be printed into a stream (based on the configuration) before the
	 * filter sends it to the output port.
	 *
	 * @param object
	 *            The new object.
	 */
	@Override
	protected void execute(final Object object) {
		if (this.active) {
			final StringBuilder sb = new StringBuilder(128);
			sb.append(this.getId());
			sb.append('(').append(object.getClass().getSimpleName()).append(") ").append(object.toString());
			final String record = sb.toString();
			if (this.printStream != null) {
				this.printStream.println(record);
			} else {
				this.logger.info(record);
			}
		}
		this.outputPort.send(object);
	}

	public String getPrintStreamName() {
		return this.printStreamName;
	}
}
