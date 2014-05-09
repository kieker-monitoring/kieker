/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.Description;
import kieker.panalysis.framework.core.IInputPort;

/**
 * @author Matthias Rohr, Jan Waller, Nils Christian Ehmke
 * 
 * @since 1.10
 */
@Description("A filter to print objects to a configured stream")
public class Printer<T> extends AbstractFilter<Printer<T>> {

	public static final String STREAM_STDOUT = "STDOUT";
	public static final String STREAM_STDERR = "STDERR";
	public static final String STREAM_STDLOG = "STDlog";
	public static final String STREAM_NULL = "NULL";

	public static final String ENCODING_UTF8 = "UTF-8";

	public final IInputPort<Printer<T>, T> input = this.createInputPort();

	private PrintStream printStream;
	private String streamName = STREAM_STDOUT;
	private String encoding = ENCODING_UTF8;
	private boolean active = true;
	private boolean append = true;

	public String getStreamName() {
		return this.streamName;
	}

	public void setStreamName(final String streamName) {
		this.streamName = streamName;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public void setEncoding(final String encoding) {
		this.encoding = encoding;
	}

	public boolean isAppend() {
		return this.append;
	}

	public void setAppend(final boolean append) {
		this.append = append;
	}

	@Override
	public void onPipelineStarts() throws Exception {
		super.onPipelineStarts();
		this.initializeStream();
	}

	@Override
	public void onPipelineStops() {
		this.closeStream();
		super.onPipelineStops();
	}

	private void initializeStream() {
		if (STREAM_STDOUT.equals(this.streamName)) {
			this.printStream = System.out;
			this.active = true;
		} else if (STREAM_STDERR.equals(this.streamName)) {
			this.printStream = System.err;
			this.active = true;
		} else if (STREAM_STDLOG.equals(this.streamName)) {
			this.printStream = null;
			this.active = true;
		} else if (STREAM_NULL.equals(this.streamName)) {
			this.printStream = null;
			this.active = false;
		} else {
			try {
				this.printStream = new PrintStream(new FileOutputStream(this.streamName, this.append), false, this.encoding);
				this.active = true;
			} catch (final FileNotFoundException ex) {
				this.active = false;
				super.logger.warn("Stream could not be created", ex);
			} catch (final UnsupportedEncodingException ex) {
				this.active = false;
				super.logger.warn("Encoding not supported", ex);
			}
		}
	}

	private void closeStream() {
		if ((this.printStream != null) && (this.printStream != System.out) && (this.printStream != System.err)) {
			this.printStream.close();
		}
	}

	@Override
	protected boolean execute(final Context<Printer<T>> context) {
		final T object = context.tryTake(this.input);
		if (null == object) {
			return false;
		}

		if (this.active) {
			final StringBuilder sb = new StringBuilder(128);

			sb.append(super.getId());
			sb.append('(').append(object.getClass().getSimpleName()).append(") ").append(object.toString());

			final String msg = sb.toString();
			if (this.printStream != null) {
				this.printStream.println(msg);
			} else {
				super.logger.info(msg);
			}
		}

		return true;
	}

}
