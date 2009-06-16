package kieker.tpmon.writer.filesystemSync;

import kieker.tpmon.writer.AbstractWorkerThread;
import kieker.tpmon.writer.AbstractMonitoringDataWriter;
import kieker.tpmon.*;
import java.io.*;
import java.util.Random;
import java.util.Vector;
import kieker.tpmon.annotations.TpmonInternal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.FileSystemWriter
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
 * Simple class to store monitoroing data in the file system. Although a
 * buffered writer is used, outliers (delays of 1000 ms) occur from time
 * to time if many monitoring events have to be writen. We believe that
 * outliers result from a flush on the buffer of the writer. 
 * 
 * A more sophisticated writer to store data in the file system is 
 * the AsyncFsWriter. This does not introduce the outliers that result
 * from flushing the writing buffer, since provides an asynchronous 
 * insertMonitoringData method. However, the AsyncFsWriter introduces
 * a little more overhead because a writing queue is required and it isn't
 * tested as much as the FileSystenWriter. Additionally, the resource demands (CPU, 
 * bus etc.) for writing monitoring data are not anymore occurring during the time 
 * of the execution that is monitored, but at some other (unknown) time.    
 * 
 * The AsyncFsWriter should usually be used instead of this class to avoid 
 * the outliers described above.
 *  
 * The asyncFsWriter is not(!) faster (but also it shouldn't be much slower) because
 * only one thread is used for writing into a single file. To tune it, it might be an
 * option to write to multiple files, while writing with more than one thread into
 * a single file is not considered a save option.     
 *
 * @author Matthias Rohr
 * 
 * History: 
 * 2008/01/04: Refactoring for the first release of 
 *             Kieker and publication under an open source licence
 * 2007/03/13: Refactoring
 * 2006/12/20: Initial Prototype
 */
public class FileSystemWriter extends AbstractMonitoringDataWriter {

    private static final Log log = LogFactory.getLog(FileSystemWriter.class);
    // configuration parameters
    private static final int maxEntriesInFile = 22000;
    // internal variables
    private String filenamePrefix = "";
    private boolean filenameInitialized = false;
    private int entriesInCurrentFileCounter = 0;
    private PrintWriter pos = null;

    private final static String defaultConstructionErrorMsg = 
            "Do not select this writer using the fully qualified classname. " +
            "Use the the constant " + TpmonController.WRITER_SYNCFS +
                " and the file system specific configuration properties.";
    
    public FileSystemWriter() {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
    }
    
    @TpmonInternal()
    public boolean init(String initString) {
        throw new UnsupportedOperationException(defaultConstructionErrorMsg);
    }
    
    public FileSystemWriter(String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
    }

    /**
     * Determines and sets a new Filename
     */
    @TpmonInternal()
    private void prepareFile() throws FileNotFoundException {
        if (entriesInCurrentFileCounter++ > maxEntriesInFile || !filenameInitialized) {
            if (pos != null) {
                pos.close();
            }
            filenameInitialized = true;
            entriesInCurrentFileCounter = 0;

            int time = (int) (System.currentTimeMillis() - 1177404043379L);     // TODO: where does this number come from ??
            int random = (new Random()).nextInt(100);
            String filename = this.filenamePrefix + time + "-" + random + ".dat";
            log.info("** " + java.util.Calendar.getInstance().getTime().toString() + " new filename: " + filename);
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                DataOutputStream dos = new DataOutputStream(bos);
                pos = new PrintWriter(dos);
                pos.flush();
            } catch (FileNotFoundException ex) {
                log.error("Tpmon: Error creating the file: " + filename + " \n ", ex);
                throw ex;
            }
        }
    }

    @TpmonInternal()
    public synchronized boolean insertMonitoringDataNow(KiekerExecutionRecord execData) {
        try {
            prepareFile(); // may throw FileNotFoundException
            pos.println(execData.toKiekerCSVRecord());
            pos.flush();
        } catch (IOException ex) {
            log.error("Failed to write data", ex);
            return false;
        }
        return true;
    }

    @TpmonInternal()
    public Vector<AbstractWorkerThread> getWorkers() {
        return null;
    }
    
   @TpmonInternal()
    public String getInfoString() {
        return "filenamePrefix :" + filenamePrefix;
    }
}
