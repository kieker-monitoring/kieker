package kieker.tools.traceAnalysis.plugins.executionFilter;

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

import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.analysis.plugin.IAnalysisPlugin;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.configuration.IOutputPort;
import kieker.analysis.plugin.configuration.OutputPort;

/**
 * Allows to filter Execution objects based on their tin and tout timestamps.
 *
 * @author Andre van Hoorn
 */
public class TimestampFilter implements IAnalysisPlugin {

    public static final long MAX_TIMESTAMP = Long.MAX_VALUE;
    public static final long MIN_TIMESTAMP = 0;

    private final long ignoreExecutionsBeforeTimestamp;
    private final long ignorExecutionsAfterTimestamp;

    /**
     * Creates a filter instance that only passes Execution objects <i>e</i>
     * with the property <i>e.tin &gt;= ignoreExecutionsBeforeTimestamp</i> and
     * <i>e.tout &lt;= ignoreExecutionsAfterTimestamp</i>.
     *
     * @param ignoreExecutionsBeforeTimestamp
     * @param ignoreExecutionsAfterTimestamp
     */
    public TimestampFilter(
            final long ignoreExecutionsBeforeTimestamp,
            final long ignoreExecutionsAfterTimestamp) {
        this.ignoreExecutionsBeforeTimestamp = ignoreExecutionsBeforeTimestamp;
        this.ignorExecutionsAfterTimestamp = ignoreExecutionsAfterTimestamp;
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
        if (execution.getTin() < this.ignoreExecutionsBeforeTimestamp
                || execution.getTout() > this.ignorExecutionsAfterTimestamp) {
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
