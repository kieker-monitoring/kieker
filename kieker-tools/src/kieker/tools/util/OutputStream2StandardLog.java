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

package kieker.tools.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of an {@link OutputStream} that redirects data received via
 * its {@link #write(int)} method to an instance of {@link Logger}.
 *
 * @since 1.10
 * @author Andre van Hoorn, Tillmann Carlos Bielefeld
 */
public class OutputStream2StandardLog extends OutputStream {
	private static final Logger LOGGER = LoggerFactory.getLogger(OutputStream2StandardLog.class);

	private static final int LINE_END = '\n';
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	/**
	 * Default constructor.
	 */
	public OutputStream2StandardLog() {
		super();
	}

	@Override
	public void write(final int b) throws IOException {
		if (b == LINE_END) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace(this.baos.toString("UTF-8")); // Redirect previous log message from RSession as log message
			}
			this.baos.reset();
		} else {
			this.baos.write(b);
		}
	}
}
