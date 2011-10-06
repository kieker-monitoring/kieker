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

package kieker.tools.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 
 * @author Andre van Hoorn
 */
public class LoggingTimestampConverter {
	// private static final Log log = LogFactory.getLog(LoggingTimestampConverter.class);

	private static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
	private static final String DATE_FORMAT_PATTERN2 = "EEE, d MMM yyyy HH:mm:ss Z";

	/**
	 * Converts a timestamp representing the number of nanoseconds since Jan 1,
	 * 1970 UTC into a human-readable datetime string given in the UTC timezone.
	 * 
	 * Note that no guarantees are made about the actual format.
	 * Particularly, it is not guaranteed that the string can be reconverted
	 * using the convertDatetimeStringToUTCLoggingTimestamp(..) method.
	 * 
	 * @param loggingTimestamp
	 * @return a human-readable datetime string (UTC timezone) which represents the passed timestamp
	 */
	public static final String convertLoggingTimestampToUTCString(final long loggingTimestamp) {
		final GregorianCalendar c = new GregorianCalendar();
		c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
		final DateFormat dateFormat_ISO8601UTC = new SimpleDateFormat(LoggingTimestampConverter.DATE_FORMAT_PATTERN2); //NOCS
		dateFormat_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat_ISO8601UTC.format(c.getTime()) + " (UTC)";
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
	 * @return a human-readable datetime string (local timezone) which represents the passed timestamp
	 */
	public static final String convertLoggingTimestampLocalTimeZoneString(final long loggingTimestamp) {
		final GregorianCalendar c = new GregorianCalendar();
		c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
		final DateFormat dateFormat_ISO8601LOCAL = new SimpleDateFormat(LoggingTimestampConverter.DATE_FORMAT_PATTERN2); //NOCS
		return dateFormat_ISO8601LOCAL.format(c.getTime()) + " (local time)";
	}

	/**
	 * Converts a datetime string of format <i>yyyyMMdd-HHmmss</i> (UTC timezone)
	 * into a timestamp representing the number of nanoseconds
	 * since Jan 1, 1970 UTC.
	 * 
	 * @param utcString
	 * @return a timestamp which represents the passed time
	 * @throws ParseException
	 */
	public static final long convertDatetimeStringToUTCLoggingTimestamp(final String utcString) throws ParseException {
		final DateFormat dateFormat_ISO8601UTC = new SimpleDateFormat(LoggingTimestampConverter.DATE_FORMAT_PATTERN); //NOCS
		dateFormat_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC")); 
		return dateFormat_ISO8601UTC.parse(utcString).getTime();
	}

	/**
	 * Converts a datetime string of format <i>yyyyMMdd-HHmmss</i> (UTC timezone)
	 * into a Date object.
	 * 
	 * @param utcString
	 * @return a Date object which represents the passed time
	 * @throws ParseException
	 */
	public static final Date convertDatetimeStringToUTCDate(final String utcString) throws ParseException {
		final DateFormat dateFormat_ISO8601UTC = new SimpleDateFormat(LoggingTimestampConverter.DATE_FORMAT_PATTERN);
		dateFormat_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC")); //NOCS
		return dateFormat_ISO8601UTC.parse(utcString);
	}
}
