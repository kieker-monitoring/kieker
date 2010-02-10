package kieker.tpan;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.IKiekerMonitoringLogReader;

import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Andre van Hoorn
 */
public class TpanInstance {

    private static final Log log = LogFactory.getLog(TpanInstance.class);
    private IKiekerMonitoringLogReader logReader;
    private final Vector<IKiekerRecordConsumer> consumers = new Vector<IKiekerRecordConsumer>();

    public void run() throws LogReaderExecutionException, RecordConsumerExecutionException {
        for (IKiekerRecordConsumer c : this.consumers) {
            this.logReader.addConsumer(c, c.getRecordTypeSubscriptionList());
            c.execute();
        }
        try {
            if (!this.logReader.execute()) {
                log.error("Calling execute() on logReader returned false");
                throw new LogReaderExecutionException("Calling execute() on logReader returned false");
            }
        } catch (LogReaderExecutionException exc) {
            log.fatal("LogReaderException! Will terminate consumers.");
            for (IKiekerRecordConsumer c : this.consumers) {
                c.terminate();
            }
            throw exc;
        }
    }

    public void setLogReader(IKiekerMonitoringLogReader reader) {
        this.logReader = reader;
    }

    public void addRecordConsumer(IKiekerRecordConsumer consumer) {
        this.consumers.add(consumer);
    }

    public static final String convertLoggingTimestampToUTCString(final long loggingTimestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
        DateFormat m_ISO8601UTC = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
        m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        return m_ISO8601UTC.format(c.getTime()) + " UTC";
    }

    public static final String convertLoggingTimestampLocalTimeZoneString(final long loggingTimestamp) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(loggingTimestamp / ((long) 1000 * 1000));
        DateFormat m_ISO8601UTC = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
        return m_ISO8601UTC.format(c.getTime()) + " UTC";
    }
}
