package kieker.tpmon.writer.util.async;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.AbstractWorkerThread
 * 
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * @author Andre van Hoorn
 */
public abstract class AbstractWorkerThread extends Thread {
    private static final Log log = LogFactory.getLog(AbstractWorkerThread.class);

   private boolean writeRecordTypeIds = false;

    
    public boolean isWriteRecordTypeIds() {
        return writeRecordTypeIds;
    }

    
    public void setWriteRecordTypeIds(boolean writeRecordTypeIds) {
        this.writeRecordTypeIds = writeRecordTypeIds;
    }

   /**
    * initShutdown has to be called before isFinished will result in true.
    * IsFinished == true means that the worker does not have additional jobs in it's queue 
    * and won't accept new jobs.
    */
    
   public abstract boolean isFinished();
       
   /**
    * Notifies the worker that the system shutdown process is initiated and that it should finish soon.
    * After a while, isFinished should result in true, if system can halt.
    */
    
   public abstract void initShutdown();
}
