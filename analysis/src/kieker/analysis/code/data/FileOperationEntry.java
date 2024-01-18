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

public class FileOperationEntry {

	@CsvCell(columnIndex = 1, columnName = "path")
	private String path;
	@CsvCell(columnIndex = 2, columnName = "operation")
	private String name;

	public FileOperationEntry() {
		// default constructor required by csv library
	}

	public FileOperationEntry(final String path, final String name) {
		this.path = path;
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(final String path) {
		this.path = path;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof FileOperationEntry) {
			final FileOperationEntry other = (FileOperationEntry) object;
			return this.checkString(this.name, other.getName()) && this.checkString(this.path, other.getPath());
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
		return this.name.hashCode() ^ this.path.hashCode();
	}
}
