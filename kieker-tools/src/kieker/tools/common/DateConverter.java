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
package kieker.tools.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.beust.jcommander.converters.BaseConverter;

/**
 * Converts parameter string representing a date value to timestamp.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class DateConverter extends BaseConverter<Long> {

	/** The format pattern which is used to print the date. */
	public static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";

	/** Date format pattern used for information. */
	public static final String HUMAN_READABLE_DATE_FORMAT = DATE_FORMAT_PATTERN.replaceAll("'", "") + " | timestamp"; // only for usage info

	/** date format provider. */
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);

	static {
		DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * Create a date converter.
	 *
	 * @param optionName
	 *            option name
	 */
	public DateConverter(final String optionName) {
		super(optionName);
	}

	@Override
	public Long convert(final String value) {
		long result;
		try {
			result = Long.parseLong(value);
		} catch (final NumberFormatException ex) {
			try {
				final Date ignoreBeforeDate = DATE_FORMAT.parse(value); // NOPMD concurrent access not possible
				result = ignoreBeforeDate.getTime() * (1000 * 1000);
			} catch (final java.text.ParseException ex2) {
				return null;
			}
		}
		return result;
	}

	@Override
	protected String getErrorString(final String value, final String to) {
		return "\"" + this.getOptionName() + "\": couldn't convert \"" + value + "\" to timestamp; use yyyyMMdd-HHmmss or timestamp integer";
	}

}
