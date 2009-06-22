package kieker.tpmon.reader.fileSystemReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;
import kieker.tpmon.core.TpmonController;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.fileSystemReader.FileSystemReader
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
 * This reader allows one to read a folder or an single tpmon file and 
 * transforms it to monitoring events that are stored in the file system 
 * again, written to a database, or whatever tpmon is configured to do
 * with the monitoring data.
 *
 * @author Matthias Rohr, Andre van Hoorn
 * 
 * History:
 * 2008/09/15: Initial version
 */
public class FileSystemReader {

    private static final Log log = LogFactory.getLog(FileSystemReader.class);
    private static FileSystemReader instance;

    // instance variables
    private TpmonController ctrl = null;
    private File inputDir = null;

    @TpmonInternal()
    public static void main(String[] args) {

//       Properties props = System.getProperties();
//       Iterator it = props.keySet().iterator();
//       while(it.hasNext()) {
//           Object curKey = it.next();
//           System.out.println("Key "+curKey.toString() + " "+props.getProperty(curKey.toString()).toString());
//       }

        String inputDir = System.getProperty("inputDir");
        if (inputDir == null || inputDir.length() == 0 || inputDir.equals("${inputDir}")) {
            log.error("No input dir found!");
            log.error("Provide an input dir as system property.");
            log.error("Example to read all tpmon-* files from /tmp:\n" +
                    "                    ant -DinputDir=/tmp/ run-reader    ");
            System.exit(1);
        } else {
            log.info("Reading all tpmon-* files from " + inputDir);
        }

        instance = FileSystemReader.instance();
        instance.setInputDir(new File(inputDir));

        log.info("Activating Tpmon");
        if (!instance.setCtrl(TpmonController.getInstance())) {
            log.error("Initialization of tpmon failed");
            System.exit(1);
        }
        log.info("Tpmon initialized");
        log.info("Staring to read files");
        instance.openAndRegisterData();
        log.info("Finished to read files");
        System.exit(0);
    }

    @TpmonInternal()
    public boolean setCtrl(TpmonController ctrl) {
        this.ctrl = ctrl;
        return (ctrl != null);
    }

    /**
     * @return the singleton instance of this class
     */
    @TpmonInternal()
    public static synchronized FileSystemReader instance() {
        if (instance == null) {
            instance = new FileSystemReader();
        }
        return instance;
    }

    @TpmonInternal()
    public void openAndRegisterData() {
        if (inputDir == null) {
            throw new IllegalStateException("call setInputDir first");
        }
        try {
            File[] inputFiles = inputDir.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    return pathname.isFile() &&
                            pathname.getName().startsWith("tpmon") &&
                            pathname.getName().endsWith(".dat");
                }
            });
            for (int i = 0; i < inputFiles.length; i++) {
                processInputFile(inputFiles[i]);
            }
        } catch (IOException e) {
            System.err.println(
                    "An error occurred while parsing files from directory " +
                    inputDir.getAbsolutePath() + ":");
            e.printStackTrace();
        }
    }

//    
//    long maximumNumberOfTracesToLoad = Long.MAX_VALUE;
//    /**
//     * Limits the number of traces to load from the data source -- this should
//     * allow to speed up exploring experiment data. Not every datasource might
//     * really obey to this limit.
//     * 
//     * default value: Long.MAX_VALUE (basically no limit, memory will be the limit earlier...)
//     * @param numberOfTracesToLoad
//     */
//    public void setMaximumNumberOfTracesToLoad(long numberOfTracesToLoad) {        
//        maximumNumberOfTracesToLoad = numberOfTracesToLoad;
//    }
    /**
     * Configures the input directory that will be processed by
     * the next call to {@link #openAndRegisterData()}
     */
    @TpmonInternal()
    public void setInputDir(File inputDir) {
        if (inputDir == null) {
            throw new IllegalArgumentException("inputDir null");
        }
        if (!inputDir.isDirectory()) {
            throw new IllegalArgumentException("inputDir is not a directory: " + inputDir.getAbsolutePath());
        }
        this.inputDir = inputDir;
    }
    // The following data structure will be reused
    //to save the allocation for each execution
    private StringTokenizer st;
    int degradableSleepTime = 0;

    @TpmonInternal()
    private void processInputFile(File input) throws IOException {
        log.info("< Loading " + input.getAbsolutePath());

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(input));
            String line;

            while ((line = in.readLine()) != null) {
                try {
                    st = new StringTokenizer(line, ";");
                    int numTokens = st.countTokens();
                    Vector<String> vec = new Vector<String>(numTokens);
                    for (int i=0; i<numTokens; i++){
                        vec.insertElementAt(st.nextToken(), i);
                    }

                    if (degradableSleepTime > 0) {
                        Thread.sleep(degradableSleepTime * 5);
                    }

                    AbstractKiekerMonitoringRecord rec = KiekerExecutionRecord.getInstance();
                    rec.initFromStringVector(vec);

                    while (!ctrl.logMonitoringRecord(rec)) {
                        Thread.sleep(500);
                        ctrl.enableMonitoring();
                        degradableSleepTime += 50;
                    }
                    if (degradableSleepTime > 0) {
                        degradableSleepTime--;
                    }
                } catch (Exception e) {
                    log.error(
                            "Failed to parse line: {" + line + "} from file " +
                            input.getAbsolutePath(), e);
                    log.error("Abort reading");
                    break;
                }
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) { log.error("Exception", e); }
            }
        }
    }
}
