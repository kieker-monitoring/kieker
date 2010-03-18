package kieker.common.logReader.filesystemReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import kieker.common.logReader.AbstractKiekerMonitoringLogReader;
import kieker.common.logReader.IKiekerRecordConsumer;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.common.logReader.RecordConsumerExecutionException;
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
 * TOOD: This reader should, as soon as proven to be stable, become the FSReader.
 *
 * Filesystem reader which reads from multiple directories simultaneously
 * while ordering the records by the logging timestamp.
 *
 * @author Andre van Hoorn
 */
public class FSMergeReader extends AbstractKiekerMonitoringLogReader {

    private static final Log log = LogFactory.getLog(FSMergeReader.class);
    public static final String PROP_NAME_INPUTDIRS = "inputDirs"; // value: semicolon-separated list of directories
    private String[] inputDirs = null;
    private boolean terminate = false;

    public void setTerminate(boolean terminate) {
        this.terminate = terminate;
    }

    // TODO: provide constructor with time interval
    public FSMergeReader(final String[] inputDirs) {
        this.inputDirs = inputDirs;
    }

    public FSMergeReader() {
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
        private Thread mainThread;
        private boolean errorOccured = false;
        private boolean isTerminated = false;
        private final List<Thread> readerThreads = new ArrayList<Thread>();
        private final List<Thread> activeReaders = new ArrayList<Thread>();
        private final AbstractKiekerMonitoringRecord FS_READER_TERMINATION_MARKER = new KiekerDummyMonitoringRecord();
        private TreeMap<AbstractKiekerMonitoringRecord, Thread> nextRecordsFromReaders = new TreeMap<AbstractKiekerMonitoringRecord, Thread>(new Comparator<AbstractKiekerMonitoringRecord>() {

            public int compare(AbstractKiekerMonitoringRecord t, AbstractKiekerMonitoringRecord t1) {
                if (t == FS_READER_TERMINATION_MARKER) {
                    return 1;
                }
                if (t1 == FS_READER_TERMINATION_MARKER) {
                    return -1;
                }
                /* Only return 0 (equal) iff the objects are identical */
                if (t == t1) {
                    return 0;
                }
                if (t.getLoggingTimestamp() < t1.getLoggingTimestamp()) {
                    return -1;
                } else {
                    return 1;
                }
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
            if (this.isTerminated) {
                throw new RecordConsumerExecutionException("Consumer already terminated");
            }

            Thread t = Thread.currentThread();
            //log.info("Putting record " + monitoringRecord);
            synchronized (this) {
                this.nextRecordsFromReaders.put(monitoringRecord, t);
                //log.info("NextrecordList" + this.nextRecordsFromReaders);
            }
            if (monitoringRecord == FS_READER_TERMINATION_MARKER) {
                /* if it is the FS_READER_TERMINATION_MARKER, the method
                 * call originates from the terminate method (called by one
                 * the reader threads) and we must not block! */
                synchronized (t) { // TODO: required to sync on t?
                    //log.info("Notifying master");
                    synchronized (this) {
                        this.notify(); // notify master in execute(that there's a new record)
                    }
                }
            } else {
                synchronized (t) {
                    try {
                        synchronized (this) {
                            this.notify(); // notify master in execute(that there's a new record)
                        }
                        t.wait();
                        //log.info("Woke up");
                    } catch (InterruptedException ex) {
                        log.error("Reader thread has been interrupted. Terminating consumer. ", ex);
                        this.terminate();
                    }
                }
            }
        }

        public boolean execute() throws RecordConsumerExecutionException {
            this.mainThread = Thread.currentThread();
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
                            reportReaderException(ex);
                        }
                    }
                }, "Reader thread for "+this.inputDirs[i]);
                this.readerThreads.add(t);
                this.activeReaders.add(t);
            }
            for (Thread t : this.readerThreads) {
                t.start();
            }

            //log.info("All threads started. Proceeding to main loop ...");

            /* caution: from now on, we have a multi-threaded reader
             *          and need to synchronize (on 'this'). */
            while (true) {
                try {
                    // Does not work with Java 1.5:
                    //Map.Entry<AbstractKiekerMonitoringRecord, Thread> firstEntry;
                    AbstractKiekerMonitoringRecord lowestKey;
                    Thread recordProvidingThread = null;
                    synchronized (this) {
                        while (this.nextRecordsFromReaders.size() != this.readerThreads.size()) {
                            this.wait();
                            if (this.errorOccured) {
                                log.error("Found error flag set");
                                throw new LogReaderExecutionException("An error occured");
                            }
                        }
                        lowestKey = this.nextRecordsFromReaders.firstKey(); // do not poll since FS_READER_TERMINATION_MARKER remains in list
                        //1.5 un-compatibility: firstEntry = this.nextRecordsFromReaders.firstEntry(); // do not poll since FS_READER_TERMINATION_MARKER remains in list
                        if (lowestKey == FS_READER_TERMINATION_MARKER) {
                            log.info("All reader threads provided FS_READER_TERMINATION_MARKER");
                            this.terminate();
                            return true;
                        } else {
                            recordProvidingThread = this.nextRecordsFromReaders.get(lowestKey);
                            this.nextRecordsFromReaders.remove(lowestKey); // now, well remove
                            //1.5 un-compatibility: this.nextRecordsFromReaders.pollFirstEntry(); // now, well remove
                            this.master.deliverRecordToConsumers(lowestKey);
                        }
                    } // release monitor
                    if (recordProvidingThread != null) { // only if we need to wake up s.o.
                        synchronized (recordProvidingThread) {
                            recordProvidingThread.notify(); // wake up blocked thread (in consume...())
                        }
                    }
                } catch (Exception ex) {
                    log.error("Exception while reading. Terminating.", ex);
                    this.terminate();
                    throw new RecordConsumerExecutionException("Error while reading. Terminating.", ex);
                }
            }
        }

        public synchronized void reportReaderException(LogReaderExecutionException ex) {
            Thread t = Thread.currentThread();
            log.error("FSReader thread '" + t.getName() + "' reports exception", ex);
            this.errorOccured = true;
            this.notifyAll();
        }

        /** Note that this method is accessed concurrently! */
        public synchronized void terminate() {
            if (this.isTerminated) {
                return;
            }
            Thread t = Thread.currentThread();
            if (this.readerThreads.contains(t)) { // i.e., a reader thread called terminate()
                //log.info("Removing myself from the list of active readers");
                this.activeReaders.remove(t);
                try {
                    this.consumeMonitoringRecord(FS_READER_TERMINATION_MARKER);
                } catch (RecordConsumerExecutionException ex) {
                    log.error("Error occured while sending FS_READER_TERMINATION_MARKER", ex);
                }
            } else if (t == this.mainThread) {
                //log.info("Main thread initiating reader shutdown");
                this.master.setTerminate(terminate);
                this.master.terminate();
                this.isTerminated = true;
                this.notifyAll();
            }
        }
    }

    @Override
    public boolean execute() throws LogReaderExecutionException {
        concurrentConsumer = new FSReaderCons(this, inputDirs);
        synchronized (this) {
            try {
                return concurrentConsumer.execute();
            } catch (RecordConsumerExecutionException ex) {
                log.error("RecordConsumerExecutionException occured", ex);
                throw new LogReaderExecutionException("RecordConsumerExecutionException occured", ex);
            }
        }
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
        } // parse inputDir property value
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
