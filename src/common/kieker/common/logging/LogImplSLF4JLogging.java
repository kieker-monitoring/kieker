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

package kieker.common.logging;

/**
 * @author Jan Waller
 */
public class LogImplSLF4JLogging implements Log {

	private final org.slf4j.Logger log;

	public LogImplSLF4JLogging(final String name) {
		this.log = org.slf4j.LoggerFactory.getLogger(name);
	}

	public boolean isDebugEnabled() {
		return this.log.isDebugEnabled();
	}

	public void debug(final String message) {
		this.log.debug(message);
	}

	public void debug(final String message, final Throwable t) {
		this.log.debug(message, t);
	}

	public void info(final String message) {
		this.log.info(message);
	}

	public void info(final String message, final Throwable t) {
		this.log.info(message, t);
	}

	public void warn(final String message) {
		this.log.warn(message);
	}

	public void warn(final String message, final Throwable t) {
		this.log.warn(message, t);
	}

	public void error(final String message) {
		this.log.error(message);
	}

	public void error(final String message, final Throwable t) {
		this.log.error(message, t);
	}
}
