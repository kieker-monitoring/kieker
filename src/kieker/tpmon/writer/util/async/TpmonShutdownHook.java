package kieker.tpmon.writer.util.async;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.*;
import java.util.Vector;

//import kieker.tpmon.AbstractWorkerThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * kieker.tpmon.TpmonShutdownHook
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
 * /

/**
 * This class ensures that virtual machine shutdown (e.g., cause by a
 * System.exit(int)) is delayed until all monitoring data is written.
 * This is important for the asynchronous writers for the files system
 * and database, since these store data with a small delay and data would
 * be lost when System.exit is not delayed.
 * 
 * When the system shutdown is initiated, the termination of the Virtual Machine
 * is delayed until all registered worker queues are empty.
 * 
 * @author Matthias Rohr
 * 
 * History: 
 * 2008/01/04: Refactoring for the first release of 
 *             Kieker and publication under an open source licence
 * 2007/12/16: Initial Prototype
 */
public class TpmonShutdownHook extends Thread {

    private static final Log log = LogFactory.getLog(TpmonShutdownHook.class);
    
    public TpmonShutdownHook() {    
    }
        
    private Vector<AbstractWorkerThread> workers = new Vector<AbstractWorkerThread>();
    
    /**
     * Registers a worker, so that it is notified when system shutdown is initialted and to 
     * allow the TpmonShutdownHook to wait till the worker is finished.
     * @param newWorker
     */
    
    public void registerWorker(AbstractWorkerThread newWorker){
        workers.add(newWorker);
    }
    
    
    public void run(){ // is called when VM shutdown (e.g., strg+c) is initiated or when system.exit is called
        try {
            // is called when VM shutdown (e.g., strg+c) is initiated or when system.exit is called
            log.info("Tpmon: TpmonShutdownHook notifies all workers to initiate shutdown");
            initateShutdownForAllWorkers();            
            while(!allWorkersFinished()) {
                Thread.sleep(500);
                log.info("Tpmon: Shutdown delayed - At least one worker is busy ... waiting additional 0.5 seconds");
            }           
            log.info("Tpmon: TpmonShutdownHook can terminate since all workers are finished");
        } catch (InterruptedException ex) {
            log.error("Tpmon: Interrupted Exception occured", ex);
        }
    }
    
    
    public synchronized void initateShutdownForAllWorkers(){
        for (AbstractWorkerThread wrk: workers) {
            if (wrk != null) wrk.initShutdown();
        }
        TpmonController.getInstance().terminate();
    }
    
    
    public synchronized boolean allWorkersFinished(){
        for (AbstractWorkerThread wrk: workers) {
            if (wrk != null && wrk.isFinished() == false) 
                return false; // at least one busy worker exists
        }
        return true;
    }    
}
