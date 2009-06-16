package kieker.tpmon.writer.filesystemAsync;

import java.util.concurrent.BlockingQueue;
import java.io.*;
import java.util.Random;
import kieker.tpmon.monitoringRecord.KiekerExecutionRecord;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.writer.core.AbstractWorkerThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.asyncFsWriter.AsyncFsWriterWorkerThread
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
 * @author Matthias Rohr
 * 
 * History:
 * 2008/05/29: Changed vmid to vmname (defaults to hostname), 
 *             which may be changed during runtime
 */
public class AsyncFsWriterWorkerThread extends AbstractWorkerThread {

    private static final Log log = LogFactory.getLog(AsyncFsWriterWorkerThread.class);
    // configuration parameters
    private static final int maxEntriesInFile = 22000;
    private static final long pollingIntervallInMillisecs = 400L;
    // internal variables
    private BlockingQueue<KiekerExecutionRecord> writeQueue = null;
    private String filenamePrefix = null;
    private boolean filenameInitialized = false;
    private int entriesInCurrentFileCounter = 0;
    private PrintWriter pos = null;
    private KiekerExecutionRecord execData = null;
    private boolean finished = false;
    private static boolean shutdown = false;

    /**
     * It is okay that it may be called multiple times for the same class
     */
    @TpmonInternal()
    public synchronized void initShutdown() {
        AsyncFsWriterWorkerThread.shutdown = true;
    }
    
//    private boolean statementChanged = true;
//    private String nextStatementText;

    public AsyncFsWriterWorkerThread(BlockingQueue<KiekerExecutionRecord> writeQueue, String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
        this.writeQueue = writeQueue;
        log.info("New Tpmon - FsWriter thread created ");
    }

    static boolean passed = false;

    @TpmonInternal()
    public void run() {
        log.info("FsWriter thread running");
        try {
            while (!finished) {
                Object data = writeQueue.take();
                if (data == TpmonController.END_OF_MONITORING_MARKER){
                    log.info("Found END_OF_MONITORING_MARKER. Will terminate");
                    // need to put the marker back into the queue to notify other threads
                    writeQueue.add(TpmonController.END_OF_MONITORING_MARKER);
                    finished = true;
                    break;
                }
                if (data != null) {
                    consume(data);
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
            TpmonController.getInstance().terminateMonitoring();
        } finally{
            this.finished = true;
        }
    }

    @TpmonInternal()
    private void consume(Object traceidObject) throws Exception {
        // TODO: We should check whether this is necessary. 
        // This should only cover an initial action which can be 
        // moved before the while loop in run()
        if (pos == null || filenameInitialized == false) {
            prepareFile();
        }
        execData = (KiekerExecutionRecord) traceidObject;
        writeDataNow(execData);
    }

    /**
     * Determines and sets a new Filename
     */
    @TpmonInternal()
    private void prepareFile() throws FileNotFoundException {
        if (entriesInCurrentFileCounter++ > maxEntriesInFile || !filenameInitialized) {
            if (pos != null) {
                pos.close();
            }
            filenameInitialized = true;
            entriesInCurrentFileCounter = 0;

            int time = (int) (System.currentTimeMillis() - 1177404043379L);     // TODO: where does this number come from?
            int random = (new Random()).nextInt(100);
            String filename = this.filenamePrefix + time + "-" + random + ".dat";

            log.info("** " + java.util.Calendar.getInstance().getTime().toString() + " new filename: " + filename);
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DataOutputStream dos = new DataOutputStream(bos);
                pos = new PrintWriter(dos);
                pos.flush();
            } catch (FileNotFoundException ex) {
                log.fatal(">Kieker-Tpmon: Error creating the file: " + filename + " \n " + ex.getMessage());
                // TODO: this error should be signalled to the controller
                // e.g. using a listener (do not add a reference to TpmonController!)
                // TODO: This is a dirty hack!
                // What we need is a listener interface!
                log.error("Will terminate monitoring!");
                TpmonController.getInstance().terminateMonitoring();
                throw ex;
            }
        }
    }

    /**
     * Note that it's not necessary to synchronize this method since 
     * a file is written at most by one thread.
     * @throws java.io.IOException
     */
    @TpmonInternal()
    private void writeDataNow(KiekerExecutionRecord execData) throws IOException {
        prepareFile(); // may throw FileNotFoundException
        pos.println(execData.toKiekerCSVRecord());
        pos.flush();
    }

    @TpmonInternal()
    public boolean isFinished() {
        return finished;
    }
}
