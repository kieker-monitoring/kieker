package kieker.analysis.datamodel;

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
 * @author Andre van Hoorn
 */
public class InvalidExecutionTrace implements IAnalysisEvent {
    private final ExecutionTrace invalidExecutionTraceArtifacts;

    public ExecutionTrace getInvalidExecutionTraceArtifacts() {
        return invalidExecutionTraceArtifacts;
    }

    public InvalidExecutionTrace(final ExecutionTrace invalidExecutionTrace){
        this.invalidExecutionTraceArtifacts = invalidExecutionTrace;
    }

    @Override
    public String toString() {
        StringBuilder strBuild =
                new StringBuilder("Invalid Trace: ");
        strBuild.append(this.invalidExecutionTraceArtifacts.toString());
        return strBuild.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof InvalidExecutionTrace)){
            return false;
        }
        if (this == obj) {
            return true;
        }
        InvalidExecutionTrace other = (InvalidExecutionTrace)obj;

        return other.getInvalidExecutionTraceArtifacts().equals(this.invalidExecutionTraceArtifacts);
    }
}
