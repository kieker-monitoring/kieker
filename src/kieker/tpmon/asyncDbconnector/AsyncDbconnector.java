
/**
 * tpmon.Dbconnector.java
 *
 * Stores monitoring data into a database.
 *
 *
 * Warning !
 * This class is an academic prototype and not intended
 * for reliability or availability critical systems.
 *
 * The insertMonitoringData methods are thread-save (also in combination with experimentId changes),
 * so that they may be used by multiple threads at the same time. We have tested this in various
 * applications, in combination with the standard mysql-Jconnector database driver.
 *
 *
 * Our experience shows that it is not a major bottleneck if not too many
 * measurement points are used (e.g., 30/second). However, there are much
 * performance tuning possible in this class. For instance, performance
 * optimization should be possible by using a connection pool
 * instead of a single database connection. The current version uses
 * prepared statements. Alternatively, it could be tuned by collecting
 * multiple database commands before sending it to the database.
 *
 *
 * @author Matthias Rohr
 *
 * History (Build) (change the String BUILD when this file is changed):
 * 2008/05/29: Changed vmid to vmname (defaults to hostname), 
 *             which may be changed during runtime
 * 2007/07/30: Initial Prototype
 *
 */





package kieker.tpmon.asyncDbconnector;


import java.sql.*;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.TpmonController;

public class AsyncDbconnector {
    
    
    static private Connection conn = null;
    
    static private boolean init = false;
    static private BlockingQueue blockingQueue;

    
    
    public AsyncDbconnector() {
   
    }
    
    
    static  Vector<DbWriter> writers= new Vector<DbWriter>();
    
    /**
     *
     * Returns false if an error occurs. Errors are printed to stdout (e.g., App-server logfiles), even if debug = false.
     *
     */
    public static synchronized boolean init() {
        if (!init) {
            if (TpmonController.debug) System.out.println("Tpmon asyncDbconnector init -- vmidhashcode:"+TpmonController.getVmname());
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (Exception ex) {
                TpmonController.formatAndOutputError("MySQL driver registration failed. Perhaps the mysql-connector-....jar missing? Exception: "+ex.getMessage()
                ,false,false);
                ex.printStackTrace();
                return false;
            }
            
            try {
                conn = DriverManager.getConnection(TpmonController.dbConnectionAddress);
                
                int numberOfConnections = 4;
                
                blockingQueue = new ArrayBlockingQueue<String>(8000);
                
//                DbWriter dbw = new DbWriter(DriverManager.getConnection(TpmonController.dbConnectionAddress),blockingQueue);
//                 new Thread(dbw).start();  
                 String preparedQuery = "INSERT INTO "+TpmonController.dbTableName+
                        " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)" +
                        "VALUES ("+experimentId+",?,?,?,?,?,"+vmname+",?,?)";
                for (int i=0 ; i < numberOfConnections; i++) {
                    DbWriter dbw = new DbWriter(DriverManager.getConnection(TpmonController.dbConnectionAddress),blockingQueue, preparedQuery);
                    writers.add(dbw);
                    new Thread(dbw).start();  
                    TpmonController.registerWorker(dbw);
                }
                System.out.println("Tpmon ("+numberOfConnections+" threads) connected to database at "+TpmonController.getDateString());                               
                init = true;
       
                
                if (TpmonController.setInitialExperimentIdBasedOnLastId) {
                    // set initial experiment id based on last id (increased by 1)
                    Statement stm = conn.createStatement();
                    ResultSet res = stm.executeQuery("SELECT max(experimentID) FROM "+TpmonController.dbTableName);
                    if (res.next())
                        experimentId = res.getInt(1)+1;
                    TpmonController.setExperimentId(experimentId);
                    System.out.println(" set initial experiment id based on last id (="+(experimentId-1)+" + 1 = "+ experimentId+")");
                }
                
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
                return false;
            }
        }
        return true;
    }
    
    private static int experimentId = TpmonController.getExperimentId();
    private static String vmname = TpmonController.getVmname();
    
    /**
     * Use this method to insert data into the database.
     */
    
    public static boolean insertMonitoringDataNow(String opname, String traceid, long tin, long tout,int executionOrderIndex, int executionStackSize) {
        return insertMonitoringDataNow(opname,"nosession",traceid,tin,tout,executionOrderIndex, executionStackSize);
    }
    
    
    /**
     * This method is not synchronized, in contrast to the insert method of the Dbconnector.java.
     * It uses several dbconnections in parallel using the consumer, producer pattern.
     *
     */
    public static boolean insertMonitoringDataNow(String opname, String sessionid, String traceid, long tin, long tout,int executionOrderIndex, int executionStackSize) {
       if (TpmonController.debug)  System.out.println("Async.insertMonitoringDataNow");
        
        if (init == false) {
            init();
            
                
            if (init ==false) {
                System.out.println("Error: Theres something wrong with the database connection of tpmon!"+
                        "- Database Connection Could Not Be Initiated");
                return false;
            }
        }
       
        try {
            // INSERT INTO `newSchema` ( `experimentid` , `operation` , `traceid` , `tin` , `tout` ) VALUES ( '0', '1231', '1231', '12312', '1221233' );
            if ( experimentId != TpmonController.getExperimentId() || !vmname.equals(TpmonController.getVmname())) { // ExperimentId and vmname may be changed
                experimentId = TpmonController.getExperimentId();     
                vmname = TpmonController.getVmname();
                String preparedQuery = "INSERT INTO "+TpmonController.dbTableName+
                        " (experimentid,operation,sessionid,traceid,tin,tout,vmname,executionOrderIndex,executionStackSize)" +
                        "VALUES ("+experimentId+",?,?,?,?,?,"+vmname+",?,?)";
                for(DbWriter wr : writers){
                    wr.changeStatement(preparedQuery);
                }
            }
            
            InsertData id = new InsertData(opname,  sessionid,  traceid,  tin,  tout, executionOrderIndex, executionStackSize);
            
            blockingQueue.put(id);
            //System.out.println("Queue is "+blockingQueue.size());
            
        } catch (InterruptedException ex) {
            System.out.println(""+System.currentTimeMillis()+" insertMonitoringData() failed: SQLException: " + ex.getMessage());
            return false;
        }
        return true;
    }
}


