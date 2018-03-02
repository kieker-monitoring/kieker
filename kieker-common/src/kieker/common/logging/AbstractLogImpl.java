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
package kieker.common.logging;

/**
 * Abstract superclass for all log implementations.
 * 
 * @author Holger Knoche
 * @since 1.14
 */
public abstract class AbstractLogImpl implements Log {

	public AbstractLogImpl() {
		// Empty constructor
	}

	@Override
	public void error(final String format, final Object... arguments) {
		if (this.isErrorEnabled()) {
			final String formattedMessage = String.format(format, arguments);
			this.error(formattedMessage);
		}
	}
	
	@Override
	public void error(final String format, final Throwable cause, final Object... arguments) {
		if (this.isErrorEnabled()) {
			final String formattedMessage = String.format(format, arguments);
			this.error(formattedMessage, cause);
		}
	}

	protected boolean isErrorEnabled() {
		return true;
	}

}
