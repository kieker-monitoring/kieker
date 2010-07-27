package kieker.monitoring.writer.filesystem;

import java.util.concurrent.BlockingQueue;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.MonitoringController;

import kieker.monitoring.writer.util.async.AbstractWorkerThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/** @author Matthias Rohr
 * 
 * History:
 * 2008/05/29: Changed vmid to vmname (defaults to hostname), 
 *             which may be changed during runtime
 */
public final class FsWriterThread extends AbstractWorkerThread {

    private static final Log log = LogFactory.getLog(FsWriterThread.class);
    // configuration parameters
    private static final int maxEntriesInFile = 22000;
    // internal variables
    private BlockingQueue<IMonitoringRecord> writeQueue = null;
    private String filenamePrefix = null;
    private boolean filenameInitialized = false;
    private int entriesInCurrentFileCounter = 0;
    private PrintWriter pos = null;
    private volatile boolean finished = false;
    private volatile static boolean shutdown = false;
    private final MappingFileWriter mappingFileWriter;

    /**
     * It is okay that it may be called multiple times for the same class
     */
    
    public synchronized void initShutdown() {
        FsWriterThread.shutdown = true;
    }

//    private boolean statementChanged = true;
//    private String nextStatementText;
    public FsWriterThread(final BlockingQueue<IMonitoringRecord> writeQueue,
            final MappingFileWriter mappingFileWriter,
            final String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
        this.writeQueue = writeQueue;
        this.mappingFileWriter = mappingFileWriter;
        log.info("New FsWriter thread created ");
    }
    static boolean passed = false;

    
    @Override
    public void run() {
        log.info("FsWriter thread running");
        try {
            while (!finished) {
                IMonitoringRecord monitoringRecord = writeQueue.take();
                if (monitoringRecord == MonitoringController.END_OF_MONITORING_MARKER) {
                    log.info("Found END_OF_MONITORING_MARKER. Will terminate");
                    // need to put the marker back into the queue to notify other threads
                    writeQueue.add(MonitoringController.END_OF_MONITORING_MARKER);
                    finished = true;
                    break;
                }
                if (monitoringRecord != null) {
                    consume(monitoringRecord);
                    //System.out.println("FSW "+writeQueue.size());
                } else {
                    // timeout ... 
                    if (shutdown && writeQueue.isEmpty()) {
                        finished = true;
                    }
                }
            }
            log.info("FsWriter finished");
        } catch (Exception ex) {
            // e.g. Interrupted Exception or IOException
            log.error("FS Writer will halt", ex);
            // TODO: This is a dirty hack!
            // What we need is a listener interface!
            log.error("Will terminate monitoring!");
            MonitoringController.getInstance().terminate();
        } finally {
            this.finished = true;
        }
    }

    
    private void consume(IMonitoringRecord monitoringRecord) throws Exception {
        // TODO: We should check whether this is necessary. 
        // This should only cover an initial action which can be 
        // moved before the while loop in run()
        if (pos == null || filenameInitialized == false) {
            prepareFile();
        }
        writeDataNow(monitoringRecord);
    }

    /**
     * Determines and sets a new Filename
     */
    
    private void prepareFile() throws FileNotFoundException {
        if (entriesInCurrentFileCounter++ > maxEntriesInFile || !filenameInitialized) {
            if (pos != null) {
                pos.close();
            }
            filenameInitialized = true;
            entriesInCurrentFileCounter = 0;

            DateFormat m_ISO8601UTC =
                    new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
            m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateStr = m_ISO8601UTC.format(new java.util.Date());
            //int time = (int) (System.currentTimeMillis() - 1177404043379L);     // TODO: where does this number come from?
            //int random = (new Random()).nextInt(100);
            String filename = this.filenamePrefix + "-" + dateStr + "-UTC-" + this.getName() + ".dat";

            //log.info("** " + java.util.Calendar.getInstance().currentTimeNanos().toString() + " new filename: " + filename);
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DataOutputStream dos = new DataOutputStream(bos);
                pos = new PrintWriter(dos);
                pos.flush();
            } catch (FileNotFoundException ex) {
                //log.fatal("Error creating the file: " + filename + " \n " + ex.getMessage());
                // TODO: this error should be signalled to the controller
                // e.g. using a listener (do not add a reference to MonitoringController!)
                // TODO: This is a dirty hack!
                // What we need is a listener interface!
                log.error("Will terminate monitoring!");
                MonitoringController.getInstance().terminate();
                throw ex;
            }
        }
    }

    // TODO: keep track of record type ID mapping!
    /**
     * Note that it's not necessary to synchronize this method since 
     * a file is written at most by one thread.
     * @throws java.io.IOException
     */
    private void writeDataNow(IMonitoringRecord monitoringRecord) throws IOException {
        Object[] recordFields = monitoringRecord.toArray();
        final int LAST_FIELD_INDEX = recordFields.length - 1;
        prepareFile(); // may throw FileNotFoundException

            pos.write("$"+this.mappingFileWriter.idForRecordTypeClass(monitoringRecord.getClass()));
            pos.write(';');
            pos.write(Long.toString(monitoringRecord.getLoggingTimestamp()));
            if (LAST_FIELD_INDEX > 0) {
                pos.write(';');
            }

        for (int i = 0; i <= LAST_FIELD_INDEX; i++) {
            Object val = recordFields[i];
            pos.write(val.toString());
            if (i < LAST_FIELD_INDEX) {
                pos.write(';');
            }
        }
        pos.println();
        pos.flush();
    }

    
    public boolean isFinished() {
        return finished;
    }
}
