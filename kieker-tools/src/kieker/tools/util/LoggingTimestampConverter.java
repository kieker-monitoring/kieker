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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This is a utility class which can be used to convert timestamps, for example by transforming a simple timestamp into a human-readable datetime string.
 *
 * @author Andre van Hoorn
 *
 * @since 1.2
 */
public final class LoggingTimestampConverter {
	private static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
	private static final String DATE_FORMAT_PATTERN2 = "EEE, d MMM yyyy HH:mm:ss.SSS Z";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private LoggingTimestampConverter() {}

	/**
	 * Converts a timestamp representing the number of nanoseconds since Jan 1,
	 * 1970 UTC into a human-readable datetime string given in the UTC timezone.
	 *
	 * Note that no guarantees are made about the actual format.
	 * Particularly, it is not guaranteed that the string can be reconverted
	 * using the convertDatetimeStringToUTCLoggingTimestamp(..) method.
	 *
	 * @param loggingTimestamp
	 *            The timestamp to be converted in nanoseconds.
	 * @return a human-readable datetime string (UTC timezone) which represents the passed timestamp
	 */
	public static final String convertLoggingTimestampToUTCString(final long loggingTimestamp) {
		final Calendar c = new GregorianCalendar();
		c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
		final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN2, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(c.getTime()) + " (UTC)";
	}

	/**
	 * Converts a timestamp representing the number of nanoseconds since Jan 1,
	 * 1970 UTC into a human-readable datetime string given in the local timezone.
	 *
	 * Note that no guarantees are made about the actual format.
	 * Particularly, it is not guaranteed that the string can be reconverted
	 * using the convertDatetimeStringToUTCLoggingTimestamp(..) method.
	 *
	 * @param loggingTimestamp
	 *            The timestamp to be converted.
	 * @return a human-readable datetime string (local timezone) which represents the passed timestamp
	 */
	public static final String convertLoggingTimestampLocalTimeZoneString(final long loggingTimestamp) {
		final Calendar c = new GregorianCalendar();
		c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
		final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN2, Locale.US);
		return dateFormat.format(c.getTime()) + " (local time)";
	}

	/**
	 * Converts a datetime string of format <i>yyyyMMdd-HHmmss</i> (UTC timezone)
	 * into a timestamp representing the number of nanoseconds
	 * since Jan 1, 1970 UTC.
	 *
	 * @param utcString
	 *            The string to be converted.
	 * @return a timestamp which represents the passed time
	 * @throws ParseException
	 *             If the specified string cannot be parsed.
	 */
	public static final long convertDatetimeStringToUTCLoggingTimestamp(final String utcString) throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.parse(utcString).getTime();
	}

	/**
	 * Converts a datetime string of format <i>yyyyMMdd-HHmmss</i> (UTC timezone)
	 * into a Date object.
	 *
	 * @param utcString
	 *            The string to be converted.
	 * @return a Date object which represents the passed time
	 * @throws ParseException
	 *             If the specified string cannot be parsed.
	 */
	public static final Date convertDatetimeStringToUTCDate(final String utcString) throws ParseException {
		final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.parse(utcString);
	}
}
