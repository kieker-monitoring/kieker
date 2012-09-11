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
public final class LogImplCommonsLogging implements Log {
	private static final boolean JDK14PATCH;

	private final org.apache.commons.logging.Log log;

	static {
		final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog("KiekerTest");
		if ("org.apache.commons.logging.impl.Jdk14Logger".equals(log.getClass().getName())) {
			JDK14PATCH = true;
		} else {
			JDK14PATCH = false;
		}
	}

	protected LogImplCommonsLogging(final String name) {
		if (JDK14PATCH) {
			this.log = org.apache.commons.logging.impl.Jdk14LoggerPatched.getLog(name);
		} else {
			this.log = org.apache.commons.logging.LogFactory.getLog(name);
		}
	}

	public boolean isDebugEnabled() {
		return this.log.isDebugEnabled();
	}

	public void debug(final String message) {
		if (this.log.isDebugEnabled()) {
			this.log.debug(message);
		}
	}

	public void debug(final String message, final Throwable t) {
		if (this.log.isDebugEnabled()) {
			this.log.debug(message, t);
		}
	}

	public void info(final String message) {
		if (this.log.isInfoEnabled()) {
			this.log.info(message);
		}
	}

	public void info(final String message, final Throwable t) {
		if (this.log.isInfoEnabled()) {
			this.log.info(message, t);
		}
	}

	public void warn(final String message) {
		if (this.log.isWarnEnabled()) {
			this.log.warn(message);
		}
	}

	public void warn(final String message, final Throwable t) {
		if (this.log.isWarnEnabled()) {
			this.log.warn(message, t);
		}
	}

	public void error(final String message) {
		if (this.log.isErrorEnabled()) {
			this.log.error(message);
		}
	}

	public void error(final String message, final Throwable t) {
		if (this.log.isErrorEnabled()) {
			this.log.error(message, t);
		}
	}
}
