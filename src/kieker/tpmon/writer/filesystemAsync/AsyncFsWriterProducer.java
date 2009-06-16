package kieker.tpmon.writer.filesystemAsync;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.writer.core.AbstractMonitoringDataWriter;
import kieker.tpmon.monitoringRecord.KiekerExecutionRecord;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.writer.core.AbstractWorkerThread;
import kieker.tpmon.annotations.TpmonInternal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.asyncFsWriter.AsyncFsWriterProducer
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
 * 
 * @author Matthias Rohr
 */
public class AsyncFsWriterProducer extends AbstractMonitoringDataWriter {

    private static final Log log = LogFactory.getLog(AsyncFsWriterProducer.class);
    //configuration parameter
    private static final int numberOfFsWriters = 1; // one is usually sufficient and more usuable since only one file is created at once
    //internal variables
    private Vector<AbstractWorkerThread> workers = new Vector<AbstractWorkerThread>();
    private BlockingQueue<KiekerExecutionRecord> blockingQueue = null;
    private String filenamePrefix = null;
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

    public AsyncFsWriterProducer(String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
        this.init();
    }

    @TpmonInternal()
    public void init() {
        blockingQueue = new ArrayBlockingQueue<KiekerExecutionRecord>(8000);
        for (int i = 0; i < numberOfFsWriters; i++) {
            Thread workerThread;
            AsyncFsWriterWorkerThread dbw = new AsyncFsWriterWorkerThread(blockingQueue, filenamePrefix);
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
    public boolean insertMonitoringDataNow(KiekerExecutionRecord execData) {
        if (this.isDebug()) {
            log.info(">Kieker-Tpmon: AsyncFsWriterDispatcher.insertMonitoringDataNow");
        }

        try {
            blockingQueue.add(execData); // tries to add immediately!
            //System.out.println(""+blockingQueue.size());
        } catch (Exception ex) {
            log.error(">Kieker-Tpmon: " + System.currentTimeMillis() + " insertMonitoringData() failed: Exception: " + ex);
            return false;
        }
        return true;
    }

    @TpmonInternal()
    public String getFilenamePrefix() {
        return filenamePrefix;
    }

    @TpmonInternal()
    public String getInfoString() {
        return "filenamePrefix :" + filenamePrefix;
    }
}
