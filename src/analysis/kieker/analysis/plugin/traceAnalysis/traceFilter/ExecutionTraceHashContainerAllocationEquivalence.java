package kieker.analysis.plugin.traceAnalysis.traceFilter;

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

import java.util.Iterator;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionTrace;

/**
 *
 * @author Andre van Hoorn
 */
class ExecutionTraceHashContainerAllocationEquivalence extends AbstractExecutionTraceHashContainer {

    private final int hashCode;

    public ExecutionTraceHashContainerAllocationEquivalence(final ExecutionTrace t) {
        super(t);
        int h = 0;
        // TODO: need a better hash function considering the order (e.g.,
        // MD5)
        for (final Execution r : t.getTraceAsSortedSet()) {
            h ^= r.getOperation().getId();
            h ^= r.getAllocationComponent().getId();
            h ^= r.getEoi();
            h ^= r.getEss();
        }
        //
        this.hashCode = h;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    private boolean executionsEqual(final Execution r1, final Execution r2) {
        if (r1 == r2) {
            return true;
        }
        if (r1 == null || r2 == null) {
            return false;
        }
        return (r1.getAllocationComponent().getId() == r2.getAllocationComponent().getId())
                && r1.getOperation().getId() == r2.getOperation().getId()
                && r1.getEoi() == r2.getEoi() && r1.getEss() == r2.getEss();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AbstractExecutionTraceHashContainer)) {
            return false;
        }
        final ExecutionTrace otherTrace = ((AbstractExecutionTraceHashContainer) obj).getExecutionTrace();
        if (super.getExecutionTrace().getLength() != otherTrace.getLength()) {
            return false;
        }
        final Iterator<Execution> otherIterator = otherTrace.getTraceAsSortedSet().iterator();
        for (final Execution r1 : super.getExecutionTrace().getTraceAsSortedSet()) {
            final Execution r2 = otherIterator.next();
            if (!this.executionsEqual(r1, r2)) {
                return false;
            }
        }
        return true;
    }
}

