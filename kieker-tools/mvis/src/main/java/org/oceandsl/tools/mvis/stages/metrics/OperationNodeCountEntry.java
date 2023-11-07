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
package org.oceandsl.tools.mvis.stages.metrics;

import org.csveed.annotations.CsvCell;

public class OperationNodeCountEntry {

    @CsvCell(columnIndex = 1, columnName = "module")
    private String module;
    @CsvCell(columnIndex = 2, columnName = "operation")
    private String operation;
    @CsvCell(columnIndex = 3, columnName = "in-edges")
    private int inEdges;
    @CsvCell(columnIndex = 4, columnName = "out-edges")
    private int outEdges;

    public OperationNodeCountEntry() {
        // dummy constructor for csveed
    }

    public OperationNodeCountEntry(final String module, final String operation, final int inEdges, final int outEdges) {
        this.module = module;
        this.operation = operation;
        this.inEdges = inEdges;
        this.outEdges = outEdges;
    }

    public String getModule() {
        return this.module;
    }

    public void setModule(final String module) {
        this.module = module;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(final String operation) {
        this.operation = operation;
    }

    public int getInEdges() {
        return this.inEdges;
    }

    public void setInEdges(final int inEdges) {
        this.inEdges = inEdges;
    }

    public int getOutEdges() {
        return this.outEdges;
    }

    public void setOutEdges(final int outEdges) {
        this.outEdges = outEdges;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof OperationNodeCountEntry) {
            final OperationNodeCountEntry other = (OperationNodeCountEntry) object;
            return this.checkString(this.module, other.getModule())
                    && this.checkString(this.operation, other.getOperation()) && this.inEdges == other.getInEdges()
                    && this.outEdges == other.getOutEdges();
        } else {
            return false;
        }
    }

    private boolean checkString(final String left, final String right) {
        if (left == null && right == null) {
            return true;
        } else if (left != null && right != null) {
            return left.equals(right);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.module.hashCode() ^ this.operation.hashCode() ^ Integer.hashCode(this.inEdges)
                ^ Integer.hashCode(this.outEdges);
    }
}
