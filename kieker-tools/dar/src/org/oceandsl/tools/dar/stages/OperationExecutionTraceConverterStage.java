/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package org.oceandsl.tools.dar.stages;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.stage.basic.AbstractTransformation;

/**
 * Convert old style operation execution traces to flow traces.
 *
 * @author Reiner Jung
 *
 */
public class OperationExecutionTraceConverterStage
        extends AbstractTransformation<OperationExecutionRecord, IFlowRecord> {

    private final Map<Long, Trace> traces = new HashMap<>();

    @Override
    protected void execute(final OperationExecutionRecord element) throws Exception {

        Trace trace = this.traces.get(element.getTraceId());

        if (trace == null) {
            trace = new Trace(element.getTraceId(), element.getSessionId(), element.getHostname(),
                    element.getTraceId());
            this.traces.put(element.getTraceId(), trace);
        }

        trace.records.put(element.getEoi(), element);
        trace.updateHighestEoi(element.getEoi());

// reactivate to process traces right away, make this an option
//        if (element.getEoi() == 0 && element.getEss() == 0) {
//            this.produceTrace(trace);
//        }
    }

    @Override
    protected void onTerminating() {
        this.traces.values().forEach(trace -> this.produceTrace(trace));
        super.onTerminating();

    }

    private void produceTrace(final Trace trace) {
        final SignatureProcessor processor = new SignatureProcessor();

        this.outputPort.send(new TraceMetadata(trace.traceId, trace.threadId, trace.sessionId, trace.hostname, 0, 0));

        final Stack<OperationExecutionRecord> stack = new Stack<>();

        int depth = -1;
        int orderIndex = 0;
        for (int i = 0; i <= trace.highestEoi; i++) {
            final OperationExecutionRecord record = trace.records.get(i);
            if (depth < record.getEss()) { // step up
                processor.parse(record.getOperationSignature());
                this.outputPort.send(new BeforeOperationEvent(record.getTin(), record.getTraceId(), orderIndex++,
                        processor.getOperationSignature(), processor.getClassSignature()));
                depth = record.getEss();
                stack.push(record);
            } else if (depth == record.getEss()) {
                final OperationExecutionRecord beforeOp = stack.pop();
                processor.parse(beforeOp.getOperationSignature());
                this.outputPort.send(new AfterOperationEvent(beforeOp.getTout(), beforeOp.getTraceId(), orderIndex++,
                        processor.getOperationSignature(), processor.getClassSignature()));

                processor.parse(record.getOperationSignature());
                this.outputPort.send(new BeforeOperationEvent(record.getTin(), record.getTraceId(), orderIndex++,
                        processor.getOperationSignature(), processor.getClassSignature()));
                depth = record.getEss();
                stack.push(record);
            } else {
                while (depth >= record.getEss()) {
                    final OperationExecutionRecord beforeOp = stack.pop();
                    processor.parse(beforeOp.getOperationSignature());
                    this.outputPort.send(new AfterOperationEvent(beforeOp.getTout(), beforeOp.getTraceId(),
                            orderIndex++, processor.getOperationSignature(), processor.getClassSignature()));
                    depth--;
                }

                processor.parse(record.getOperationSignature());
                this.outputPort.send(new BeforeOperationEvent(record.getTin(), record.getTraceId(), orderIndex++,
                        processor.getOperationSignature(), processor.getClassSignature()));
                depth = record.getEss();
                stack.push(record);
            }
        }

        if (depth > 0) {
            while (!stack.isEmpty()) {
                final OperationExecutionRecord beforeOp = stack.pop();
                processor.parse(beforeOp.getOperationSignature());
                this.outputPort.send(new AfterOperationEvent(beforeOp.getTout(), beforeOp.getTraceId(), orderIndex++,
                        processor.getOperationSignature(), processor.getClassSignature()));
                depth--;
            }
        }
    }

    /** Internal trace representation. */
    private class Trace {
        private final long traceId;
        private final long threadId;
        private final String sessionId;
        private final String hostname;

        private final Map<Integer, OperationExecutionRecord> records = new HashMap<>();
        private int highestEoi;

        public Trace(final long traceId, final String sessionId, final String hostname, final long threadId) {
            this.traceId = traceId;
            this.sessionId = sessionId;
            this.hostname = hostname;
            this.threadId = threadId;
            this.highestEoi = -1;
        }

        public void updateHighestEoi(final int eoi) {
            if (eoi > this.highestEoi) {
                this.highestEoi = eoi;
            }
        }
    }

}
