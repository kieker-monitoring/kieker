package kieker.tpmon.writer.filesystemAsync;

import java.io.File;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.writer.AbstractMonitoringDataWriter;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;
import kieker.tpmon.annotation.TpmonInternal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.asyncFsWriter.AsyncFsWriterProducer
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
 * @author Matthias Rohr, Andre van Hoorn
 */
public class AsyncFsWriterProducer extends AbstractMonitoringDataWriter {

    private static final Log log = LogFactory.getLog(AsyncFsWriterProducer.class);
    //configuration parameter
    private static final int numberOfFsWriters = 1; // one is usually sufficient and more usuable since only one file is created at once
    //internal variables
    private Vector<AbstractWorkerThread> workers = new Vector<AbstractWorkerThread>();
    private BlockingQueue<AbstractKiekerMonitoringRecord> blockingQueue = null;
    private String storagePathBase = null;
    private final static String defaultConstructionErrorMsg =
            "Do not select this writer using the full-qualified classname. " +
            "Use the the constant " + TpmonController.WRITER_ASYNCFS +
            " and the file system specific configuration properties.";

    public AsyncFsWriterProducer() {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
    }

    @TpmonInternal()
    public boolean init(String initString) {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
    }

    @TpmonInternal()
    public Vector<AbstractWorkerThread> getWorkers() {
        return workers;
    }

    public AsyncFsWriterProducer(String storagePathBase) {
        this.storagePathBase = storagePathBase;
        this.init();
    }

    @TpmonInternal()
    public void init() {
        File f = new File(storagePathBase);
        if (!f.isDirectory()) {
            log.error(this.storagePathBase + " is not a directory");
            log.error("Will abort init().");
            return;
        }

        // TODO Change to format: yyyymmdd-hhmmss
        int time = (int) (System.currentTimeMillis() - 1177404043379L);     // TODO: where does this number come from?
        String storageDir = this.storagePathBase + "/tpmon--" + time + "/";

        f = new File(storageDir);
        if(!f.mkdir()){
            log.error("Failed to create directory '"+this.storagePathBase + "'");
            log.error("Will abort init().");
            return;
        }
        log.info("Directory for monitoring data: " + storageDir);

        blockingQueue = new ArrayBlockingQueue<AbstractKiekerMonitoringRecord>(8000);
        for (int i = 0; i < numberOfFsWriters; i++) {
            AsyncFsWriterWorkerThread dbw = new AsyncFsWriterWorkerThread(blockingQueue, storageDir);
            //dbw.setDaemon(true); might lead to inconsistent data due to harsh shutdown
            workers.add(dbw);
            dbw.start();
        }
        //System.out.println(">Kieker-Tpmon: (" + numberOfFsWriters + " threads) will write to the file system");
        log.info(">Kieker-Tpmon: (" + numberOfFsWriters + " threads) will write to the file system");
    }

    /**
     * This method is not synchronized, in contrast to the insert method of the Dbconnector.java.
     */
    @TpmonInternal()
    public boolean writeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord) {
        if (this.isDebug()) {
            log.info(">Kieker-Tpmon: AsyncFsWriterDispatcher.insertMonitoringDataNow");
        }

        try {
            blockingQueue.add(monitoringRecord); // tries to add immediately!
        //System.out.println(""+blockingQueue.size());
        } catch (Exception ex) {
            log.error(">Kieker-Tpmon: " + System.currentTimeMillis() + " insertMonitoringData() failed: Exception: " + ex);
            return false;
        }
        return true;
    }

    @TpmonInternal()
    public String getFilenamePrefix() {
        return storagePathBase;
    }

    @TpmonInternal()
    public String getInfoString() {
        return "filenamePrefix :" + storagePathBase;
    }

    @TpmonInternal()
    public void registerMonitoringRecordType(int id, String className) {
        log.info("Registered monitoring record type with id '" + id + "':" + className);
    //throw new UnsupportedOperationException("Not supported yet.");
    }
}
