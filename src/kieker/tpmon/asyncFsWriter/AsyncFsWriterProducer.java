package kieker.tpmon.asyncFsWriter;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.AbstractMonitoringDataWriter;
import kieker.tpmon.annotations.TpmonInternal;
import kieker.tpmon.asyncDbconnector.InsertData;

import kieker.tpmon.asyncDbconnector.Worker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author matthias
 */
public class AsyncFsWriterProducer extends AbstractMonitoringDataWriter {

    private static final Log log = LogFactory.getLog(AsyncFsWriterProducer.class);
    //configuration parameter
    final int numberOfFsWriters = 1; // one is usually sufficient and more usuable since only one file is created at once
    //internal variables
    private Vector<Worker> workers = new Vector<Worker>();

    @TpmonInternal
    public Vector<Worker> getWorkers() {
        return workers;
    }

    private BlockingQueue<InsertData> blockingQueue = null;

    private String filenamePrefix = null;
    
    public AsyncFsWriterProducer(String filenamePrefix){
        this.filenamePrefix = filenamePrefix;
        this.init();
    }

    @TpmonInternal
    public void init() {
        blockingQueue = new ArrayBlockingQueue<InsertData>(8000);
        for (int i = 0; i < numberOfFsWriters; i++) {
            AsyncFsWriterWorker dbw = new AsyncFsWriterWorker(blockingQueue, filenamePrefix);                        
            new Thread(dbw).start();                       
            workers.add(dbw);
        }
        //System.out.println(">Kieker-Tpmon: (" + numberOfFsWriters + " threads) will write to the file system");
        log.info(">Kieker-Tpmon: (" + numberOfFsWriters + " threads) will write to the file system");
    }

    /**
     * Use this method to insert data into the database.
     */
    @TpmonInternal
    public boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String traceid, long tin, long tout, int executionOrderIndex, int executionStackSize) {
        return this.insertMonitoringDataNow(experimentId, vmName, opname, "nosession", traceid, tin, tout, executionOrderIndex, executionStackSize);
    }

    /**
     * This method is not synchronized, in contrast to the insert method of the Dbconnector.java.
     * It uses several dbconnections in parallel using the consumer, producer pattern.
     *
     */
    @TpmonInternal
    public boolean insertMonitoringDataNow(int experimentId, String vmName, String opname, String sessionid, String traceid, long tin, long tout, int executionOrderIndex, int executionStackSize) {
        if (this.isDebug()) {
            log.info(">Kieker-Tpmon: AsyncFsWriterDispatcher.insertMonitoringDataNow");
        }

        try {
            InsertData id = new InsertData(experimentId, vmName, opname, sessionid, traceid, tin, tout, executionOrderIndex, executionStackSize);
            blockingQueue.add(id); // tries to add immediately!
        //System.out.println(""+blockingQueue.size());

        } catch (Exception ex) {
            log.error(">Kieker-Tpmon: " + System.currentTimeMillis() + " insertMonitoringData() failed: Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }
    
    @TpmonInternal()
    public String getFilenamePrefix() {
        return filenamePrefix;
    }
    
}
