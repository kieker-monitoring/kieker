package kieker.tpmon.asyncFsWriter;

import java.util.concurrent.BlockingQueue;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import kieker.tpmon.KiekerExecutionRecord;
import kieker.tpmon.TpmonController;
import kieker.tpmon.annotations.TpmonInternal;
import kieker.tpmon.Worker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.asyncFsWriter.AsyncFsWriterWorker
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
public class AsyncFsWriterWorker implements Runnable, Worker {

    private static final Log log = LogFactory.getLog(AsyncFsWriterWorker.class);
    // configuration parameters
    private static final int maxEntriesInFile = 22000;
    private static final long pollingIntervallInMillisecs = 400L;
    // internal variables
    private BlockingQueue writeQueue = null;
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
    @TpmonInternal
    public synchronized void initShutdown() {
        AsyncFsWriterWorker.shutdown = true;
    }
    
//    private boolean statementChanged = true;
//    private String nextStatementText;

    public AsyncFsWriterWorker(BlockingQueue writeQueue, String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
        this.writeQueue = writeQueue;
        log.info("New Tpmon - FsWriter thread created ");
    }

    @TpmonInternal
    public void run() {
        log.info("FsWriter thread running");
        try {
            while (!finished) {
                Object data = writeQueue.poll(pollingIntervallInMillisecs, TimeUnit.MILLISECONDS);
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
        } finally{
            this.finished = true;
        }
    }

    @TpmonInternal
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
    @TpmonInternal
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
                log.error("Will disable monitoring!");
                TpmonController.getInstance().disableMonitoring();
                throw ex;
            }
        }
    }

    /**
     * Note that it's not necessary to synchronize this method since 
     * a file is written at most by one thread.
     * 
     * @param data
     * @throws java.io.IOException
     */
    @TpmonInternal
    private void writeDataNow(KiekerExecutionRecord execData) throws IOException {
        prepareFile(); // may throw FileNotFoundException
        pos.println(execData.toKiekerCSVRecord());
        pos.flush();
    }

    @TpmonInternal
    public boolean isFinished() {
        return finished;
    }
}
