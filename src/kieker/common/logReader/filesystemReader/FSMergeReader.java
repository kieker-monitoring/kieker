package kieker.common.logReader.filesystemReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
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
import kieker.tpmon.monitoringRecord.KiekerDummyMonitoringRecord;

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
 * Filesystem reader which reads from multiple directories simultaneously
 * while ordering the records by the logging timestamp.
 *
 * @author Andre van Hoorn
 */
public class FSMergeReader extends AbstractKiekerMonitoringLogReader {

    private static final Log log = LogFactory.getLog(FSMergeReader.class);
    private static final String PROP_NAME_INPUTDIRS = "inputDirs"; // value: semicolon-separated list of directories
    private String[] inputDirs;
    private boolean terminate = false;

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    // TODO: provide constructor with time interval
    public FSMergeReader(final String[] inputDirs) {
        this.inputDirs = inputDirs;
    }
    /**
     * Acts as a consumer to the FSReader and delegates incoming records
     * to ...
     */
    private FSReaderCons concurrentConsumer;

    private class FSReaderCons implements IKiekerRecordConsumer {

        private final FSMergeReader master;
        private final String[] inputDirs;
        // we synchronize access to the following data structures on this object.
        private final List<Thread> readerThreads = new ArrayList<Thread>();
        private final List<Thread> activeReaders = new ArrayList<Thread>();
        private final AbstractKiekerMonitoringRecord FS_READER_TERMINATION_MARKER = new KiekerDummyMonitoringRecord();
        private TreeMap<AbstractKiekerMonitoringRecord, Thread> nextRecordsFromReaders = new TreeMap<AbstractKiekerMonitoringRecord, Thread>(new Comparator<AbstractKiekerMonitoringRecord>() {

            public int compare(AbstractKiekerMonitoringRecord t, AbstractKiekerMonitoringRecord t1) {
                if (t == t1) {
                    return 0;
                }
                if (t == FS_READER_TERMINATION_MARKER) {
                    return 1;
                }
                if (t1 == FS_READER_TERMINATION_MARKER) {
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
        }

        public String[] getRecordTypeSubscriptionList() {
            return null;
        }

        /** Note that this method is accessed concurrently! */
        public void consumeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) throws RecordConsumerExecutionException {
            Thread t = Thread.currentThread();
            this.nextRecordsFromReaders.put(monitoringRecord, t);
            synchronized (this) {
                if (this.nextRecordsFromReaders.size() == this.activeReaders.size()) {
                    this.notify(); // wake up master thread waiting in execute()
                }
                if (monitoringRecord == FS_READER_TERMINATION_MARKER) {
                    /* if it is the FS_READER_TERMINATION_MARKER, the method
                     * call originates from the terminate method (called by one
                     * the reader threads) and we must not block! */
                } else {
                    synchronized (t) {
                        try {
                            t.wait();
                        } catch (InterruptedException ex) {
                            log.error("Reader thread has been interrupted. Terminating consumer. ", ex);
                            this.terminate();
                        }
                    }
                }

            }
        }

        public boolean execute() throws RecordConsumerExecutionException {
            // init and start reader threads
            ThreadGroup tg = new ThreadGroup("FS reader threads");
            for (int i = 0; i < inputDirs.length; i++) {
                final FSReader r = new FSReader(this.inputDirs[i]);
                r.addConsumer(this, null); // consume records of any type and pass to this
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
            for (Thread t : this.readerThreads) {
                t.start();
            }

            /* caution: from now on, we have a multi-threaded reader
             *          and need to synchronize (on 'this'). */

            synchronized (this) {
                while (activeReaders.size() > 0) {
                    try {
                        this.wait();
                        Map.Entry<AbstractKiekerMonitoringRecord, Thread> k =
                                this.nextRecordsFromReaders.pollFirstEntry();
                        this.master.deliverRecordToConsumers(k.getKey());
                        k.getValue().notify(); // wake up blocked thread (in consume...())
                    } catch (InterruptedException ex) {
                        log.error("Was interrupted", ex);
                    } catch (LogReaderExecutionException ex) {
                        log.info("LogReaderExecutionException", ex);
                        throw new RecordConsumerExecutionException("LogReaderExecutionException", ex);
                    }
                }
                this.terminate();
            }
            return true;
        }

        /** Note that this method is accessed concurrently! */
        public void terminate() {
            Thread t = Thread.currentThread();
            synchronized (this) {
                if (this.readerThreads.contains(t)) { // i.e., a reader thread called terminate()
                    this.activeReaders.remove(t);
                    try {
                        this.consumeMonitoringRecord(FS_READER_TERMINATION_MARKER);
                    } catch (RecordConsumerExecutionException ex) {
                        log.error("Error occured while sending FS_READER_TERMINATION_MARKER", ex);
                    }
                } else {
                    if (this.activeReaders.size() == 0) {
                        this.master.setTerminate(terminate);
                        this.master.terminate();
                    }
                }
            }
        }
    }

    @Override
    public boolean execute() throws LogReaderExecutionException {
        concurrentConsumer = new FSReaderCons(this, inputDirs);
        synchronized (this) {
            try {
                concurrentConsumer.execute();
                this.wait(); // will be notified by terminate() method in super class
            } catch (InterruptedException ex) {
                Logger.getLogger(FSMergeReader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RecordConsumerExecutionException ex) {
                log.error("RecordConsumerExecutionException occured", ex);
                throw new LogReaderExecutionException("RecordConsumerExecutionException occured", ex);
            }
        }

        return true;
    }

    /**
     * @param initString List of input directories separated by semicolon
     */
    public void init(String initString) throws IllegalArgumentException {
        super.initVarsFromInitString(initString);
        String dirList = super.getInitProperty(PROP_NAME_INPUTDIRS);
        if (dirList == null) {
            log.error("Missing value for property " + PROP_NAME_INPUTDIRS);
            throw new IllegalArgumentException("Missing value for property " + PROP_NAME_INPUTDIRS);
        }
        // parse inputDir property value
        try {
            StringTokenizer dirNameTokenizer = new StringTokenizer(dirList, ";");
            this.inputDirs = new String[dirNameTokenizer.countTokens()];
            for (int i = 0; dirNameTokenizer.hasMoreTokens(); i++) {
                this.inputDirs[i] = dirNameTokenizer.nextToken().trim();
            }
        } catch (Exception exc) {
            throw new IllegalArgumentException("Error parsing list of input directories'" + dirList + "'", exc);
        }
    }
}
