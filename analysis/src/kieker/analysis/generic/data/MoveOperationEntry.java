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
package kieker.analysis.generic.data;

import org.csveed.annotations.CsvCell;

/**
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class MoveOperationEntry {

	@CsvCell(columnIndex = 1, columnName = "source-component")
	private String sourceComponentName;

	@CsvCell(columnIndex = 2, columnName = "target-component")
	private String targetComponentName;

	@CsvCell(columnIndex = 3, columnName = "operation")
	private String operationName;

	public MoveOperationEntry() {
		// pojo constructor
	}

	public MoveOperationEntry(final String sourceComponentName, final String targetComponentName,
			final String operationName) {
		this.sourceComponentName = sourceComponentName;
		this.targetComponentName = targetComponentName;
		this.operationName = operationName;
	}

	public String getSourceComponentName() {
		return this.sourceComponentName;
	}

	public void setSourceComponentName(final String sourceComponentName) {
		this.sourceComponentName = sourceComponentName;
	}

	public String getTargetComponentName() {
		return this.targetComponentName;
	}

	public void setTargetComponentName(final String targetComponentName) {
		this.targetComponentName = targetComponentName;
	}

	public String getOperationName() {
		return this.operationName;
	}

	public void setOperationName(final String operationName) {
		this.operationName = operationName;
	}

}
