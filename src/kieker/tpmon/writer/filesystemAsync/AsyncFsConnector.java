package kieker.tpmon.writer.filesystemAsync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kieker.tpmon.writer.AbstractKiekerMonitoringLogWriter;
import kieker.common.monitoringRecord.AbstractKiekerMonitoringRecord;
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
public final class AsyncFsConnector extends AbstractKiekerMonitoringLogWriter {

    private static final Log log = LogFactory.getLog(AsyncFsConnector.class);
    //configuration parameter
    private static final int numberOfFsWriters = 1; // one is usually sufficient and more usuable since only one file is created at once
    //internal variables
    private Vector<AbstractWorkerThread> workers = new Vector<AbstractWorkerThread>();
    private BlockingQueue<AbstractKiekerMonitoringRecord> blockingQueue = null;
    private String storagePathBase = null;
    private String storageDir = null; // full path
    private int asyncRecordQueueSize = 8000;
    private File mappingFile = null;
    private final static String defaultConstructionErrorMsg =
            "Do not select this writer using the full-qualified classname. " +
            "Use the the constant " + TpmonController.WRITER_ASYNCFS +
            " and the file system specific configuration properties.";

    public AsyncFsConnector() {
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

    public AsyncFsConnector(String storagePathBase, int asyncRecordQueueSize) {
        this.storagePathBase = storagePathBase;
        this.asyncRecordQueueSize = asyncRecordQueueSize;
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

        DateFormat m_ISO8601UTC =
                new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
        m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateStr = m_ISO8601UTC.format(new java.util.Date());
        storageDir = this.storagePathBase + "/tpmon-" + dateStr + "-UTC/";

        f = new File(storageDir);
        if (!f.mkdir()) {
            log.error("Failed to create directory '" + this.storagePathBase + "'");
            log.error("Will abort init().");
            return;
        }
        log.info("Directory for monitoring data: " + storageDir);

        try {
            this.mappingFile = new File(storageDir + File.separatorChar + "tpmon.map");
            this.mappingFile.createNewFile();
        } catch (Exception exc) {
            log.error("Failed to create mapping file '" + this.mappingFile.getAbsolutePath() + "'", exc);
            log.error("Will abort init().");
            return;
        }

        blockingQueue = new ArrayBlockingQueue<AbstractKiekerMonitoringRecord>(asyncRecordQueueSize);
        for (int i = 0; i < numberOfFsWriters; i++) {
            FsWriterThread dbw = new FsWriterThread(blockingQueue, storageDir + "/tpmon");
            dbw.setDaemon(true); // might lead to inconsistent data due to harsh shutdown
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
    public boolean writeMonitoringRecord(final AbstractKiekerMonitoringRecord monitoringRecord) {
        if (this.isDebug()) {
            log.info(">Kieker-Tpmon: AsyncFsWriterDispatcher.insertMonitoringDataNow");
        }

        if(monitoringRecord == TpmonController.END_OF_MONITORING_MARKER){
            log.info(log);
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
        return "filenamePrefix :" + this.storagePathBase +
                ", outputDirectory :" + this.storageDir;
    }

    @TpmonInternal()
    public void registerMonitoringRecordType(int id, String className) {
        log.info("Registered monitoring record type with id '" + id + "':" + className);
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            fos = new FileOutputStream(this.mappingFile, true); // append
            pw = new PrintWriter(fos);
            pw.println("$" + id + "=" + className);
        } catch (Exception exc) {
            log.fatal("Failed to register record type", exc);
        } finally {
            try {
                pw.close();
                fos.close();
            } catch (IOException exc) {
                log.error("IO Exception", exc);
            }
        }
    }

    @TpmonInternal()
    public void setWriteRecordTypeIds(boolean writeRecordTypeIds) {
        super.setWriteRecordTypeIds(writeRecordTypeIds);
        for (AbstractWorkerThread t : workers) {
            log.info("t.setWriteRecordTypeIds(" + writeRecordTypeIds + ")");
            t.setWriteRecordTypeIds(writeRecordTypeIds);
        }
    }
}
