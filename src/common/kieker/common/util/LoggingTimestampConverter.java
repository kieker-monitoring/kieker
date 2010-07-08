package kieker.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */
/**
 *
 * @author Andre van Hoorn
 */
public class LoggingTimestampConverter {

    private static final Log log = LogFactory.getLog(LoggingTimestampConverter.class);
    private static final String dateFormatPattern = "EEE, d MMM yyyy HH:mm:ss Z";

    public static final String convertLoggingTimestampToUTCString(final long loggingTimestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
        DateFormat m_ISO8601UTC = new SimpleDateFormat(dateFormatPattern);
        m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        return m_ISO8601UTC.format(c.getTime()) + " (UTC)";
    }

    public static final String convertLoggingTimestampLocalTimeZoneString(final long loggingTimestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
        DateFormat m_ISO8601UTC = new SimpleDateFormat(dateFormatPattern);
        return m_ISO8601UTC.format(c.getTime()) + " (local time)";
    }
}
