package kieker.common.logReader.filesystemReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.common.logReader.AbstractKiekerMonitoringLogReader;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;

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
 *
 * This reader allows one to read a folder or an single tpmon file and
 * transforms it to monitoring events that are stored in the file system
 * again, written to a database, or whatever tpmon is configured to do
 * with the monitoring data.
 */
/**
 * Filesystem reader which reads from multiple directories simultaneously
 * while ordering the records by the logging timestamp.
 *
 * @author Andre van Hoorn
 */
public class FSMergeReader extends AbstractKiekerMonitoringLogReader {

    private static final Log log = LogFactory.getLog(FSMergeReader.class);
    private String[] inputDirs;

    public FSMergeReader(final String[] inputDirs) {
        this.inputDirs = inputDirs;
    }

    /**
     * Acts as a consumer to the FSReader and delegates incoming records
     * to ...
     */
    private class FSReaderCons implements IKiekerRecordConsumer {

        private final FSMergeReader master;
        private List<FSReader> readers;
        private List<FSReader> activeReaders;
        private ConcurrentSkipListMap<AbstractKiekerMonitoringRecord, Thread> nextRecordsFromReaders = new ConcurrentSkipListMap<AbstractKiekerMonitoringRecord, Thread>(new Comparator<AbstractKiekerMonitoringRecord>() {

            public int compare(AbstractKiekerMonitoringRecord t, AbstractKiekerMonitoringRecord t1) {
                if (t == t1) {
                    return 0;
                }
                if (t == TpmonController.END_OF_MONITORING_MARKER) {
                    return 1;
                }
                if (t1 == TpmonController.END_OF_MONITORING_MARKER) {
                    return -1;
                }
                if (t.getLoggingTimestamp() < t1.getLoggingTimestamp()) {
                    return -1;
                }
                if (t.getLoggingTimestamp() > t1.getLoggingTimestamp()) {
                    return 1;
                }
                return 0;
            }
        });

        public FSReaderCons(final FSMergeReader master, final List<FSReader> readers) {
            this.master = master;
            this.readers = readers;
            this.activeReaders = new ArrayList<FSReader>(readers);
        }

        public String[] getRecordTypeSubscriptionList() {
            return null;
        }

        public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
            Thread t = Thread.currentThread();
            this.nextRecordsFromReaders.put(monitoringRecord, t);
            try {
                if (this.nextRecordsFromReaders.size() == this.readers.size()) {
                    this.notify();
                }
                // TODO: handle last record (which aborts reader)
                Thread.currentThread().wait();
            } catch (InterruptedException ex) {
                log.error("Reader has been interrupted. Terminating consumer. ", ex);
                this.terminate();
            }
        }

        public boolean execute() throws RecordConsumerExecutionException {
            // TODO: start reader Threads

            while (readers.size() > 0) {
                try {
                    this.wait();
                    // TODO: fetch reader and notify
                    this.master.deliverRecordToConsumers(this.nextRecordsFromReaders.firstKey());
                } catch (InterruptedException ex) {
                    log.error("Was interrupted", ex);
                } catch (LogReaderExecutionException ex) {
                    log.info("LogReaderExecutionException", ex);
                    throw new RecordConsumerExecutionException("LogReaderExecutionException", ex);
                }
            }

            /* do nothing */
            return true;
        }

        public void terminate() {
            this.master.terminate();
        }
    }

    @Override
    public boolean execute() throws LogReaderExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param initString List of input directories separated by semicolon
     */
    public void init(String initString) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
