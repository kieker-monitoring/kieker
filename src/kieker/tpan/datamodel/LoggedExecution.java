package kieker.tpan.datamodel;

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

/**
 *
 * @author Andre van Hoorn
 */
public class LoggedExecution extends Execution {
    private final long loggingTimestamp;

    private LoggedExecution() {
        super();
        this.loggingTimestamp = -1;
    }

    public LoggedExecution(final Operation op,
            final AllocationComponentInstance allocationComponent, final long traceId,
            final String sessionId, final int eoi, final int ess, final long tin,
            final long tout, long loggingTimestamp) {
        super(op, allocationComponent, traceId, sessionId, eoi, ess, tin, tout);
        this.loggingTimestamp = loggingTimestamp;
    }

    public final long getLoggingTimestamp() {
        return this.loggingTimestamp;
    }
}
