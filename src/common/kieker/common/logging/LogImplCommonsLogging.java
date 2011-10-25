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

package kieker.common.logging;

import org.apache.commons.logging.impl.Jdk14LoggerPatched;

/**
 * 
 * @author Jan Waller
 */
public final class LogImplCommonsLogging implements Log {
	private final org.apache.commons.logging.Log log;

	public LogImplCommonsLogging(final String name) {
		this.log = Jdk14LoggerPatched.getLog(name);
	}

	@Override
	public void debug(final String message) {
		if (this.log.isDebugEnabled()) {
			this.log.debug(message);
		}
	}

	@Override
	public void debug(final String message, final Throwable t) {
		if (this.log.isDebugEnabled()) {
			this.log.debug(message, t);
		}
	}

	@Override
	public void info(final String message) {
		if (this.log.isInfoEnabled()) {
			this.log.info(message);
		}
	}

	@Override
	public void info(final String message, final Throwable t) {
		if (this.log.isInfoEnabled()) {
			this.log.info(message, t);
		}
	}

	@Override
	public void warn(final String message) {
		if (this.log.isWarnEnabled()) {
			this.log.warn(message);
		}
	}

	@Override
	public void warn(final String message, final Throwable t) {
		if (this.log.isWarnEnabled()) {
			this.log.warn(message, t);
		}
	}

	@Override
	public void error(final String message) {
		if (this.log.isErrorEnabled()) {
			this.log.error(message);
		}
	}

	@Override
	public void error(final String message, final Throwable t) {
		if (this.log.isErrorEnabled()) {
			this.log.error(message, t);
		}
	}

	@Override
	public void fatal(final String message) {
		if (this.log.isFatalEnabled()) {
			this.log.fatal(message);
		}
	}

	@Override
	public void fatal(final String message, final Throwable t) {
		if (this.log.isFatalEnabled()) {
			this.log.fatal(message, t);
		}
	}
}
