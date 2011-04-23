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
    //private static final Log log = LogFactory.getLog(LoggingTimestampConverter.class);

	private static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
    private static final String dateFormatPattern = "EEE, d MMM yyyy HH:mm:ss Z";

    /**
     * Converts a timestamp representing the number of nanoseconds since Jan 1,
     * 1970 UTC into a human-readable datetime string given in the UTC timezone.
     *
     * Note that no guarantees are made about the actual format.
     * Particularly, it is not guaranteed that the string can be reconverted
     * using the convertDatetimeStringToUTCLoggingTimestamp(..) method.
     *
     * @param loggingTimestamp
     * @return
     */
    public static final String convertLoggingTimestampToUTCString(final long loggingTimestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
        DateFormat m_ISO8601UTC = new SimpleDateFormat(dateFormatPattern);
        m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        return m_ISO8601UTC.format(c.getTime()) + " (UTC)";
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
     * @return
     */
    public static final String convertLoggingTimestampLocalTimeZoneString(final long loggingTimestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
        DateFormat m_ISO8601UTC = new SimpleDateFormat(dateFormatPattern);
        return m_ISO8601UTC.format(c.getTime()) + " (local time)";
    }

    /**
     * Converts a datetime string of format <i>yyyyMMdd-HHmmss</i> (UTC timezone)
     * into a timestamp representing the number of nanoseconds
     * since Jan 1, 1970 UTC.
     *
     * @param utcString
     * @return
     * @throws ParseException
     */
    public static final long convertDatetimeStringToUTCLoggingTimestamp(final String utcString)
            throws ParseException {
        final DateFormat m_ISO8601UTC = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        return m_ISO8601UTC.parse(utcString).getTime();
    }

    /**
     * Converts a datetime string of format <i>yyyyMMdd-HHmmss</i> (UTC timezone)
     * into a Date object.
     *
     * @param utcString
     * @return
     * @throws ParseException
     */
    public static final Date convertDatetimeStringToUTCDate(final String utcString)
            throws ParseException {
        final DateFormat m_ISO8601UTC = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        return (m_ISO8601UTC.parse(utcString));
    }
}
