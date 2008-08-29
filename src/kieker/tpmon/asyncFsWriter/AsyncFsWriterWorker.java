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
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
import kieker.tpmon.aspects.TpmonInternal;
import kieker.tpmon.asyncDbconnector.InsertData;
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
    private String filenamePrefix = "";
    private boolean filenameInitialized = false;
    private int entriesInCurrentFileCounter = 0;
    private PrintWriter pos = null;
    private InsertData id = null;
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

    public AsyncFsWriterWorker(BlockingQueue writeQueue) {

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
        } catch (InterruptedException ex) {
            log.error(ex);
        }
    }

    @TpmonInternal
    private void consume(Object traceidObject) {
        //if (TpmonController.debug) System.out.println("FsWriter "+this+" Consuming "+traceidObject);
//        try {
        if (pos == null || filenameInitialized == false) {
            prepareFile();
        }

        id = (InsertData) traceidObject;
        writeDataNow(id.experimentId + ";" + id.opname + ";" + id.sessionid + ";" + id.traceid + ";" + id.tin + ";" + id.tout + ";" + id.vmName + ";" + id.executionOrderIndex + ";" + id.executionStackSize);

//            
//        } catch (Exception ex) {
//            System.out.println("Tpmon FsWriter Erro: "+ex.getMessage());
//            ex.printStackTrace();
//        }
    }

    /**
     * Determines and sets a new Filename
     */
    @TpmonInternal
    private void prepareFile() {
        if (entriesInCurrentFileCounter++ > maxEntriesInFile || !filenameInitialized) {
            if (pos != null) {
                //dos.flush();
                try {
                    pos.close();
                } catch (Exception ex) {
                }
            }
            filenameInitialized = true;
            entriesInCurrentFileCounter = 0;

            int time = (int) (System.currentTimeMillis() - 1177404043379L);
            int random = (int) (Math.random() * 100d);
            String filename = this.filenamePrefix + time + "-" + random + ".dat";

            log.info("** " + java.util.Calendar.getInstance().getTime().toString() + " new filename: " + filename);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(filename);
                BufferedOutputStream bos = new BufferedOutputStream(fos);

                DataOutputStream dos = new DataOutputStream(bos);
                pos = new PrintWriter(dos);
                pos.flush();
            } catch (FileNotFoundException ex) {
                System.out.println(">Kieker-Tpmon: Error creating the file: " + filename + " \n " + ex.getMessage());
                ex.printStackTrace();
            }

        }
    }

    @TpmonInternal
    public boolean insertMonitoringDataNow(int experimentId, String vmName, String componentname, String methodname, String sessionID, String requestID, long tin, long tout, int executionOrderIndex) {
        writeDataNow(experimentId + "," + componentname + methodname + "," + sessionID + "," + requestID + "," + tin + "," + tout + "," + vmName + "," + executionOrderIndex + "," + id.executionStackSize);
        return true;
    }

    @TpmonInternal
    private void writeDataNow(String data) {
        prepareFile();
        pos.println(data);
        pos.flush();
    }

    @TpmonInternal
    public boolean isFinished() {
        return finished;
    }
}

