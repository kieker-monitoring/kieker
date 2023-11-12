/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.sar;

import org.csveed.annotations.CsvCell;

import kieker.model.analysismodel.execution.EDirection;

/**
 * Storage operation dataflow record.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class StorageOperationDataflow {

    @CsvCell(columnIndex = 1, columnName = "common-block")
    private String commonBlockName;

    @CsvCell(columnIndex = 2, columnName = "file")
    private String fileName;

    @CsvCell(columnIndex = 3, columnName = "module")
    private String moduleName;

    @CsvCell(columnIndex = 4, columnName = "operation")
    private String operationName;

    @CsvCell(columnIndex = 5, columnName = "direction")
    private EDirection direction;

    public StorageOperationDataflow() {
        // default constructor for csv reader
    }

    public StorageOperationDataflow(final String commonBlockName, final String fileName, final String moduleName,
            final String operationName, final EDirection direction) {
        this.commonBlockName = commonBlockName;
        this.fileName = fileName;
        this.moduleName = moduleName;
        this.operationName = operationName;
        this.direction = direction;
    }

    public String getCommonBlockName() {
        return this.commonBlockName;
    }

    public void setCommonBlockName(final String commonBlockName) {
        this.commonBlockName = commonBlockName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(final String moduleName) {
        this.moduleName = moduleName;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public void setOperationName(final String operationName) {
        this.operationName = operationName;
    }

    public EDirection getDirection() {
        return this.direction;
    }

    public void setDirection(final EDirection direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof StorageOperationDataflow) {
            final StorageOperationDataflow other = (StorageOperationDataflow) object;
            return this.checkString(this.commonBlockName, other.getCommonBlockName())
                    && this.checkString(this.fileName, other.getFileName())
                    && this.checkString(this.moduleName, other.getModuleName())
                    && this.checkString(this.operationName, other.getOperationName())
                    && this.direction.equals(other.getDirection());
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
        return this.commonBlockName.hashCode() ^ this.fileName.hashCode() ^ this.moduleName.hashCode()
                ^ this.operationName.hashCode() ^ this.direction.hashCode();
    }

}
