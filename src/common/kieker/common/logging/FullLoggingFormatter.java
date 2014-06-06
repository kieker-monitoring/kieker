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

package kieker.common.logging;

import java.util.logging.LogRecord;

/**
 * A formatter for the logging which is used by the Kieker tools. It prints the important information, as well as a stack trace.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class FullLoggingFormatter extends SimpleLoggingFormatter {

	@Override
	protected void fillStringBuilderWithMessage(final StringBuilder sb, final LogRecord record) {
		super.fillStringBuilderWithMessage(sb, record);
		this.fillStringBuilderWithStackTrace(sb, record);
	}

	private void fillStringBuilderWithStackTrace(final StringBuilder sb, final LogRecord record) {
		final Throwable thrown = record.getThrown();
		if (thrown != null) {
			sb.append(thrown.getLocalizedMessage()).append(LINE_SEPERATOR);
			final StackTraceElement[] stackTrace = thrown.getStackTrace();
			for (final StackTraceElement stackTraceElement : stackTrace) {
				sb.append("\t").append(stackTraceElement).append(LINE_SEPERATOR);
			}
		}
	}

}
