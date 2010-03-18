package kieker.tpmon.writer;

import java.util.HashMap;
import java.util.StringTokenizer;
import kieker.common.monitoringRecord.AbstractKiekerMonitoringRecord;
import kieker.tpmon.annotation.TpmonInternal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.AbstractKiekerMonitoringLogWriter.java
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
public abstract class AbstractKiekerMonitoringLogWriter implements IKiekerMonitoringLogWriter {

    private static final Log log = LogFactory.getLog(AbstractKiekerMonitoringLogWriter.class);

    private boolean debugEnabled;
    private boolean writeRecordTypeIds;
    private final HashMap<String, String> map = new HashMap<String, String>();

    @TpmonInternal()
    public abstract boolean writeMonitoringRecord(AbstractKiekerMonitoringRecord monitoringRecord);

    @TpmonInternal()
    public abstract void registerMonitoringRecordType(int id, String className);

    /** Returns the value for the initialization property @a propName or the
     *  the passed default value @a default if no value for this property
     *  exists. */
   @TpmonInternal()
    protected final String getInitProperty(String propName, String defaultVal) {
        if (!this.initStringProcessed) {
            log.error("InitString not yet processed. " +
                    " Call method initVarsFromInitString(..) first.");
            return null;
        }

        String retVal = this.map.get(propName);
        if (retVal == null) {
            retVal = defaultVal;
        }

        return retVal;
    }

    /** Returns the value for the initialization property @a propName or null
     *  if no value for this property exists. */
   @TpmonInternal()
    protected final String getInitProperty(String propName) {
        return this.getInitProperty(propName, null);
    }
    private boolean initStringProcessed = false;

    /**
     * Parses the initialization string @a initString for this component.
     * The initilization string consists of key/value pairs.
     * After this method is executed, the parameter values can be retrieved
     * using the method getInitProperty(..).
     */
   @TpmonInternal()
    protected final void initVarsFromInitString(String initString) throws IllegalArgumentException {
        if (initString == null || initString.length() == 0) {
            initStringProcessed = true;
            return;
        }

        try {
            StringTokenizer keyValListTokens = new StringTokenizer(initString, "|");
            while (keyValListTokens.hasMoreTokens()) {
                String curKeyValToken = keyValListTokens.nextToken().trim();
                StringTokenizer keyValTokens = new StringTokenizer(curKeyValToken, "=");
                if (keyValTokens.countTokens() != 2) {
                    throw new IllegalArgumentException("Expected key=value pair, found " + curKeyValToken);
                }
                String key = keyValTokens.nextToken().trim();
                String val = keyValTokens.nextToken().trim();
                log.info("Found key/value pair: " + key + "=" + val);
                map.put(key, val);
            }
        } catch (Exception exc) {
            throw new IllegalArgumentException("Error parsing init string '" + initString + "'", exc);
        }

        initStringProcessed = true;
    }

   @TpmonInternal()
    public boolean isWriteRecordTypeIds(){
       return this.writeRecordTypeIds;
   }

    @TpmonInternal()
    public void setWriteRecordTypeIds(final boolean writeRecordTypeIds){
        this.writeRecordTypeIds = writeRecordTypeIds;
    }

    @TpmonInternal()
    public void setDebug(boolean debug) {
        this.debugEnabled = debug;
    }

    @TpmonInternal()
    public boolean isDebug() {
        return this.debugEnabled;
    }
}
