/*
 * 2008/05/29: Changed vmid to vmname (defaults to hostname), 
 *             which may be changed during runtime
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.tpmon.asyncFsWriter;

import java.util.concurrent.BlockingQueue;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import kieker.tpmon.KiekerExecutionRecord;
import kieker.tpmon.TpmonController;
import kieker.tpmon.annotations.TpmonInternal;
import kieker.tpmon.asyncDbconnector.Worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author matthias
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
     * It is okay that is may be called multiple times for the same class
     */
    @TpmonInternal
    public synchronized void initShutdown() {
        AsyncFsWriterWorker.shutdown = true;
    }
    boolean statementChanged = true;
    String nextStatementText;

    public AsyncFsWriterWorker(BlockingQueue writeQueue, String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
        this.writeQueue = writeQueue;
        log.info("New Tpmon - FsWriter thread created ");
    }

    @TpmonInternal
    public void run() {
        log.info("FsWriter thread running");
        //System.out.println("FsWriter thread running");
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

            int time = (int) (System.currentTimeMillis() - 1177404043379L);
            int random = (int) (Math.random() * 100d);
            String filename = this.filenamePrefix + time + "-" + random + ".dat";
            //System.out.println("this.filenamePrefix:"+this.filenamePrefix);
            //System.out.println(""+filename);

            log.info("** " + java.util.Calendar.getInstance().getTime().toString() + " new filename: " + filename);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(filename);
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

