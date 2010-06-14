package kieker.analysis.reader.filesystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.MonitoringRecordTypeRegistry;

import kieker.analysis.reader.AbstractMonitoringLogReader;
import kieker.analysis.reader.MonitoringLogReaderException;
import kieker.common.record.OperationExecutionRecord;
import kieker.common.util.PropertyMap;

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
 */

/**
 * This reader allows one to read a folder (the contained tpmon-*.dat files, 
 * respectively) and transform pass the monitoring events to registered consumers.
 *
 * @author Matthias Rohr, Andre van Hoorn
 *
 * History:
 * 2008/09/15: Initial version
 */
class FSDirectoryReader extends AbstractMonitoringLogReader {

    private static final String PROP_NAME_INPUTDIR = "inputDirName";
    private static final Log log = LogFactory.getLog(FSDirectoryReader.class);
    private final MonitoringRecordTypeRegistry typeRegistry = new MonitoringRecordTypeRegistry();

    private volatile boolean recordTypeIdMapInitialized = false; // will read it "on-demand"

    private File inputDir = null;

    /** Constructor for FSDirectoryReader. Requires a subsequent call to the init
     *  method in order to specify the input directory using the parameter
     *  @a inputDirName. */
    public FSDirectoryReader(){ }

    public FSDirectoryReader(final String inputDirName) {
        initInstanceFromArgs(inputDirName); // throws IllegalArgumentException
    }

    /** Valid key/value pair: inputDirName=INPUTDIRECTORY */
    public void init(String initString) throws IllegalArgumentException{
        PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws IllegalArgumentException
        this.initInstanceFromArgs(propertyMap.getProperty(PROP_NAME_INPUTDIR)); // throws IllegalArgumentException
    }

   private void initInstanceFromArgs(final String inputDirName) throws IllegalArgumentException {
        if (inputDirName == null || inputDirName.equals("")) {
            throw new IllegalArgumentException("Invalid or missing property "+PROP_NAME_INPUTDIR+": " + inputDirName);
        }
        this.inputDir = new File(inputDirName);
    }

    static final String filePrefix = "tpmon";
    static final String filePostfix = ".dat";

    @Override
    public boolean read() throws MonitoringLogReaderException {
        boolean retVal = false;
        try {
            File[] inputFiles = this.inputDir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isFile() &&
                            pathname.getName().startsWith(filePrefix) &&
                            pathname.getName().endsWith(filePostfix);
                }
            });

            if (inputFiles == null) {
                throw new MonitoringLogReaderException("Directory '" + this.inputDir + "' does not exist or an I/O error occured. No files starting with '"
                        +filePrefix+"' and ending with '"+filePostfix+"' could be found.");
            } else {
                retVal = true;
            }
            Arrays.sort(inputFiles, new FileComparator()); // sort alphabetically
            for (int i = 0; inputFiles != null && i < inputFiles.length; i++) {
                this.processInputFile(inputFiles[i]);
            }
        } catch (MonitoringLogReaderException e) {
            log.error("Exception", e);
            throw e;
        } catch (Exception e2) {
            if (!(e2 instanceof MonitoringLogReaderException)) {
                MonitoringLogReaderException readerEx = new MonitoringLogReaderException("An error occurred while parsing files from directory " +
                        this.inputDir.getAbsolutePath() + ":", e2);
                log.error("Exception", readerEx);
                throw readerEx;
            }
        } 
        return retVal;
    }

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
                    this.typeRegistry.registerRecordTypeIdMapping(id, classname);
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

    private void processInputFile(final File input) throws IOException, MonitoringLogReaderException {
        log.info("< Loading " + input.getAbsolutePath());

        BufferedReader in = null;
        StringTokenizer st = null;

        try {
            in = new BufferedReader(new FileReader(input));
            String line;

            while ((line = in.readLine()) != null) {
                IMonitoringRecord rec = null;
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
                            // TODO: use IDs
                            Class<? extends IMonitoringRecord> clazz = this.typeRegistry.fetchClassForRecordTypeId(id);
                            rec = (IMonitoringRecord) clazz.newInstance();
                            token = st.nextToken();
                            //log.info("LoggingTimestamp: " + Long.valueOf(token) + " (" + token + ")");
                            rec.setLoggingTimestamp(Long.valueOf(token));
                            vec = new String[numTokens - 2];
                            haveTypeId = true;
                        } else if (i == 0) { // for historic reasons, this is the default type
                            rec = new OperationExecutionRecord();
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

                    // TODO: create typed array
                    Object[] typedArray = StringToTypedArray(vec, rec.getValueTypes());
                    rec.initFromArray(typedArray);
                    this.deliverRecord(rec);
                } catch (Exception e) {
                    log.error(
                            "Failed to parse line: {" + line + "} from file " +
                            input.getAbsolutePath(), e);
                    log.error("Abort reading");
                    throw new MonitoringLogReaderException("LogReaderExecutionException ", e);
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

    private final Object[] StringToTypedArray(String[] vec, Class[] valueTypes)
    throws IllegalArgumentException{
        final Object[] typedArray = new Object[vec.length];
        int curIdx = -1;
        for (Class clazz : valueTypes){
            curIdx++;
            if (clazz == String.class){
                typedArray[curIdx] = vec[curIdx];
                continue;
            }
            if (clazz == int.class || clazz == Integer.class){
                typedArray[curIdx] = Integer.valueOf(vec[curIdx]);
                continue;
            }
            if (clazz == long.class || clazz == Long.class){
                typedArray[curIdx] = Long.valueOf(vec[curIdx]);
                continue;
            }
            if (clazz == float.class || clazz == Float.class){
                typedArray[curIdx] = Float.valueOf(vec[curIdx]);
                continue;
            }
            if (clazz == double.class || clazz == Double.class){
                typedArray[curIdx] = Double.valueOf(vec[curIdx]);
                continue;
            }
            if (clazz == byte.class || clazz == Byte.class){
                typedArray[curIdx] = Byte.valueOf(vec[curIdx]);
                continue;
            }
            if (clazz == short.class || clazz == Short.class){
                typedArray[curIdx] = Short.valueOf(vec[curIdx]);
                continue;
            }
            if (clazz == boolean.class || clazz == Boolean.class){
                typedArray[curIdx] = Boolean.valueOf(vec[curIdx]);
                continue;
            }
            throw new IllegalArgumentException("Unsupported type: " + clazz.getName());
        }
        return typedArray;
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
