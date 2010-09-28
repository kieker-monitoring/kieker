package kieker.analysis.reader.filesystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.common.record.DummyMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;
import kieker.common.util.PropertyMap;
import kieker.analysis.plugin.MonitoringRecordConsumerException;

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
    private String[] inputDirs = null;

    public FSReader(final String[] inputDirs) {
        this(inputDirs, null);
    }

    private final Collection<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType;

    /**
     *
     * @param inputDirs
     * @param readOnlyRecordsOfType select only records of this type; null selects all
     */
    public FSReader(final String[] inputDirs, final Collection<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType) {
        this.inputDirs = inputDirs;
        this.readOnlyRecordsOfType = readOnlyRecordsOfType;
    }

    /** Default constructor used for construction by reflection. */
    public FSReader() {
        this.readOnlyRecordsOfType = null; // final member wants to be initialized
    }
    /**
     * Acts as a consumer to the FSDirectoryReader and delegates incoming records
     * to ...
     */
    private FSReaderCons concurrentConsumer;

    private class FSReaderCons implements IMonitoringRecordReceiver {

        private final FSReader master;
        private final String[] inputDirs;
        // we synchronize access to the following data structures on this object.
        //private volatile Thread mainThread;
        private final AtomicBoolean errorOccured = new AtomicBoolean(false);
        private final AtomicBoolean isTerminated = new AtomicBoolean(false);
        private final Collection<Thread> readerThreads = new ArrayList<Thread>();
        //private final List<Thread> activeReaders = new ArrayList<Thread>();
        private final IMonitoringRecord FS_READER_TERMINATION_MARKER = new DummyMonitoringRecord();

        private final AtomicLong nextOrderRecordBufferElementId =
                new AtomicLong(0);
        private final class OrderRecordBufferElement {
            private final long elementId = nextOrderRecordBufferElementId.getAndIncrement();

            private final IMonitoringRecord record;

            /**
             * OrderRecordBufferElements must not be created by calling the default constructor
             */
            @SuppressWarnings("unused")
			private OrderRecordBufferElement() {
                this.record = null;
            }

            public OrderRecordBufferElement (IMonitoringRecord record){
                this.record = record;
            }

            public long getElementId() {
                return this.elementId;
            }

            public IMonitoringRecord getRecord() {
                return this.record;
            }
        }

        private final TreeMap<OrderRecordBufferElement, CountDownLatch> orderRecordBuffer = new TreeMap<OrderRecordBufferElement, CountDownLatch>(new Comparator<OrderRecordBufferElement>() {

            @Override
            public int compare(OrderRecordBufferElement e, OrderRecordBufferElement e1) {
                IMonitoringRecord t = e.getRecord();
                IMonitoringRecord t1 = e1.getRecord();

                if (t == FS_READER_TERMINATION_MARKER) {
                    return 1;
                }
                if (t1 == FS_READER_TERMINATION_MARKER) {
                    return -1;
                }
                /* Only return 0 (equal) iff the objects are identical, i.e., the same */
                if (t == t1) {
                    return 0;
                }

                if (t.getLoggingTimestamp() == t1.getLoggingTimestamp()){
                    /* Here, two records have an equal timestamp but they are not
                     * the same and thus, we must not return 0!
                     * We use the id of the wrapping element to order these two
                     * elements in a deterministic way. */
                    //log.info("Elements have equal timestamp; ordering by wrapper id.");
                    return (e.getElementId() < e1.getElementId()) ? -1 : 1;
                }

                /* In every case, the timestamps are different at this place! */
                return t.getLoggingTimestamp() < t1.getLoggingTimestamp() ? -1 : 1;
            }
        });

        public FSReaderCons(final FSReader master, final String[] inputDirs) {
            this.master = master;
            this.inputDirs = inputDirs;
        }

        /** 
         * Threads executing this method (concurrently) put record into sorted
         * buffer, notify the buffer consumer and block until they are granted
         * to read the next record.
         */
        @Override
        public boolean newMonitoringRecord(IMonitoringRecord monitoringRecord) {
            if (this.isTerminated.get()) {
                log.error("Consumer already terminated");
                return false;
            }

            try {
                final CountDownLatch myLatch = new CountDownLatch(1);
                synchronized (this.orderRecordBuffer) {
                    this.orderRecordBuffer.put(new OrderRecordBufferElement(monitoringRecord), myLatch);
                    this.orderRecordBuffer.notifyAll(); // notify main thread of new record
                }
                if (monitoringRecord != FS_READER_TERMINATION_MARKER) {
                    myLatch.await(); // countDown called by main thread
                }
            } catch (InterruptedException ex) {
                log.error("Reader thread has been interrupted.", ex);
                this.errorOccured.set(true);
                return false;
            }
            return true;
        }

        public boolean execute() throws MonitoringRecordConsumerException {
            try {
                { // 1. init and start reader threads
                    for (int i = 0; i < inputDirs.length; i++) {
                        final FSDirectoryReader r = new FSDirectoryReader(this.inputDirs[i], readOnlyRecordsOfType);
                        r.addRecordReceiver(this); // consume records of any type and pass to this
                        final Thread t = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    r.read();
                                    newMonitoringRecord(FS_READER_TERMINATION_MARKER); // signal termination
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
                                throw new MonitoringLogReaderException("An error occured");
                            }
                        }

                        OrderRecordBufferElement nextBufferElement = this.orderRecordBuffer.firstKey(); // do not poll since FS_READER_TERMINATION_MARKER remains in list
                        nextRecord = nextBufferElement.getRecord();
                        //1.5 un-compatibility: firstEntry = this.nextRecordsFromReaders.firstEntry(); // do not poll since FS_READER_TERMINATION_MARKER remains in list
                        if (nextRecord == FS_READER_TERMINATION_MARKER) {
                            log.info("All reader threads provided FS_READER_TERMINATION_MARKER");
                            return true; // we're done
                        } else {
                            consumerLatch = this.orderRecordBuffer.get(nextBufferElement);
                            if (this.orderRecordBuffer.remove(nextBufferElement) == null) { // now, we'll remove
                                log.warn("failed to remove nextRecord " + nextRecord + "\n" +
                                        "consumerLatch: " + consumerLatch +"\n" +
                                        "first key: " + this.orderRecordBuffer.firstKey());
                                throw new MonitoringRecordConsumerException("failed to remove nextRecord " + nextRecord);
                            }
                            this.master.deliverRecord(nextRecord);
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
                throw new MonitoringRecordConsumerException("Error while reading. Terminating.", ex);
            }
        }

        private void reportReaderException(Exception ex) {
            Thread t = Thread.currentThread();
            log.error("FSReader thread '" + t.getName() + "' reports exception", ex);
            this.errorOccured.set(true);
            synchronized (this.orderRecordBuffer) {
                this.orderRecordBuffer.notifyAll(); // notify main thread of new record
            }
        }

        public void terminate(final boolean error) {
            if (error) {
                this.errorOccured.set(true);
            }
            synchronized (this.orderRecordBuffer) {
                this.orderRecordBuffer.put(new OrderRecordBufferElement(FS_READER_TERMINATION_MARKER), null);
                this.orderRecordBuffer.notifyAll(); // notify main thread of new record
            }
        }
    }

    @Override
    public boolean read() throws MonitoringLogReaderException {
        concurrentConsumer = new FSReaderCons(this, inputDirs);
        boolean success = false;
        try {
            success = concurrentConsumer.execute();
        } catch (MonitoringRecordConsumerException ex) {
            log.error("RecordConsumerExecutionException occured", ex);
            throw new MonitoringLogReaderException("RecordConsumerExecutionException occured", ex);
        } finally {
            concurrentConsumer.terminate(success);
        }
        return success;
    }

    /**
     * @param initString List of input directories separated by semicolon
     */
    public void init(String initString) throws IllegalArgumentException {
        PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws IllegalArgumentException
        String dirList = propertyMap.getProperty(PROP_NAME_INPUTDIRS);

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
