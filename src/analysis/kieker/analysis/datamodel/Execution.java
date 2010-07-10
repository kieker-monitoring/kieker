package kieker.analysis.datamodel;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
public class Execution implements IAnalysisEvent {
    private final Operation operation;
    private final AllocationComponent allocationComponent;
    private long traceId;
    private String sessionId;
    private final int eoi;
    private final int ess;
    private final long tin;
    private final long tout;

    public Execution() {
        this.operation = null;
        this.allocationComponent = null;
        this.traceId = -1;
        this.sessionId = null;
        this.eoi = -1;
        this.ess = -1;
        this.tin = -1;
        this.tout = -1;
    }

    public Execution(final Operation op, final AllocationComponent allocationComponent,
            final long traceId, final String sessionId, final int eoi, final int ess,
            final long tin, final long tout) {
        this.operation = op;
        this.allocationComponent = allocationComponent;
        this.traceId = traceId;
        this.sessionId = sessionId;
        this.eoi = eoi;
        this.ess = ess;
        this.tin = tin;
        this.tout = tout;
    }

    public final AllocationComponent getAllocationComponent() {
        return allocationComponent;
    }

    public final int getEoi() {
        return eoi;
    }

    public final int getEss() {
        return ess;
    }

    public final Operation getOperation() {
        return operation;
    }

    public final String getSessionId() {
        return sessionId;
    }

    public final long getTin() {
        return tin;
    }

    public final long getTout() {
        return tout;
    }

    public final long getTraceId() {
        return traceId;
    }

    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
            strBuild.append("[").append(this.eoi)
                    .append(",").append(this.ess).append("]").append(" ");
            strBuild.append(this.tin).append("-").append(this.tout).append(" ");
            strBuild.append(this.allocationComponent.toString()).append(".");
            strBuild.append(this.operation.getSignature().getName()).append(" ");

            strBuild.append((this.sessionId!=null)?this.sessionId:"<NOSESSIONID>");

            return strBuild.toString();
    }
}
