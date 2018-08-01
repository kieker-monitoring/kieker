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

package kieker.test.tools.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

/**
 * This simple helper class wraps a given {@link OutputStream} but intercepts everything that is written to this instance via
 * {@link StringTeeOutputStream#write(int)}. The written content can later be accessed.
 * 
 * @see StringTeePrintStream
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.6
 */
public class StringTeeOutputStream extends OutputStream {

	private final StringWriter stringWriter = new StringWriter();
	private final OutputStream interceptedStream;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param teeStream
	 *            The stream to be intercepted.
	 */
	public StringTeeOutputStream(final OutputStream teeStream) {
		this.interceptedStream = teeStream;
	}

	@Override
	public void write(final int b) throws IOException {
		this.stringWriter.write(b);
		this.interceptedStream.write(b);
	}

	/**
	 * Delivers a string representation of the stream's content.
	 * 
	 * @return The written content.
	 */
	public String getString() {
		return this.stringWriter.getBuffer().toString();
	}
}
