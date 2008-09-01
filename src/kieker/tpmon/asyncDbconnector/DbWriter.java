/*
 * DbWriter.java
 *
 * Created on July 30, 2007, 9:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package kieker.tpmon.asyncDbconnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import kieker.tpmon.annotations.TpmonInternal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author matthias
 */
public class DbWriter  implements Runnable, Worker{
    private static final Log log = LogFactory.getLog(DbWriter.class);
    
    private static final long pollingIntervallInMillisecs = 400L;
    
    private Connection conn;
    private BlockingQueue writeQueue;
    private PreparedStatement psInsertMonitoringData;
    private static boolean shutdown = false;
    private boolean finished = false;
    
    boolean statementChanged = true;
    String nextStatementText;
    
    public DbWriter(Connection initializedConnection, BlockingQueue writeQueue, String statementtext) {
        this.conn = initializedConnection;
        this.writeQueue = writeQueue;
        this.nextStatementText = statementtext;
        log.info("New Tpmon - DbWriter thread created");
    }
        
     @TpmonInternal()
    synchronized void changeStatement(String statementtext){
        nextStatementText = statementtext;
        statementChanged = true;
    }
    
    /**
     * May be called more often than required... but that doens't harm
     */
     @TpmonInternal()
    public void initShutdown() {
        DbWriter.shutdown = true;
    }           
    
      @TpmonInternal()
    public void run() {
        log.info("Dbwriter thread running");
        try {            
            while(!finished) { 
                Object data = writeQueue.poll(pollingIntervallInMillisecs, TimeUnit.MILLISECONDS); 
                if (data != null) {
                    consume(data); 
                } else {
                    // timeout ... 
                    if (shutdown && writeQueue.isEmpty()) {
                        finished = true;
                    }
                }
            }
            log.info("Dbwriter finished");
        } catch (InterruptedException ex) {
            System.out.println("ERROR: "+ex.getMessage());
        }
    }
    
    
    
    /**
     * writes next item into database
     */
       @TpmonInternal()
    private void  consume(Object traceidObject) {
        //if (TpmonController.debug) System.out.println("DbWriter "+this+" Consuming "+traceidObject);
        try {
            if (statementChanged || psInsertMonitoringData == null) {                
                psInsertMonitoringData = conn.prepareStatement(nextStatementText);
                statementChanged = false;
            }
            
            InsertData id = (InsertData) traceidObject;
            
            psInsertMonitoringData.setString(1,id.opname);
            psInsertMonitoringData.setString(2,id.sessionid);
            psInsertMonitoringData.setString(3,id.traceid);
            psInsertMonitoringData.setLong(4,id.tin);
            psInsertMonitoringData.setLong(5,id.tout);
            psInsertMonitoringData.setLong(6,id.executionOrderIndex);
            psInsertMonitoringData.setLong(7,id.executionStackSize);
            psInsertMonitoringData.execute();
            
            
        } catch (SQLException ex) {
            log.error("Tpmon DbWriter Error during database statement preparation: ", ex);
            ex.printStackTrace();
        }
    }
    
     @TpmonInternal()
    public boolean isFinished() {
        return finished;
    }
}
