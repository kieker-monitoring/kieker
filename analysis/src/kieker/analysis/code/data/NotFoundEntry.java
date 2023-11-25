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

public class NotFoundEntry {

	@CsvCell(columnIndex = 1, columnName = "file-path")
	private String fileName;
	@CsvCell(columnIndex = 2, columnName = "module-name")
	private String moduleName;
	@CsvCell(columnIndex = 3, columnName = "operation")
	private String operation;
	@CsvCell(columnIndex = 4, columnName = "call")
	private String call;

	public NotFoundEntry() {
		// default constructor required by csv library
	}

	public NotFoundEntry(final String fileName, final String moduleName, final String operation, final String call) {
		this.fileName = fileName;
		this.moduleName = moduleName;
		this.operation = operation;
		this.call = call;
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

	public String getOperation() {
		return this.operation;
	}

	public void setOperation(final String operation) {
		this.operation = operation;
	}

	public String getCall() {
		return this.call;
	}

	public void setCall(final String call) {
		this.call = call;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof NotFoundEntry) {
			final NotFoundEntry other = (NotFoundEntry) object;
			return this.checkString(this.call, other.getCall()) && this.checkString(this.fileName, other.getFileName())
					&& this.checkString(this.moduleName, other.getModuleName())
					&& this.checkString(this.operation, other.getOperation());
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
		return this.call.hashCode() ^ this.fileName.hashCode() ^ this.moduleName.hashCode() ^ this.operation.hashCode();
	}
}
