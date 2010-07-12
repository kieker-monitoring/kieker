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

package kieker.analysis.plugin.traceAnalysis.executionFilter;

import java.util.TreeSet;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.plugin.IAnalysisPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;

/**
 *
 * @author Andre van Hoorn
 */
public class TraceIdFilter implements IAnalysisPlugin {

    private final TreeSet<Long> selectedTraces;

    /**
     *
     * @param selectedTraces 
     */
    public TraceIdFilter(final TreeSet<Long> selectedTraces) {
        this.selectedTraces = selectedTraces;
    }

    public IInputPort<Execution> getExecutionInputPort() {
        return this.executionInputPort;
    }
    private final IInputPort<Execution> executionInputPort =
            new AbstractInputPort<Execution>("Execution input") {

        @Override
        public void newEvent(Execution event) {
            newExecution(event);
        }
    };

    public IOutputPort<Execution> getExecutionOutputPort(){
        return this.executionOutputPort;
    }
    private final OutputPort<Execution> executionOutputPort =
            new OutputPort<Execution>("Execution output");

    private void newExecution(Execution execution){
        if (this.selectedTraces != null
                && !this.selectedTraces.contains(execution.getTraceId())) {
            // not interested in this trace
            return;
        }
        this.executionOutputPort.deliver(execution);
    }

    @Override
    public boolean execute() {
        return true; // do nothing
    }

    @Override
    public void terminate(boolean error) {
        return; // do nothing
    }

}
