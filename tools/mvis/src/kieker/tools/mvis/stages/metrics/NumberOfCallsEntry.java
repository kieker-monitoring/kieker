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
package kieker.tools.mvis.stages.metrics;

import org.csveed.annotations.CsvCell;

public class NumberOfCallsEntry {

	@CsvCell(columnIndex = 1, columnName = "source-path")
	private String sourceFile;
	@CsvCell(columnIndex = 2, columnName = "source-operation")
	private String sourceFunction;
	@CsvCell(columnIndex = 3, columnName = "target-path")
	private String targetFile;
	@CsvCell(columnIndex = 4, columnName = "target-operation")
	private String targetFunction;
	@CsvCell(columnIndex = 5, columnName = "calls")
	private long calls;

	public NumberOfCallsEntry() {
		// dummy constructor for csveed
	}

	public NumberOfCallsEntry(final String sourceFile, final String sourceFunction, final String targetFile,
			final String targetFunction, final long calls) {
		this.sourceFile = sourceFile;
		this.sourceFunction = sourceFunction;
		this.targetFile = targetFile;
		this.targetFunction = targetFunction;
		this.calls = calls;
	}

	public String getSourceFile() {
		return this.sourceFile;
	}

	public void setSourceFile(final String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public String getSourceFunction() {
		return this.sourceFunction;
	}

	public void setSourceFunction(final String sourceFunction) {
		this.sourceFunction = sourceFunction;
	}

	public String getTargetFile() {
		return this.targetFile;
	}

	public void setTargetFile(final String targetFile) {
		this.targetFile = targetFile;
	}

	public String getTargetFunction() {
		return this.targetFunction;
	}

	public void setTargetFunction(final String targetFunction) {
		this.targetFunction = targetFunction;
	}

	public long getCalls() {
		return this.calls;
	}

	public void setCalls(final long calls) {
		this.calls = calls;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof NumberOfCallsEntry) {
			final NumberOfCallsEntry other = (NumberOfCallsEntry) object;
			return this.checkString(this.sourceFile, other.getSourceFile())
					&& this.checkString(this.sourceFunction, other.getSourceFunction())
					&& this.checkString(this.targetFile, other.getTargetFile())
					&& this.checkString(this.targetFunction, other.getTargetFunction())
					&& (this.calls == other.getCalls());
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
		return this.sourceFile.hashCode() ^ this.sourceFunction.hashCode() ^ this.targetFile.hashCode()
				^ this.targetFunction.hashCode() ^ Long.hashCode(this.calls);
	}
}
