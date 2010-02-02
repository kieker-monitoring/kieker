package kieker.common.logReader.filesystemReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        private final String[] inputDirs;
        private final List<Thread> readerThreads = new ArrayList<Thread>();
        private final List<Thread> activeReaders = new ArrayList<Thread>();
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

        public FSReaderCons(final FSMergeReader master, final String[] inputDirs) {
            this.master = master;
            this.inputDirs = inputDirs;
            ThreadGroup tg = new ThreadGroup("FS reader threads");
            for (int i = 0; i < inputDirs.length; i++) {
                final FSReader r = new FSReader(this.inputDirs[i]);
                final Thread t = new Thread(tg, new Runnable() {

                    public void run() {
                        try {
                            r.execute();
                        } catch (LogReaderExecutionException ex) {
                            log.error(r, ex);
                        }
                    }
                });
                this.readerThreads.add(t);
                this.activeReaders.add(t);
            }
        }

        public String[] getRecordTypeSubscriptionList() {
            return null;
        }

        public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
            Thread t = Thread.currentThread();
            this.nextRecordsFromReaders.put(monitoringRecord, t);
            try {
                /* as soon as all readers provided a record, notify consumers */
                // TODO: remaining problem: what if reader provides no record?
                synchronized (t) {
                    if (this.nextRecordsFromReaders.size() == this.activeReaders.size()) {
                        this.notify();
                    } // TODO: thread-safe?
                    t.wait();
                }
            } catch (InterruptedException ex) {
                log.error("Reader has been interrupted. Terminating consumer. ", ex);
                this.terminate();
            }
        }

        public boolean execute() throws RecordConsumerExecutionException {
            // TODO: start reader Threads

            while (activeReaders.size() > 0) {
                try {
                    this.wait();
                    Map.Entry<AbstractKiekerMonitoringRecord, Thread> k = this.nextRecordsFromReaders.pollFirstEntry();
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
            Thread t = Thread.currentThread();
            if (this.readerThreads.contains(t)) {
                this.activeReaders.remove(t);
                this.notifyAll();
            } else {
                if (this.activeReaders.size() == 0) {
                    this.master.terminate();
                }
            }
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
