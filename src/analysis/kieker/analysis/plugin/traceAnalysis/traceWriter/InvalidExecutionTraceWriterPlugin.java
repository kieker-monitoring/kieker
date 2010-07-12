package kieker.analysis.plugin.traceAnalysis.traceWriter;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.analysis.datamodel.InvalidExecutionTrace;
import kieker.analysis.datamodel.repository.SystemModelRepository;
import kieker.analysis.plugin.configuration.AbstractInputPort;
import kieker.analysis.plugin.configuration.IInputPort;
import kieker.analysis.plugin.traceAnalysis.AbstractInvalidExecutionTraceProcessingPlugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class InvalidExecutionTraceWriterPlugin extends AbstractInvalidExecutionTraceProcessingPlugin {

    private static final Log log = LogFactory.getLog(InvalidExecutionTraceWriterPlugin.class);
    private final String outputFn;
    private final BufferedWriter ps;

    public InvalidExecutionTraceWriterPlugin(
            final String name,
            final SystemModelRepository systemEntityFactory,
            final String outputFn) throws FileNotFoundException, IOException {
        super(name, systemEntityFactory);
        this.outputFn = outputFn;
        this.ps = new BufferedWriter(new FileWriter(outputFn));
    }

    @Override
    public void printStatusMessage() {
        super.printStatusMessage();
        final int numTraces = this.getSuccessCount();
        System.out.println("Wrote " + numTraces + " execution trace artifact"
                + (numTraces > 1 ? "s" : "") + " to file '"
                + this.outputFn + "'");
    }

    @Override
    public void terminate(final boolean error) {
        if (this.ps != null) {
            try {
                this.ps.close();
            } catch (IOException ex) {
                log.error("IOException", ex);
            }
        }
    }

    @Override
    public boolean execute() {
        return true; // no need to do anything here
    }

    @Override
    public IInputPort<InvalidExecutionTrace> getInvalidExecutionTraceInputPort() {
        return this.InvalidExecutionTraceInputPort;
    }
    private final IInputPort<InvalidExecutionTrace> InvalidExecutionTraceInputPort =
            new AbstractInputPort<InvalidExecutionTrace>("Invalid execution traces") {

                @Override
                public void newEvent(InvalidExecutionTrace et) {
                    try {
                        ps.append(et.getInvalidExecutionTrace().toString());
                        reportSuccess(et.getInvalidExecutionTrace().getTraceId());
                    } catch (IOException ex) {
                        reportError(et.getInvalidExecutionTrace().getTraceId());
                        log.error(ex, ex);
                    }
                }
            };
}
