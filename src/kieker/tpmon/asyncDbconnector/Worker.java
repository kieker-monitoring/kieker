/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon.asyncDbconnector;

import kieker.tpmon.annotations.TpmonInternal;

/**
 *
 * @author matthias
 */
public interface Worker {   
    
   /**
    * initShutdown has to be called before isFinished will result in true.
    * IsFinished == true means that the worker does not have additional jobs in it's queue 
    * and won't accept new jobs.
    * @return
    */
   @TpmonInternal
   public boolean isFinished();
       
   /**
    * Notifies the worker that the system shutdown process is initiated and that it should finish soon.
    * After a while, isFinished should result in true, if system can halt.
    */
   @TpmonInternal
   public void initShutdown();
}
