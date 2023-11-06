/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import java.util.logging.Formatter; // NOCS 
import java.util.logging.Level; // NOCS 
import java.util.logging.LogRecord; // NOCS 

/**
 * A formatter for the logging to the console which is used by the Kieker tools.
 * It simplifies the log messages and prints only the important information.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class SimpleConsoleLoggingFormatter extends Formatter {

	protected static final String LINE_SEPERATOR = System.getProperty("line.separator");

	/**
	 * Empty constructor.
	 */
	public SimpleConsoleLoggingFormatter() {
		// No code necessary
	}

	@Override
	public String format(final LogRecord record) {
		final StringBuilder sb = new StringBuilder();

		this.fillStringBuilderWithMessage(sb, record);

		return sb.toString();
	}

	/**
	 * Populates the string builder with logging information.
	 * 
	 * @param sb
	 *            the string builder
	 * @param record
	 *            one log record to be used for population of the string builder
	 */
	protected void fillStringBuilderWithMessage(final StringBuilder sb, final LogRecord record) {
		if ((record.getLevel() == Level.WARNING) || (record.getLevel() == Level.SEVERE)) {
			sb.append(record.getLevel().getLocalizedName());
			sb.append(": ");
		}

		sb.append(record.getMessage());
		sb.append(LINE_SEPERATOR);
	}

}
