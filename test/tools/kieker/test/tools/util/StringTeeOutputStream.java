/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
 * @author Andre van Hoorn
 */
public class StringTeeOutputStream extends OutputStream {
	private final StringWriter stringWriter = new StringWriter();
	private final OutputStream interceptedStream;

	public StringTeeOutputStream(final OutputStream teeStream) {
		this.interceptedStream = teeStream;
	}

	@Override
	public void write(final int b) throws IOException {
		this.stringWriter.write(b);
		this.interceptedStream.write(b);
	}

	public String getString() {
		return this.stringWriter.getBuffer().toString();
	}
}
