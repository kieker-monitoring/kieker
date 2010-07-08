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

package kieker.analysis.plugin.traceAnalysis;

import kieker.analysis.datamodel.repository.SystemModelRepository;

/**
 *
 * @author Andre van Hoorn
 */
public abstract class AbstractTraceProcessingPlugin extends AbstractTraceAnalysisPlugin {
    private int numTracesProcessed = 0;
    private int numTracesSucceeded = 0;
    private int numTracesFailed = 0;

    private long lastTraceIdSuccess = -1;
    private long lastTraceIdError = -1;

    public AbstractTraceProcessingPlugin (final String name,
            final SystemModelRepository systemEntityFactory){
        super(name, systemEntityFactory);
    }

    protected final void reportSuccess(final long traceId){
        this.lastTraceIdSuccess = traceId;
        this.numTracesSucceeded++;
        this.numTracesProcessed++;
    }

    protected final void reportError(final long traceId){
        this.lastTraceIdError = traceId;
        this.numTracesFailed++;
        this.numTracesProcessed++;
    }

    public final int getSuccessCount(){
        return this.numTracesSucceeded;
    }

    public final int getErrorCount(){
        return this.numTracesFailed;
    }

    public final int getTotalCount(){
        return this.numTracesProcessed;
    }

    public final long getLastTraceIdError() {
        return lastTraceIdError;
    }

    public final long getLastTraceIdSuccess() {
        return lastTraceIdSuccess;
    }

    public void printStatusMessage(){
        this.printMessage(new String[] {
            "Trace processing summary: "
                    + this.numTracesProcessed + " total; "
                    + this.numTracesSucceeded + " succeeded; "
                    + this.numTracesFailed + " failed."
        });
    }
}
