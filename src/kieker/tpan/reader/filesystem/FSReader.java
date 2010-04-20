package kieker.tpan.reader.filesystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kieker.tpan.reader.AbstractMonitoringLogReader;
import kieker.tpan.consumer.IMonitoringRecordConsumer;
import kieker.tpan.reader.LogReaderExecutionException;
import kieker.tpan.consumer.MonitoringRecordConsumerExecutionException;
import kieker.common.record.DummyMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

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
 * ordered by the logging timestamp.
 *
 * @author Andre van Hoorn
 */
public class FSReader extends AbstractMonitoringLogReader {

    private static final Log log = LogFactory.getLog(FSReader.class);
    public static final String PROP_NAME_INPUTDIRS = "inputDirs"; // value: semicolon-separated list of directories
    private volatile String[] inputDirs = null;

    // TODO: provide constructor with time interval
    public FSReader(final String[] inputDirs) {
        this.inputDirs = inputDirs;
    }

    /** Default constructor used for construction by reflection. */
    public FSReader() {
    }
    /**
     * Acts as a consumer to the FSDirectoryReader and delegates incoming records
     * to ...
     */
    private FSReaderCons concurrentConsumer;

    private class FSReaderCons implements IMonitoringRecordConsumer {

        private final FSReader master;
        private final String[] inputDirs;
        // we synchronize access to the following data structures on this object.
        //private volatile Thread mainThread;
        private final AtomicBoolean errorOccured = new AtomicBoolean(false);
        private final AtomicBoolean isTerminated = new AtomicBoolean(false);
        private final Collection<Thread> readerThreads = new ArrayList<Thread>();
        //private final List<Thread> activeReaders = new ArrayList<Thread>();
        private final IMonitoringRecord FS_READER_TERMINATION_MARKER = new DummyMonitoringRecord();
        // guarded by recordListLock
        private final TreeMap<IMonitoringRecord, CountDownLatch> orderRecordBuffer = new TreeMap<IMonitoringRecord, CountDownLatch>(new Comparator<IMonitoringRecord>() {

            public int compare(IMonitoringRecord t, IMonitoringRecord t1) {
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

        public FSReaderCons(final FSReader master, final String[] inputDirs) {
            this.master = master;
            this.inputDirs = inputDirs;
        }

        public String[] getRecordTypeSubscriptionList() {
            return null;
        }

        /** 
         * Threads executing this method (concurrently) put record into sorted
         * buffer, notify the buffer consumer and block until they are granted
         * to read the next record.
         */
        public void consumeMonitoringRecord(IMonitoringRecord monitoringRecord) throws MonitoringRecordConsumerExecutionException {
            if (this.isTerminated.get()) {
                throw new MonitoringRecordConsumerExecutionException("Consumer already terminated");
            }

            try {
                final CountDownLatch myLatch = new CountDownLatch(1);
                synchronized (this.orderRecordBuffer) {
                    this.orderRecordBuffer.put(monitoringRecord, myLatch);
                    this.orderRecordBuffer.notifyAll(); // notify main thread of new record
                }
                if (monitoringRecord != FS_READER_TERMINATION_MARKER) {
                    myLatch.await(); // countDown called by main thread
                }
            } catch (InterruptedException ex) {
                log.error("Reader thread has been interrupted.", ex);
                this.errorOccured.set(true);
            }
        }

        public boolean execute() throws MonitoringRecordConsumerExecutionException {
            try {
                { // 1. init and start reader threads
                    for (int i = 0; i < inputDirs.length; i++) {
                        final FSDirectoryReader r = new FSDirectoryReader(this.inputDirs[i]);
                        r.addConsumer(this, null); // consume records of any type and pass to this
                        final Thread t = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    r.execute();
                                    consumeMonitoringRecord(FS_READER_TERMINATION_MARKER); // signal termination
                                } catch (Exception ex) {
                                    log.error(r, ex);
                                    reportReaderException(ex);
                                }
                            }
                        }, "Reader thread for " + this.inputDirs[i]);
                        this.readerThreads.add(t);
                        t.start();
                    }
                }

                // 2. now process all records provided by the threads in the right order
                while (true) {
                    // Does not work with Java 1.5: Map.Entry<AbstractMonitoringRecord, Thread> firstEntry;
                    IMonitoringRecord nextRecord;
                    CountDownLatch consumerLatch = null;
                    synchronized (orderRecordBuffer) {
                        while (this.orderRecordBuffer.size() < this.readerThreads.size()) { // always there must be one record from each thread
                            orderRecordBuffer.wait();
                            if (this.errorOccured.get()) {
                                log.error("Found error flag set");
                                throw new LogReaderExecutionException("An error occured");
                            }
                        }

                        nextRecord = this.orderRecordBuffer.firstKey(); // do not poll since FS_READER_TERMINATION_MARKER remains in list
                        //1.5 un-compatibility: firstEntry = this.nextRecordsFromReaders.firstEntry(); // do not poll since FS_READER_TERMINATION_MARKER remains in list
                        if (nextRecord == FS_READER_TERMINATION_MARKER) {
                            log.info("All reader threads provided FS_READER_TERMINATION_MARKER");
                            return true; // we're done
                        } else {
                            consumerLatch = this.orderRecordBuffer.get(nextRecord);
                            if (this.orderRecordBuffer.remove(nextRecord) == null) { // now, well remove
                                log.warn("failed to remove nextRecord " + nextRecord);
                            }
                            this.master.deliverRecordToConsumers(nextRecord);
                        }
                    } // release monitor
                    if (consumerLatch != null) { // wake up blocked thread (in consume...())
                        consumerLatch.countDown();
                    } else {
                        log.warn("consumerLatch == null");
                    }
                }
            } catch (Exception ex) {
                log.error("Exception while reading. Terminating.", ex);
                this.errorOccured.set(true);
                throw new MonitoringRecordConsumerExecutionException("Error while reading. Terminating.", ex);
            }
        }

        private synchronized void reportReaderException(Exception ex) {
            Thread t = Thread.currentThread();
            log.error("FSReader thread '" + t.getName() + "' reports exception", ex);
            this.errorOccured.set(true);
            this.notifyAll();
        }

        public void terminate(final boolean error) {
            log.info("terminate method called");
            if (error) {
                this.errorOccured.set(true);
            }
            synchronized (this.orderRecordBuffer) {
                this.orderRecordBuffer.put(FS_READER_TERMINATION_MARKER, null);
                this.orderRecordBuffer.notifyAll(); // notify main thread of new record
            }
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    @Override
    public boolean execute() throws LogReaderExecutionException {
        concurrentConsumer = new FSReaderCons(this, inputDirs);
        boolean success = false;
        try {
            success = concurrentConsumer.execute();
        } catch (MonitoringRecordConsumerExecutionException ex) {
            log.error("RecordConsumerExecutionException occured", ex);
            throw new LogReaderExecutionException("RecordConsumerExecutionException occured", ex);
        } finally {
            log.info("Initiating shutdown");
            concurrentConsumer.terminate(success);
        }
        return success;
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
