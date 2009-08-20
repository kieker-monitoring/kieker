package kieker.common.logReader.filesystemReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;

import kieker.common.logReader.AbstractKiekerMonitoringLogReader;
import kieker.common.logReader.LogReaderExecutionException;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
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
 */
/**
 * @author Matthias Rohr, Andre van Hoorn
 *
 * History:
 * 2008/09/15: Initial version
 */
public class FSReader extends AbstractKiekerMonitoringLogReader {

    private static final Log log = LogFactory.getLog(FSReader.class);
    private File inputDir = null;

    //private FSReader(){ }

    public FSReader(final String inputDirName) {
        this.inputDir = new File(inputDirName);
    }

    @Override
    @TpmonInternal()
    public boolean execute() throws LogReaderExecutionException {
        boolean retVal = false;
        try {
            File[] inputFiles = this.inputDir.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    return pathname.isFile() &&
                            pathname.getName().startsWith("tpmon") &&
                            pathname.getName().endsWith(".dat");
                }
            });

            Arrays.sort(inputFiles, new FileComparator()); // sort alphabetically
            for (int i = 0; inputFiles != null && i < inputFiles.length; i++) {
                this.processInputFile(inputFiles[i]);
            }
            if (inputFiles == null) {
                throw new LogReaderExecutionException("Directory '" + this.inputDir + "' does not exist or an I/O error occured");
            } else {
                retVal = true;
            }
        } catch (Exception e) {
            LogReaderExecutionException readerEx = new LogReaderExecutionException("An error occurred while parsing files from directory " +
                    this.inputDir.getAbsolutePath() + ":", e);
            log.error("Exception", readerEx);
            throw readerEx;
        } finally {
            super.terminate();
        }
        return retVal;
    }
    HashMap<Integer, Class<? extends AbstractKiekerMonitoringRecord>> recordTypeMap = new HashMap<Integer, Class<? extends AbstractKiekerMonitoringRecord>>();

    @TpmonInternal()
    //@SuppressWarnings("unchecked")
    private void readMappingFile() throws IOException {
        File mappingFile = new File(this.inputDir.getAbsolutePath() + File.separator + "tpmon.map");
        BufferedReader in = null;
        StringTokenizer st = null;
        try {
            in = new BufferedReader(new FileReader(mappingFile));
            String line;

            while ((line = in.readLine()) != null) {
                try {
                    st = new StringTokenizer(line, "=");
                    int numTokens = st.countTokens();
                    if (numTokens == 0) {
                        continue;
                    }
                    if (numTokens != 2) {
                        throw new IllegalArgumentException("Invalid number of tokens (" + numTokens + ") Expecting 2");
                    }
                    String idStr = st.nextToken();
                    // the leading $ is optional
                    Integer id = Integer.valueOf(idStr.startsWith("$") ? idStr.substring(1) : idStr);
                    String classname = st.nextToken();
                    log.info("Found mapping: " + id + "<->" + classname);
                    log.info("Loading record type class '" + classname + "'");
                    Class<? extends AbstractKiekerMonitoringRecord> recordClass = Class.forName(classname).asSubclass(AbstractKiekerMonitoringRecord.class);
                    this.recordTypeMap.put(id, recordClass);
                } catch (Exception e) {
                    log.error(
                            "Failed to parse line: {" + line + "} from file " +
                            mappingFile.getAbsolutePath(), e);
                    break;
                }
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    log.error("Exception", e);
                }
            }
        }
    }

    @TpmonInternal()
    private void processInputFile(final File input) throws IOException, LogReaderExecutionException {
        log.info("< Loading " + input.getAbsolutePath());

        BufferedReader in = null;
        boolean recordTypeIdMapInitialized = false; // will read it "on-demand"
        StringTokenizer st = null;

        try {
            in = new BufferedReader(new FileReader(input));
            String line;

            while ((line = in.readLine()) != null) {
                AbstractKiekerMonitoringRecord rec = null;
                try {
                    if (!recordTypeIdMapInitialized && line.startsWith("$")) {
                        this.readMappingFile();
                        recordTypeIdMapInitialized = true;
                    }
                    st = new StringTokenizer(line, ";");
                    int numTokens = st.countTokens();
                    String[] vec = null;
                    boolean haveTypeId = false;
                    for (int i = 0; st.hasMoreTokens(); i++) {
//                        log.info("i:" + i + " numTokens:" + numTokens + " hasMoreTokens():" + st.hasMoreTokens());
                        String token = st.nextToken();
                        if (i == 0 && token.startsWith("$")) {
                            /* We found a record type ID and need to lookup the class */
//                            log.info("i:" + i + " numTokens:" + numTokens + " hasMoreTokens():" + st.hasMoreTokens());

                            Integer id = Integer.valueOf(token.substring(1));
                            Class<? extends AbstractKiekerMonitoringRecord> clazz = this.recordTypeMap.get(id);
                            Method m = clazz.getMethod("getInstance"); // lookup method getInstance
                            rec = (AbstractKiekerMonitoringRecord) m.invoke(null); // call static method
                            token = st.nextToken();
                            //log.info("LoggingTimestamp: " + Long.valueOf(token) + " (" + token + ")");
                            rec.setLoggingTimestamp(Long.valueOf(token));
                            vec = new String[numTokens - 2];
                            haveTypeId = true;
                        } else if (i == 0) { // for historic reasons, this is the default type
                            rec = KiekerExecutionRecord.getInstance();
                            vec = new String[numTokens];
                        }
                        //log.info("haveTypeId:" + haveTypeId + ";" + " token:" + token + " i:" + i);
                        if (!haveTypeId || i > 0) { // only if current field is not the id
                            vec[haveTypeId ? i - 1 : i] = token;
                        }
                    }
                    if (vec == null) {
                        vec = new String[0];
                    }

                    rec.initFromStringArray(vec);
                    this.deliverRecordToConsumers(rec);
                } catch (Exception e) {
                    log.error(
                            "Failed to parse line: {" + line + "} from file " +
                            input.getAbsolutePath(), e);
                    log.error("Abort reading");
                    throw new LogReaderExecutionException("LogReaderExecutionException ", e);
                }
            }
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    log.error("Exception", e);
                }
            }
        }
    }

    /** source: http://weblog.janek.org/Archive/2005/01/16/HowtoSortFilesandDirector.html */
    private static class FileComparator
            implements Comparator<File> {

        private Collator c = Collator.getInstance();

        public int compare(File f1,
                File f2) {
            if (f1 == f2) {
                return 0;
            }

            if (f1.isDirectory() && f2.isFile()) {
                return -1;
            }
            if (f1.isFile() && f2.isDirectory()) {
                return 1;
            }

            return c.compare(f1.getName(), f2.getName());
        }
    }
}
