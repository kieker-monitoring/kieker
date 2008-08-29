package kieker.tpmon.asyncFsWriter;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.TpmonController;
import kieker.tpmon.aspects.TpmonInternal;
import kieker.tpmon.asyncDbconnector.InsertData;

/**
 *
 * @author matthias
 */
public class AsyncFsWriterProducer {
      //configuration parameter
      final static int numberOfFsWriters = 1; // one is usually sufficient and more usuable since only one file is created at once
    
      //internal variables
      private static Vector<AsyncFsWriterWorker> workers= new Vector<AsyncFsWriterWorker>();
      private static BlockingQueue<InsertData> blockingQueue  = null;
      private static boolean init = false;
      
      @TpmonInternal
      public static synchronized boolean init() {
            if (!init) {
                blockingQueue = new ArrayBlockingQueue<InsertData>(8000);
                for (int i=0 ; i < numberOfFsWriters; i++) {
                        AsyncFsWriterWorker dbw = new AsyncFsWriterWorker(blockingQueue);
                        workers.add(dbw);
                        new Thread(dbw).start();       
                        TpmonController.registerWorker(dbw);
                    }
                init = true;
                System.out.println(">Kieker-Tpmon: ("+numberOfFsWriters+" threads) will write to the file system");   
            }            
            return init;
        }
        
         /**
     * Use this method to insert data into the database.
     */
    @TpmonInternal
    public static boolean insertMonitoringDataNow(String opname, String traceid, long tin, long tout,int executionOrderIndex, int executionStackSize) {
        return insertMonitoringDataNow(opname,"nosession",traceid,tin,tout,executionOrderIndex,executionStackSize);
    }
    
    /**
     * This method is not synchronized, in contrast to the insert method of the Dbconnector.java.
     * It uses several dbconnections in parallel using the consumer, producer pattern.
     *
     */
    @TpmonInternal
    public static boolean insertMonitoringDataNow(String opname, String sessionid, String traceid, long tin, long tout, int executionOrderIndex, int executionStackSize) {
       if (TpmonController.debug)  System.out.println(">Kieker-Tpmon: AsyncFsWriterDispatcher.insertMonitoringDataNow");
        
        if (init == false) {
            init();
                            
            if (init ==false) {
                System.out.println(">Kieker-Tpmon: Error: Theres something wrong with the file system writer of tpmon!"+
                        "- It could not be initialized");
                return false;
            }
        }
       
        try {                      
            InsertData id = new InsertData(opname,  sessionid,  traceid,  tin,  tout, executionOrderIndex, executionStackSize);            
            blockingQueue.put(id);
            //System.out.println(""+blockingQueue.size());
            
        } catch (InterruptedException ex) {
            System.out.println(">Kieker-Tpmon: "+System.currentTimeMillis()+" insertMonitoringData() failed: Exception: " + ex.getMessage());
            return false;
        }
        return true;
    }        
}
