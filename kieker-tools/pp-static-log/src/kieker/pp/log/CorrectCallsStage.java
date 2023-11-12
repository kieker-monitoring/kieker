/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
 ***************************************************************************/
package kieker.pp.log;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.oceandsl.analysis.code.stages.data.CallerCalleeEntry;

/**
 * Read process caller callee events and fills in the callee's file name.
 *
 * <ul>
 * <li>The <b>mapInputPort</b> receives an event containing the function, file name map.
 * <li>The <b>inputPort</b> receives {@link CallerCalleeEntry} events.
 * </ul>
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class CorrectCallsStage extends AbstractStage {

    private final InputPort<CallerCalleeEntry> inputPort = this.createInputPort(CallerCalleeEntry.class);
    private final InputPort<Map<String, String>> mapInputPort = this.createInputPort();
    private final OutputPort<CallerCalleeEntry> outputPort = this.createOutputPort(CallerCalleeEntry.class);

    private final Map<String, String> functionFileMap = new HashMap<>();
    private final Map<String, String> learnedFileMap = new HashMap<>();
    private CallerCalleeEntry unprocessedInvocation;

    @Override
    protected void execute() throws Exception {
        if (this.unprocessedInvocation == null) {
            this.unprocessedInvocation = this.inputPort.receive();
        }
        if (this.unprocessedInvocation != null) {
            this.learnedFileMap.put(this.unprocessedInvocation.getCaller(), this.unprocessedInvocation.getSourcePath());
            final String calleeFile = this.functionFileMap
                    .get(this.unprocessedInvocation.getCallee().toLowerCase(Locale.ROOT));
            if (calleeFile != null) {
                this.unprocessedInvocation.setTargetPath(calleeFile);
                this.outputPort.send(this.unprocessedInvocation);
                this.unprocessedInvocation = null; // NOPMD NullAssignment
            } else if (!this.functionFileMap.isEmpty()) {
                this.logger.warn("No match found for function {}", this.unprocessedInvocation.getCallee());
                final String guessedCalleeFile = this.learnedFileMap.get(this.unprocessedInvocation.getCallee());
                if (guessedCalleeFile != null) {
                    this.unprocessedInvocation.setTargetPath(guessedCalleeFile);
                } else {
                    this.unprocessedInvocation
                            .setTargetPath(String.format("no file for %s", this.unprocessedInvocation.getCallee()));
                }
                this.outputPort.send(this.unprocessedInvocation);
                this.unprocessedInvocation = null; // NOPMD
            }
        }
        final Map<String, String> mapping = this.mapInputPort.receive();
        if (mapping != null) {
            this.functionFileMap.putAll(mapping);
        }
    }

    public InputPort<CallerCalleeEntry> getInputPort() {
        return this.inputPort;
    }

    public InputPort<Map<String, String>> getMapInputPort() {
        return this.mapInputPort;
    }

    public OutputPort<CallerCalleeEntry> getOutputPort() {
        return this.outputPort;
    }
}
