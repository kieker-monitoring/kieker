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

public class GlobalDataEntry {

	@CsvCell(columnIndex = 1, columnName = "name")
	private String name;

	@CsvCell(columnIndex = 2, columnName = "files")
	private String files;

	@CsvCell(columnIndex = 3, columnName = "modules")
	private String modules;

	@CsvCell(columnIndex = 4, columnName = "variables")
	private String variables;

	public GlobalDataEntry() {
		// default constructor required by csv library
	}

	public GlobalDataEntry(final String name, final String files, final String modules, final String variables) {
		this.name = name;
		this.files = files;
		this.modules = modules;
		this.variables = variables;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getFiles() {
		return this.files;
	}

	public void setFiles(final String files) {
		this.files = files;
	}

	public String getModules() {
		return this.modules;
	}

	public void setModules(final String modules) {
		this.modules = modules;
	}

	public String getVariables() {
		return this.variables;
	}

	public void setVariables(final String variables) {
		this.variables = variables;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof GlobalDataEntry) {
			final GlobalDataEntry other = (GlobalDataEntry) object;
			return this.checkString(this.files, other.getFiles()) && this.checkString(this.modules, other.getModules())
					&& this.checkString(this.name, other.getName())
					&& this.checkString(this.variables, other.getVariables());
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
		return this.name.hashCode() ^ this.files.hashCode() ^ this.modules.hashCode() ^ this.variables.hashCode();
	}

}
