package kieker.tpmon.monitoringRecord;

/*
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
 */
import java.io.Serializable;
import kieker.tpmon.annotation.TpmonInternal;

import kieker.tpmon.core.TpmonController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn
 */
public abstract class AbstractKiekerMonitoringRecord implements Serializable {

    private static final Log log = LogFactory.getLog(AbstractKiekerMonitoringRecord.class);
    private long loggingTimestamp = -1;

    @TpmonInternal()
    public static AbstractKiekerMonitoringRecord getInstance() {
        throw new UnsupportedOperationException("Extending classes must override this method");
    }

    /**
     * Sets the values of this monitoring object's field according to
     * the csvRecord. Additionally, the internal recordTypeId must be
     * set accordingly.
     *
     * @param recordTypeId
     * @param recordVector
     */
    @TpmonInternal()
    public abstract void initFromStringArray(String[] recordVector)
            throws IllegalArgumentException;

    /**
     * Returns a CSV record for the object.
     */
    @TpmonInternal()
    public abstract String[] toStringArray();

    @TpmonInternal()
    public abstract int getRecordTypeId();

    @TpmonInternal()
    public final long getLoggingTimestamp() {
        return this.loggingTimestamp;
    }

    @TpmonInternal()
    public final void setLoggingTimestamp(long loggingTimestamp) {
        this.loggingTimestamp = loggingTimestamp;
    }

    /**
     * Registers monitoring record type and returns its id.
     * If logging of record ids is disabled, -1 is returned and no
     * registration takes place.
     *
     * All implementing record types must call this method <b>once</b>
     * in order to get their type id.
     *
     * @param recordTypeClass
     * @return
     */
    @TpmonInternal()
    protected static final int registerMonitoringRecordType(Class recordTypeClass) {
        // ctrlInst leads to error
        // REALY UGLY DEPENDNECY! Every tool that uses AbstractKiekerExecutionMonitoringRecord needs JMS stuff...
        //
        // (by Andre) Not true: only if JMS is configured as writer; but yes, this dependency is very ugly!
        return TpmonController.getInstance().registerMonitoringRecordType(recordTypeClass);
    }


     // TODO (by André): Why is the separator declared here? Should be configured
     //                  for a reader/writer.
     public static final String separator = ";";

     // TODO (by André): we should not (ab)use toString for serialization purposes
     //                  since this is not the purpose of Object's toString method.
     /**
     * Creates a string serialization for this KiekerExecutionRecord, that
     * can be used to reconstruct a KiekerExecutionRecord, based on the initFromString
     * method.
     *
     * Semicolon is used as internal separator (static variable separator). There are no semicolons in methodsnames, etc. but
     * be careful with extensions.
     *
     * Matthias Rohr
     */
    @Override
    @TpmonInternal()
    public final String toString() {
        String[] recordVector = this.toStringArray();
        StringBuilder sb = new StringBuilder();
        boolean first=true;
        for (String curStr : recordVector) {
            if (!first) {
                sb.append(AbstractKiekerMonitoringRecord.separator);
            }
            sb.append(curStr);
            first = false;
        }
        return sb.toString();
    }

    //    @Override
//    @TpmonInternal()
//    public String toString() {
//        StringBuilder strBuild = new StringBuilder();
//        String[] valueVec = this.toStringArray();
//        for (String v : valueVec) {
//            strBuild.append(v);
//            strBuild.append(' ');
//        }
//        return strBuild.toString();
//    }
}
