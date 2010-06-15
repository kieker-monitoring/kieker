package kieker.common.record;

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
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Andre van Hoorn
 */
public abstract class AbstractMonitoringRecord implements IMonitoringRecord {

    private static final Log log = LogFactory.getLog(AbstractMonitoringRecord.class);
    private volatile long loggingTimestamp = -1;

    public final long getLoggingTimestamp() {
        return this.loggingTimestamp;
    }

    public final void setLoggingTimestamp(final long timestamp) {
        this.loggingTimestamp = timestamp;
    }

    /**
     * Creates a string representation of this record.
     *
     * Matthias should not use this method for serialization purposes
     * since this is not the purpose of Object's toString method.
     */
    @Override
    public final String toString() {
        Object[] recordVector = this.toArray();
        StringBuilder sb = new StringBuilder();
        sb.append(this.loggingTimestamp);
        for (Object curStr : recordVector) {
            sb.append(";");
            sb.append(curStr);
        }
        return sb.toString();
    }
}
