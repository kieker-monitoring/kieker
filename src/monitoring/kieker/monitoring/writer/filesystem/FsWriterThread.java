package kieker.monitoring.writer.filesystem;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

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
    
    @Override
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
        FsWriterThread.log.info("New FsWriter thread created ");
    }
    static boolean passed = false;

    
    @Override
    public void run() {
        FsWriterThread.log.info("FsWriter thread running");
        try {
            while (!this.finished) {
                final IMonitoringRecord monitoringRecord = this.writeQueue.take();
                if (monitoringRecord == MonitoringController.END_OF_MONITORING_MARKER) {
                    FsWriterThread.log.info("Found END_OF_MONITORING_MARKER. Will terminate");
                    // need to put the marker back into the queue to notify other threads
                    this.writeQueue.add(MonitoringController.END_OF_MONITORING_MARKER);
                    this.finished = true;
                    break;
                }
                if (monitoringRecord != null) {
                    this.consume(monitoringRecord);
                    //System.out.println("FSW "+writeQueue.size());
                } else {
                    // timeout ... 
                    if (FsWriterThread.shutdown && this.writeQueue.isEmpty()) {
                        this.finished = true;
                    }
                }
            }
            FsWriterThread.log.info("FsWriter finished");
        } catch (final Exception ex) {
            // e.g. Interrupted Exception or IOException
            FsWriterThread.log.error("FS Writer will halt", ex);
            // TODO: This is a dirty hack!
            // What we need is a listener interface!
            FsWriterThread.log.error("Will terminate monitoring!");
            // TODO: fix?
            //MonitoringController.getInstance().terminate();
        } finally {
            this.finished = true;
        }
    }

    
    private void consume(final IMonitoringRecord monitoringRecord) throws Exception {
        // TODO: We should check whether this is necessary. 
        // This should only cover an initial action which can be 
        // moved before the while loop in run()
        if ((this.pos == null) || (this.filenameInitialized == false)) {
            this.prepareFile();
        }
        this.writeDataNow(monitoringRecord);
    }

    /**
     * Determines and sets a new Filename
     */
    
    private void prepareFile() throws FileNotFoundException {
        if ((this.entriesInCurrentFileCounter++ > FsWriterThread.maxEntriesInFile) || !this.filenameInitialized) {
            if (this.pos != null) {
                this.pos.close();
            }
            this.filenameInitialized = true;
            this.entriesInCurrentFileCounter = 0;

            final DateFormat m_ISO8601UTC =
                    new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
            m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            final String dateStr = m_ISO8601UTC.format(new java.util.Date());
            //int time = (int) (System.currentTimeMillis() - 1177404043379L);     // TODO: where does this number come from?
            //int random = (new Random()).nextInt(100);
            final String filename = this.filenamePrefix + "-" + dateStr + "-UTC-" + this.getName() + ".dat";

            //log.info("** " + java.util.Calendar.getInstance().currentTimeNanos().toString() + " new filename: " + filename);
            try {
                final FileOutputStream fos = new FileOutputStream(filename);
                final BufferedOutputStream bos = new BufferedOutputStream(fos);
                final DataOutputStream dos = new DataOutputStream(bos);
                this.pos = new PrintWriter(dos);
                this.pos.flush();
            } catch (final FileNotFoundException ex) {
                //log.fatal("Error creating the file: " + filename + " \n " + ex.getMessage());
                // TODO: this error should be signalled to the controller
                // e.g. using a listener (do not add a reference to MonitoringController!)
                // TODO: This is a dirty hack!
                // What we need is a listener interface!
                FsWriterThread.log.error("Will terminate monitoring!");
                // TODO: FIX?
                //MonitoringController.getInstance().terminate();
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
    private void writeDataNow(final IMonitoringRecord monitoringRecord) throws IOException {
        final Object[] recordFields = monitoringRecord.toArray();
        final int LAST_FIELD_INDEX = recordFields.length - 1;
        this.prepareFile(); // may throw FileNotFoundException

            this.pos.write("$"+this.mappingFileWriter.idForRecordTypeClass(monitoringRecord.getClass()));
            this.pos.write(';');
            this.pos.write(Long.toString(monitoringRecord.getLoggingTimestamp()));
            if (LAST_FIELD_INDEX > 0) {
                this.pos.write(';');
            }

        for (int i = 0; i <= LAST_FIELD_INDEX; i++) {
            final Object val = recordFields[i];
            // TODO: assert that val!=null and provide suitable log msg if null
            this.pos.write(val.toString());
            if (i < LAST_FIELD_INDEX) {
                this.pos.write(';');
            }
        }
        this.pos.println();
        this.pos.flush();
    }

    
    @Override
	public boolean isFinished() {
        return this.finished;
    }
}
