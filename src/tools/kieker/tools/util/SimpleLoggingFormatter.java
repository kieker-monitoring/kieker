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

package kieker.tools.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A formatter for the logging which is used by the Kieker tools. It simplifies the log messages and prints only the important information.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class SimpleLoggingFormatter extends Formatter {

	private static final String LINE_SEPERATOR = System.getProperty("line.separator");

	@Override
	public String format(final LogRecord record) {
		final Date date = new Date(record.getMillis());
		final DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		final String dateText = dateFormat.format(date);

		final StringBuilder sb = new StringBuilder();

		sb.append(record.getLevel().getLocalizedName());
		sb.append(" (").append(dateText).append("): ");
		sb.append(record.getMessage());
		sb.append(LINE_SEPERATOR);
		sb.append(LINE_SEPERATOR);

		return sb.toString();
	}

}
