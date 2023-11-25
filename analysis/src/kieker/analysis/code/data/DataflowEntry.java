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
package kieker.analysis.code.data;

import org.csveed.annotations.CsvCell;

import kieker.model.analysismodel.execution.EDirection;

public class DataflowEntry {

	@CsvCell(columnIndex = 1, columnName = "source-path")
	private String sourcePath;

	@CsvCell(columnIndex = 2, columnName = "source-module")
	private String sourceModule;

	@CsvCell(columnIndex = 3, columnName = "source-operation")
	private String sourceOperation;

	@CsvCell(columnIndex = 4, columnName = "target-path")
	private String targetPath;

	@CsvCell(columnIndex = 5, columnName = "target-module")
	private String targetModule;

	@CsvCell(columnIndex = 6, columnName = "target-operation")
	private String targetOperation;

	@CsvCell(columnIndex = 7, columnName = "direction")
	private EDirection direction;

	public DataflowEntry() {
		// default constructor required by csv library
	}

	public DataflowEntry(final String sourcePath, final String sourceModule, final String sourceOperation,
			final String targetPath, final String targetModule, final String targetOperatio,
			final EDirection direction) {
		this.sourcePath = sourcePath;
		this.sourceModule = sourceModule;
		this.sourceOperation = sourceOperation;
		this.targetPath = targetPath;
		this.targetModule = targetModule;
		this.targetOperation = targetOperatio;
		this.direction = direction;
	}

	public String getSourcePath() {
		return this.sourcePath;
	}

	public void setSourcePath(final String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getSourceModule() {
		return this.sourceModule;
	}

	public void setSourceModule(final String sourceModule) {
		this.sourceModule = sourceModule;
	}

	public String getSourceOperation() {
		return this.sourceOperation;
	}

	public void setSourceOperation(final String sourceOperation) {
		this.sourceOperation = sourceOperation;
	}

	public String getTargetPath() {
		return this.targetPath;
	}

	public void setTargetPath(final String targetPath) {
		this.targetPath = targetPath;
	}

	public String getTargetModule() {
		return this.targetModule;
	}

	public void setTargetModule(final String targetModule) {
		this.targetModule = targetModule;
	}

	public String getTargetOperation() {
		return this.targetOperation;
	}

	public void setTargetOperation(final String targetOperation) {
		this.targetOperation = targetOperation;
	}

	public EDirection getDirection() {
		return this.direction;
	}

	public void setDirection(final EDirection direction) {
		this.direction = direction;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof DataflowEntry) {
			final DataflowEntry other = (DataflowEntry) object;
			return this.checkString(this.sourcePath, other.getSourcePath())
					&& this.checkString(this.sourceModule, other.getSourceModule())
					&& this.checkString(this.sourceOperation, other.getSourceOperation())
					&& this.checkString(this.targetPath, other.getTargetPath())
					&& this.checkString(this.targetModule, other.getTargetModule())
					&& this.checkString(this.targetOperation, other.getTargetOperation());
		} else {
			return false;
		}
	}

	private boolean checkString(final String left, final String right) {
		if ((left == null) && (right == null)) {
			return true;
		} else if ((left != null) && (right != null)) {
			return left.equals(right);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.sourcePath.hashCode() ^ this.sourceModule.hashCode() ^ this.sourceOperation.hashCode()
				^ this.targetPath.hashCode() ^ this.targetModule.hashCode() ^ this.targetOperation.hashCode();
	}

}
