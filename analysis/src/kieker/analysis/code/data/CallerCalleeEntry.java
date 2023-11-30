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
package kieker.analysis.code.data;

import org.csveed.annotations.CsvCell;

/**
 * @author Reiner Jung
 * @since 1.0
 *
 */
public class CallerCalleeEntry {

	@CsvCell(columnIndex = 1, columnName = "caller-path")
	private String sourcePath;
	@CsvCell(columnIndex = 2, columnName = "caller-module")
	private String sourceModule;
	@CsvCell(columnIndex = 3, columnName = "caller-operation")
	private String caller;
	@CsvCell(columnIndex = 4, columnName = "callee-path")
	private String targetPath;
	@CsvCell(columnIndex = 5, columnName = "callee-module")
	private String targetModule;
	@CsvCell(columnIndex = 6, columnName = "callee-operation")
	private String callee;

	public CallerCalleeEntry() {
		// default constructor required for csv library
	}

	public CallerCalleeEntry(final String sourcePath, final String sourceModule, final String caller,
			final String targetPath, final String targetModule, final String callee) {
		this.sourcePath = sourcePath;
		this.targetPath = targetPath;
		this.sourceModule = sourceModule;
		this.targetModule = targetModule;
		this.caller = caller;
		this.callee = callee;
	}

	public String getCallee() {
		return this.callee;
	}

	public void setCallee(final String callee) {
		this.callee = callee;
	}

	public String getCaller() {
		return this.caller;
	}

	public void setCaller(final String caller) {
		this.caller = caller;
	}

	public String getSourcePath() {
		return this.sourcePath;
	}

	public void setSourcePath(final String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getTargetPath() {
		return this.targetPath;
	}

	public void setTargetPath(final String targetPath) {
		this.targetPath = targetPath;
	}

	public String getSourceModule() {
		return this.sourceModule;
	}

	public void setSourceModule(final String sourceModule) {
		this.sourceModule = sourceModule;
	}

	public String getTargetModule() {
		return this.targetModule;
	}

	public void setTargetModule(final String targetModule) {
		this.targetModule = targetModule;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof CallerCalleeEntry) {
			final CallerCalleeEntry other = (CallerCalleeEntry) object;
			return this.checkString(this.sourcePath, other.getSourcePath())
					&& this.checkString(this.sourceModule, other.getSourceModule())
					&& this.checkString(this.caller, other.getCaller())
					&& this.checkString(this.targetPath, other.getTargetPath())
					&& this.checkString(this.targetModule, other.getTargetModule())
					&& this.checkString(this.callee, other.getCallee());
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
		return this.sourcePath.hashCode() ^ this.sourceModule.hashCode() ^ this.caller.hashCode()
				^ this.targetPath.hashCode() ^ this.targetModule.hashCode() ^ this.callee.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s:%s:%s -> %s:%s:%s", this.sourcePath, this.sourceModule, this.caller, this.targetPath,
				this.targetModule, this.callee);
	}
}
